package cc.allio.uno.component.netty;

import cc.allio.uno.component.netty.config.NettyServerConfig;
import cc.allio.uno.component.netty.handler.ServerIdleTrigger;
import cc.allio.uno.component.netty.model.HeartBeats;
import cc.allio.uno.component.netty.model.RemoteTransporter;
import cc.allio.uno.component.netty.codec.RemoteTransporterDecoder;
import cc.allio.uno.component.netty.codec.RemoteTransporterEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * 一个netty的服务端
 *
 * @author jiangw
 * @date 2020/11/26 11:18
 * @since 1.0
 */
@Slf4j
public class NettyServer extends AbstractNettyService {

    private final NettyServerConfig serverConfig;

    /**
     * 服务端的启动器
     */
    private ServerBootstrap serverBootstrap;

    /**
     * reactor模式中接受连接请求的main-reactor
     */
    private NioEventLoopGroup boss;

    /**
     * reactor模式中业务处理的sub-reactor
     */
    private NioEventLoopGroup worker;

    private Channel serverChannel;

    /**
     * 以默认配置启动netty-server
     */
    public NettyServer() {
        this(new NettyServerConfig());
    }

    public NettyServer(NettyServerConfig serverConfig) {
        super();
        this.serverConfig = serverConfig;
        init();
    }

    @Override
    public void init() {
        serverBootstrap = new ServerBootstrap();
        boss = new NioEventLoopGroup(1, new DefaultThreadFactory("server boss thread-"));
        worker = new NioEventLoopGroup(serverConfig.getWorkerThreads(), new DefaultThreadFactory("server client thread-"));

        // 处理IO远大于非IO, 100%。
        worker.setIoRatio(100);
        // reactor
        serverBootstrap.group(boss, worker);

        // options
        // 池化缓存区
        serverBootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        serverBootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        // 地址复用
        serverBootstrap.option(ChannelOption.SO_REUSEADDR, true);
        serverBootstrap.childOption(ChannelOption.SO_REUSEADDR, true);

        // TCP_NODELAY 为true时采用Nagle算法
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        // 长连接
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        // 远程连接关闭，是否触发ChannelInputShutdownEvent事件，在userChannelTrigger
        serverBootstrap.childOption(ChannelOption.ALLOW_HALF_CLOSURE, true);

    }

    @Override
    public void start() {
        // channel type
        serverBootstrap.channel(NioServerSocketChannel.class);
        // handler
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(
                        new LengthFieldBasedFrameDecoder(16384, 0, 4, 0, 4),
                        new IdleStateHandler(true, HeartBeats.config().getReadIdleTime(), 0, 0, HeartBeats.config().getUnit()),
                        new ServerIdleTrigger(),
                        new RemoteTransporterEncoder(),
                        new RemoteTransporterDecoder(),
                        new NettyServerHandler()
                );
                ChannelHandler extraHandler = extraHandler();
                if (extraHandler != null) {
                    ch.pipeline().addLast(extraHandler);
                }
            }
        });

        serverBootstrap.localAddress(new InetSocketAddress(serverConfig.getListenerPort()));
        try {
            ChannelFuture future = serverBootstrap.bind().sync();
            serverChannel = future.channel();
            if (log.isInfoEnabled()) {
                log.info("netty server started successful! listener port: {}", serverConfig.getListenerPort());
            }
            serverChannel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdownGracefully() {
        if (serverBootstrap != null) {
            log.info("server shutdown...");
            if (serverChannel != null) {
                serverChannel.close();
            }
            // 关闭反应器组
            boss.shutdownGracefully();
            worker.shutdownGracefully();

            // 关闭业务处理器
            shutdownProcessors();
        }
    }

    @Override
    protected void processAck(RemoteTransporter remoteTransporter) {

    }

    public Channel channel() {
        return this.serverChannel;
    }

    @Override
    protected ChannelHandler extraHandler() {
        return null;
    }

    class NettyServerHandler extends SimpleChannelInboundHandler<RemoteTransporter> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RemoteTransporter msg) throws Exception {
            doRequestAndResponse(ctx, msg);
        }
    }
}