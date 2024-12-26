package cc.allio.uno.websocket;

import cc.allio.uno.core.task.CronTask;
import cc.allio.uno.core.task.ProxyBufferTimerSegmentTask;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.JsonUtils;
import jakarta.websocket.*;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <b>提供websocket基本功能</b>
 * <p>For Example:</p>
 * <pre><code>
 *     &copy;Controller
 *     &copy;ServerEndpoint("test")
 *     public class DemoWebSocketEndpoint extends BaseWebsocketEndpoint {
 *        ...
 *     }
 * </code></pre>
 * <b>具有广播推送功能</b>
 *
 * @param <R> 数据接收范型
 * @author j.x
 * @since 1.0
 */
@Slf4j
public abstract class BaseWebsocketEndpoint<R> implements WebSocketEndpoint {

    /**
     * 当前只能存在一个连接
     */
    private Session session;

    /**
     * 心跳连接的状态</br>
     * <ul>
     *     <ol>开启连接时设置为true</ol>
     *     <ol>{@link #doOnClose()}调用时设置为false<</ol>
     *     <ol>发送信息时设置为true</ol>
     * </ul>
     * <p>判断</p>
     * <ul>
     *     <ol>到达心跳时间时,如果为true不断开连接，并设置为false</ol>
     *     <ol>到达心跳时间时,如果为false断开连接</ol>
     * </ul>
     */
    private final AtomicBoolean heartbeatsStatus = new AtomicBoolean(false);

    /**
     * 检查心跳的任务
     */
    private CronTask<Object> checkHeartBeatsTask;

    /**
     * 收集返回数据任务
     */
    private ProxyBufferTimerSegmentTask<String> collectTask;

    /**
     * 收集数据sink
     */
    private FluxSink<String> collectSink;

    /**
     * 端点认证器，包含连接认证与消息认证
     */
    private EndpointAuthenticator endpointAuthenticator;

    // ----------------- WebSocket -----------------

