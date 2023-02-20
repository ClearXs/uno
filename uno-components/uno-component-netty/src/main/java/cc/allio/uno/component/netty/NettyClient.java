package cc.allio.uno.component.netty;

import cc.allio.uno.component.netty.concurrent.Callback;
import cc.allio.uno.component.netty.config.GlobeConfig;
import cc.allio.uno.component.netty.config.NettyClientConfig;
import cc.allio.uno.component.netty.handler.ClientIdleTrigger;
import cc.allio.uno.component.netty.handler.ConnectorWatchDog;
import cc.allio.uno.component.netty.model.HeartBeats;
import cc.allio.uno.component.netty.model.RemoteTransporter;
import cc.allio.uno.component.netty.transport.UnresolvedAddress;
import cc.allio.uno.component.netty.codec.RemoteTransporterDecoder;
import cc.allio.uno.component.netty.codec.RemoteTransporterEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一个netty的客户端
 *
 * @author jiangw
 * @date 2020/11/26 11:44
 * @since 1.0
 */
@Slf4j
public class NettyClient extends AbstractNettyService {

    private final NettyClientConfig clientConfig;
    private Bootstrap bootstrap;
    private NioEventLoopGroup worker;
    private final Timer timer = new HashedWheelTimer();
    private int retryTimed = 0;
    private final AtomicBoolean isRetry = new AtomicBoolean(false);

    public NettyClient(UnresolvedAddress address) {
        this(new NettyClientConfig(address));
    }

    public NettyClient(NettyClientConfig clientConfig) {
        super();
        this.clientConfig = clientConfig;
        init();
    }

    @Override
    public void init() {
        bootstrap = new Bootstrap();
        worker = new NioEventLoopGroup(clientConfig.getWorkThreads(), new DefaultThreadFactory("netty client"));
        bootstrap.group(worker);

        // IO利用率
        worker.setIoRatio(100);
        // options
        // 池化缓存区
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        // 连接超时时间
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, GlobeConfig.CONNECT_TIMEOUT_MILLIS);
        // 地址复用
        bootstrap.option(ChannelOption.SO_REUSEADDR, true);
        // 长连接
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        // TCP
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        // 关闭连接不发通知
        bootstrap.option(ChannelOption.ALLOW_HALF_CLOSURE, false);

        bootstrap.channel(NioSocketChannel.class);
    }

    @Override
    public void start() {
    }

    /**
     * 客户端连接通用方法，方法加锁，阻塞调用者，直至可以连接
     *
     * @param channelGroup 通道组，重连成功时向通道组添加新的通道
     * @param callback     回调方法
     * @return 连接成功时，直接返回相应的连接器
     * @throws InterruptedException 线程中断异常
     */
    public Connector connect(ChannelGroup channelGroup, Callback callback) throws InterruptedException, ExecutionException {
        UnresolvedAddress address = clientConfig.getDefaultAddress();
        if (address == null) {
            throw new IllegalArgumentException("default address is empty");
        }
        return connect(address, channelGroup, 4, callback);
    }

    /**
     * @param address 远程服务器地址
     * @param weight  客户端连接权重
     * @see #connect(ChannelGroup, Callback)
     */
    public Connector connect(UnresolvedAddress address, ChannelGroup channelGroup, final int weight, Callback callback) throws InterruptedException, ExecutionException {
        ExecutorService connector = Executors.newSingleThreadExecutor();
        final ReentrantLock connectionLock = new ReentrantLock();
        final Condition condition = connectionLock.newCondition();
        Future<Connector> connectorFuture = connector.submit(() -> {
            connectionLock.lock();
            try {
                Connector sConnector = null;
                while (!isRetry.get()) {
                    sConnector = callConnect(address, channelGroup, weight, callback);
                }
                condition.signalAll();
                return sConnector;
            } finally {
                connectionLock.unlock();
            }
        });
        connectionLock.lock();
        try {
            condition.await();
            connector.shutdown();
        } finally {
            connectionLock.unlock();
        }
        return connectorFuture.get();
    }

    private Connector callConnect(UnresolvedAddress address, ChannelGroup channelGroup, final int weight, Callback callback) throws InterruptedException {
        // 重连
        final ConnectorWatchDog connectorWatchDog = new ConnectorWatchDog(bootstrap, timer, channelGroup, callback) {

            @Override
            public ChannelHandler[] channelHandlers() {
                ChannelHandler extraHandler = extraHandler();
                List<ChannelHandler> handlers = Arrays.asList(
                        this,
                        new LengthFieldPrepender(4),
                        new IdleStateHandler(0, HeartBeats.config().getWriteIdleTime(), 0, HeartBeats.config().getUnit()),
                        new ClientIdleTrigger(),
                        new RemoteTransporterDecoder(),
                        new RemoteTransporterEncoder(),
                        new NettyClientHandler()
                );
                if (extraHandler != null) {
                    handlers = new ArrayList<>(handlers);
                    handlers.add(extraHandler);
                }
                return handlers.toArray(new ChannelHandler[0]);
            }
        };

        Connector connector = new NettyAbstractConnector(address, channelGroup) {
            @Override
            public void setReConnect(boolean reConnect) {
                // 确定是否需要进行重连，如果消费者与生产者之间，不进行重连，由注册中心控制。
                if (reConnect) {
                    connectorWatchDog.start();
                } else {
                    connectorWatchDog.stop();
                }
            }

            @Override
            public int weight() {
                return weight;
            }
        };
        try {
            synchronized (bootstrapLock()) {
                // 处理器
                bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(connectorWatchDog.channelHandlers());
                    }
                });
                ChannelFuture channelFuture = bootstrap.connect(InetSocketAddress.createUnresolved(address.getHost(), address.getPort())).sync();
                connector.setChannelFuture(channelFuture);
            }
            if (callback != null) {
                callback.acceptable(connector);
            }
            isRetry.set(true);
        } catch (Throwable ex) {
            if (callback != null) {
                callback.rejected(ex);
            }
            retryTimed++;
            long delay = 4L << retryTimed;
            if (delay >= GlobeConfig.MAX_DELAY_TIMED) {
                delay = GlobeConfig.MAX_DELAY_TIMED;
            }
            Thread.sleep(delay);
            isRetry.set(false);
        }
        return connector;
    }

    @Override
    public void shutdownGracefully() throws InterruptedException {
        if (bootstrap != null) {
            log.info("client shutdown...");
            // 关闭工作反应器
            worker.shutdownGracefully();
            // 关闭业务处理器
            shutdownProcessors();
        }
    }

    protected Bootstrap bootstrapLock() {
        return bootstrap;
    }

    @Override
    protected void processAck(RemoteTransporter remoteTransporter) {

    }

    @Override
    protected ChannelHandler extraHandler() {
        return null;
    }

    class NettyClientHandler extends SimpleChannelInboundHandler<RemoteTransporter> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RemoteTransporter msg) throws Exception {
            doRequestAndResponse(ctx, msg);
        }
    }
}
