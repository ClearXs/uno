package cc.allio.uno.netty.handler;

import cc.allio.uno.netty.ChannelGroup;
import cc.allio.uno.netty.exception.RemoteException;
import cc.allio.uno.netty.concurrent.Callback;
import cc.allio.uno.netty.config.GlobeConfig;
import cc.allio.uno.netty.transport.RemoteAddress;
import cc.allio.uno.netty.transport.UnresolvedAddress;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * 客户端重连检测
 *
 * @author j.x
 * @since 1.0
 */
@ChannelHandler.Sharable
public abstract class ConnectorWatchDog extends ChannelInboundHandlerAdapter implements TimerTask, ChannelHandlerHolder {

    private static Logger logger = LoggerFactory.getLogger(ConnectorWatchDog.class);
    private static final int START = 1;
    private static final int STOP = 2;

    private final Bootstrap bootstrap;

    private final Timer timer;
    private volatile int state = START;

    /**
     * 远程连接地址
     */
    private UnresolvedAddress socketAddress;


    private final ChannelGroup channelGroup;

    /**
     * 第一次连接，需要获取连接地址，所以需要置为true
     */
    private volatile boolean firstConnect = true;

    /**
     * 重连次数，考虑到这个处理器是共享的，所以会出现线程不安全的情况，使用线程本地存储进行操作
     */
    private int attempts;

    /**
     * 回调
     */
    private Callback callback;

    public ConnectorWatchDog(Bootstrap bootstrap, Timer timer, ChannelGroup channelGroup, Callback callback) {
        this.bootstrap = bootstrap;
        this.timer = timer;
        this.channelGroup = channelGroup;
        this.callback = callback;
    }

    public void start() {
        state = START;
    }

    public void stop() {
        state = STOP;
    }

    @Override
    public synchronized void channelActive(ChannelHandlerContext ctx) throws Exception {
        attempts = 0;
        firstConnect = true;
        Channel channel = ctx.channel();
        if (channelGroup != null) {
            channelGroup.add(channel);
        }
        if (logger.isInfoEnabled()) {
            logger.info("connector msg: {}", channel);
        }
        // 通知回调
        if (callback != null) {
            callback.acceptable(null);
        }
        // 通知流水线的处理器重连成功
        ctx.fireChannelActive();
    }

    @Override
    public synchronized void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (!isReConnect()) {
            if (logger.isWarnEnabled()) {
                logger.warn("cancel re connect");
            }
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("client started re connection");
        }
        if (firstConnect) {
            InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            this.socketAddress = new RemoteAddress(socketAddress.getHostString(), socketAddress.getPort());
            firstConnect = false;
        }
        attempts++;
        long timed = 4L << attempts;
        if (timed >= GlobeConfig.MAX_DELAY_TIMED) {
            timed = GlobeConfig.MAX_DELAY_TIMED;
        }
        timer.newTimeout(this, timed, TimeUnit.MILLISECONDS);
        if (callback != null) {
            callback.rejected(new RemoteException("reconnection failed"));
        }
        ctx.fireChannelInactive();
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        ChannelFuture channelFuture;
        synchronized (bootstrap) {
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(channelHandlers());
                }
            });
            // 重连
            channelFuture = bootstrap.connect(InetSocketAddress.createUnresolved(socketAddress.getHost(), socketAddress.getPort()));
        }
        channelFuture.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                if (logger.isInfoEnabled()) {
                    logger.info("client re connection successful");
                }
            } else {
                future.channel().pipeline().fireChannelInactive();
            }
        });
    }

    private boolean isStarted() {
        return state == START;
    }

    private boolean isReConnect() {
        return isStarted() && channelGroup != null;
    }
}