    /**
     * <ul>
     *     <li>验证Token校验</li>
     *     <li>调用{@link #init(Session)}</li>
     *     <li>加入{@link EndpointManager#registry(String, WebSocketEndpoint)}</li>
     * </ul>
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException {
        this.session = session;
        this.endpointAuthenticator = new EndpointAuthenticator(this);
        boolean isPass = endpointAuthenticator.authConnection(new ConnectionContext(config, this)).test(session);
        if (!isPass) {
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "token valid failed"));
        } else {
            log.info("Open websocket connection session id {}", session.getId());
            try {
                // 初始化
                init(session);
                doOnOpen(session);
            } catch (Throwable e) {
                log.error("Websocket init error", e);
                session.close();
            }
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        setHeartbeatsStatus(true);
        log.info("Accept an message：[{}], session id {}", message, session.getId());
        // 心跳
        if (PING.equals(message)) {
            session.getBasicRemote().sendText(PONG);
        } else {
            try {
                Class<R> actualGenericType = (Class<R>) ClassUtils.getSingleActualGenericType(this.getClass());
                List<R> actualMessages = new ArrayList<>();
                // 真实消息类型
                try {
                    // 尝试解析为Object
                    actualMessages.add(JsonUtils.parse(message.getBytes(), actualGenericType));
                } catch (Throwable ex) {
                    // 尝试解析为List
                    actualMessages.addAll(JsonUtils.readList(message.getBytes(), actualGenericType));
                }
                // 通过检测的数据
                List<R> checked = actualMessages.stream()
                        .filter(actualMessage -> {
                            EndpointContext<R> endpointContext = new EndpointContext<>(actualMessage, this);
                            return endpointAuthenticator.authReceiveMessage(endpointContext).test(session);
                        })
                        .collect(Collectors.toList());
                doOnMessage(session, checked);
            } catch (Throwable e) {
                log.error("On Message Failed", e);
            }
        }
    }


    @OnClose
    public void onClose(Session session, CloseReason reason) {
        log.info("Close connection，reason：[{}], session id {}", reason.getReasonPhrase(), session.getId());
        try {
            EndpointManager.unRegistry(getEndpointKey().getKey(), this);
            doOnClose();
        } catch (Throwable e) {
            log.error("On Close Failed", e);
        }
        // 为什么需要额外开启线程，来执行关闭操作。
        // 主要原因是时间轮的stop不允许同一个线程执行
        CompletableFuture.runAsync(() -> {
            // sessionId重置为null
            this.session = null;
            setHeartbeatsStatus(false);
            if (checkHeartBeatsTask != null) {
                checkHeartBeatsTask.finish();
            }
            if (collectTask != null) {
                collectTask.finish();
            }
        });
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.info("On error：[{}], session id {}", throwable.getMessage(), session.getId());
        try {
            doOnError();
        } catch (Throwable e) {
            log.error("On Error Failed", e);
        }
    }

    @Override
    public void init(Session session) throws ParseException {
        initHeartbeat(session);
        initCollector(session);
        EndpointManager.registry(getEndpointKey().getKey(), this);
    }

    /**
     * 心跳初始化
     */
    private void initHeartbeat(Session session) throws ParseException {
        boolean globalHeartbeats = getProperties().isGlobalHeartbeats();
        if (enableHeartbeat() && globalHeartbeats) {
            // 心跳
            String heartbeatsTimeout = getProperties().getHeatbetasTimeout();
            // 检查心跳
            checkHeartBeatsTask = new CronTask<>(heartbeatsTimeout);
            checkHeartBeatsTask.addComputableTask((buffer, current) -> {
                if (getHeartbeatsStatus()) {
                    setHeartbeatsStatus(false);
                } else {
                    try {
                        log.warn("After the heartbeat time, no heartbeat message will close the websocket");
                        // HashedWheelTimer.end()如果是同一个线程不允许关闭
                        // session.close()会回调加上@OnClose注解的方法
                        session.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            checkHeartBeatsTask.run();
        }

    }

    /**
     * 收集器初始化
     */
    private void initCollector(Session session) throws ParseException {
        // 数据收集器初始化
        String collectTime = getProperties().getCollectTime();
        // 收集并发送数据，存储的数据是订阅的主题
        collectTask = new ProxyBufferTimerSegmentTask<>(collectTime, Flux.create(sink -> collectSink = sink));
        collectTask.addComputableTask((buffer, current) -> {
            if (CollectionUtils.isNotEmpty(buffer)) {
                // 发送websocket数据
                combine(buffer).ifPresent(result -> {
                    log.info("Send Session Id: {}, data: {}", session.getId(), result);
                    try {
                        session.getBasicRemote().sendText(result);
                    } catch (Throwable e) {
                        log.error("Collect Session Id: {}, data failed", session.getId(), e);
                    }
                });
            }
        });
        collectTask.run();
    }

    @Override
    public void publish(Object source) {
        try {
            Predicate<Session> sessionPredicate = endpointAuthenticator.authPublishMessage(new EndpointContext<>(source, this));
            boolean testOK = sessionPredicate.test(session);
            if (testOK) {
                doOnPublish(source, collectSink, session);
            }
        } catch (Throwable e) {
            log.error("On Publish Failed", e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        // 会话Id作为唯一标识
        return obj instanceof BaseWebsocketEndpoint &&
                this.session.getId().equals(((BaseWebsocketEndpoint) obj).getSession().getId());
    }

    @Override
    public Session getSession() {
        return this.session;
    }

    /**
     * 设置心跳状态
     *
     * @param status boolean类型
     */
    protected void setHeartbeatsStatus(boolean status) {
        heartbeatsStatus.set(status);
    }

    /**
     * 获取心跳状态
     *
     * @return status boolean类型
     */
    protected boolean getHeartbeatsStatus() {
        return heartbeatsStatus.get();
    }

    // -------------------- 抽象方法 --------------------

    /**
     * 子类实现，当连接打开并且验证通过-初始化完成后调用
     * 注册在端点管理器中:
     * <code>EndpointManager.registry(Endpoint.COMPREHENSIVE, this);</code>
     *
     * @param session Websocket连接信息
     */
    protected abstract void doOnOpen(Session session) throws Throwable;

    /**
     * 子类实现，数据通过WebSocket发送数据
     *
     * @param session Websocket连接信息
     * @param message 消息数据
     */
    protected abstract void doOnMessage(Session session, List<R> message) throws Throwable;

    /**
     * 子类实现，把数据源转换为String类型，调用{@link FluxSink#next(Object)}方法对数据进行收集，根据{@link WebSocketProperties#getCollectTime()}的结构异步发送</br>
     * 除了这种异步发送方案之外还可以直接使用{@link Session#getBasicRemote()}进行同步发送数据
     *
     * @param source      数据源
     * @param collectSink 消息收集发射器
     * @param session     会话
     */
    protected abstract void doOnPublish(Object source, FluxSink<String> collectSink, Session session) throws Throwable;

    /**
     * websocket关闭连接时或者产生异常，释放对时序数据的监听
     * 在端点管理器移除:
     * <code>EndpointManager.registry(Endpoint.COMPREHENSIVE, this);</code>
     */
    protected abstract void doOnClose() throws Throwable;

    /**
     * websocket发生错误时调用
     */
    protected abstract void doOnError() throws Throwable;

}
