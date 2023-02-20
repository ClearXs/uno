package cc.allio.uno.core.bus.event;

import cc.allio.uno.core.bus.Topic;

import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 * 定义为消息总线上订阅的节点信息
 *
 * @author jw
 * @date 2021/12/16 19:56
 */
public interface EventNode<C> {

    /**
     * 获取当前订阅id
     *
     * @return 订阅id
     */
    Long getSubscribeId();

    /**
     * 获取当前关联{@link Topic}
     *
     * @return {@link Topic}唯一字符串
     */
    String getTopic();

    /**
     * 当上游数据发射时产生事件
     *
     * @param onNext 处理的Function对象
     * @return 监听id
     * @see #reply(Class, Consumer)
     * @deprecated 使用@{@link #reply(Class, Consumer)}
     */
    @Deprecated
    Long doEmmit(Consumer<C> onNext);

    /**
     * 当{@link Topic#discard(Long)}时，node触发这个事件
     *
     * @param consumer 解除时触发的回调
     * @return 监听id
     * @see #reply(Class, Consumer)
     * @deprecated 使用@{@link #reply(Class, Consumer)}
     */
    @Deprecated
    Long doLift(LongConsumer consumer);

    /**
     * 监听指定目标事件
     *
     * @param eventType 事件类型，<b>非空</b>
     * @param consumer  触发事件回调，<b>非空</b>，当触发异常时，回调参数默认为空
     * @return 监听回调Id
     * @see EmitEvent
     * @see LiftEvent
     */
    Long reply(Class<? extends TopicEvent> eventType, Consumer<C> consumer);

    /**
     * 检索该节点上属于指定事件监听器数量
     *
     * @param eventType 事件类型
     * @return 监听器数组集合
     */
    Listener<C>[] retrieval(Class<? extends TopicEvent> eventType);

    /**
     * 释放监听
     *
     * @param listenerId 监听id{@link #doEmmit(Consumer)}或{@link #doLift(LongConsumer)}返回
     */
    void release(Long listenerId);

    /**
     * 更新在当前{@link EventNode}上进行监听的状态
     *
     * @param listeners    监听者集合
     * @param eventContext 事件上下文
     */
    void update(Listener<C>[] listeners, EventContext<C> eventContext);

    /**
     * 更新在当前{@link EventNode}上进行监听的状态
     *
     * @param eventContext 事件上下文参数
     */
    void update(EventContext<C> eventContext);

}
