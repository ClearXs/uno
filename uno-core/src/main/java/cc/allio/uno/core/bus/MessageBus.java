package cc.allio.uno.core.bus;

import cc.allio.uno.core.bus.event.EventNode;
import cc.allio.uno.core.bus.event.TopicEvent;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 * 消息总线，定义于不同组件之间进行通信，设计是基于Topic-Event，即主题事件。
 *
 * @author jiangwei
 * @date 2021/12/15 15:44
 * @see TopicEvent
 * @see DefaultMessageBus
 */
public interface MessageBus<C> {

    /**
     * 批量订阅消息总线
     *
     * @param subscriptions 订阅集合
     * @return 多源的数据流
     * @see #subscribe(Subscription)
     */
    default Flux<EventNode<C>> subscribe(List<Subscription> subscriptions) {
        return Flux.fromIterable(subscriptions)
                .flatMap(this::subscribe);
    }

    /**
     * 订阅该消息总线，如果没有生成{@link Topic}将会先生成Topic。
     *
     * @param subscription 订阅消息
     * @return 返回指定Topic下新的Node实例
     */
    Flux<EventNode<C>> subscribe(Subscription subscription);

    /**
     * 向消息指定{@link Topic}解除当前{@link EventNode}。<br/>
     * {@link EventNode#doLift(LongConsumer)}}事件
     *
     * @param listenerId 监听id
     * @param topic      消息主题
     */
    void unSubscribe(Long listenerId, String topic);

    /**
     * 从消息总线释放指定的topic
     *
     * @param topic topic唯一标识
     */
    void releaseTopic(String topic);

    /**
     * 同一个上下文，批量发布数据
     *
     * @param subscriptions 订阅信息集合
     * @param context       时序上下文
     * @see #publish(Subscription, C)
     */
    default void publish(List<Subscription> subscriptions, C context) {
        subscriptions.forEach(subscription -> publish(subscription, context));
    }

    /**
     * 向消息总线发布数据，数据将会在当前路径Topic中所有{@link EventNode#doEmmit(Consumer)}被接受
     *
     * @param subscription 订阅信息数据
     * @param context      时序数据上下文
     */
    default void publish(Subscription subscription, C context) {
        publish(subscription.getPath(), context);
    }

    /**
     * 向消息总线发布数据，数据将会在当前路径Topic中所有{@link EventNode#doEmmit(Consumer)}被接受
     *
     * @param path    订阅主题路径
     * @param context 时序数据上下文
     */
    void publish(String path, C context);
}
