package cc.allio.uno.core.bus;

import cc.allio.uno.core.bus.event.BusEvent;
import cc.allio.uno.core.bus.event.Node;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 * 事件总线，定义于不同组件之间进行通信，设计是基于Topic-Event，即主题事件。
 *
 * @author j.x
 * @date 2021/12/15 15:44
 * @see BusEvent
 * @see DefaultEventBus
 */
public interface EventBus<C extends EventContext> {

    /**
     * @see #hasTopic(Subscription)
     */
    default boolean hasTopic(TopicKey topicKey) {
        return hasTopic(topicKey.getSubscription());
    }

    /**
     * @see #hasTopic(Subscription)
     */
    default boolean hasTopic(String path) {
        return hasTopic(Subscription.of(path));
    }

    /**
     * 根据指定的订阅信息判断是否存在订阅的主题
     *
     * @param subscription subscription
     * @return true if exist, false otherwise
     * @see #findTopic(Subscription)
     */
    default boolean hasTopic(Subscription subscription) {
        Long topicCount = findTopic(subscription).count().block();
        return topicCount != null && topicCount != 0;
    }

    /**
     * 根据主题路径获取指定的主题实例
     *
     * @param topicKey topicKey
     * @return Topic stream or empty stream
     */
    default Flux<Topic<C>> findTopic(TopicKey topicKey) {
        return findTopic(topicKey.getSubscription());
    }

    /**
     * 根据主题路径获取指定的主题实例
     *
     * @param path 主题路径
     * @return Topic stream or empty stream
     */
    default Flux<Topic<C>> findTopic(String path) {
        return findTopic(Subscription.of(path));
    }

    /**
     * 根据主题路径获取指定的主题实例
     *
     * @param subscription 订阅实例
     * @return Topic stream or empty stream
     */
    Flux<Topic<C>> findTopic(Subscription subscription);

    /**
     * 批量订阅事件总线
     *
     * @param subscriptions 订阅集合
     * @return 多源的数据流
     * @see #subscribe(Subscription)
     */
    default Flux<Node<C>> subscribe(List<Subscription> subscriptions) {
        return Flux.fromIterable(subscriptions).flatMap(this::subscribe);
    }

    /**
     * 订阅该事件总线，如果没有生成{@link Topic}将会先生成Topic。
     *
     * @param path 主题（可以是通配符/test/*，订阅test下所有主题）
     * @return 返回指定Topic下新的Node实例
     */
    default Flux<Node<C>> subscribe(String path) {
        return subscribe(Subscription.of(path));
    }

    /**
     * 订阅该事件总线，如果没有生成{@link Topic}将会先生成Topic。
     *
     * @param topicEvent 主题事件（可以是通配符/test/*，订阅test下所有主题）
     * @return 返回指定Topic下新的Node实例
     */
    default Flux<Node<C>> subscribe(TopicEvent topicEvent) {
        return subscribe(topicEvent.getTopicKey().getSubscription());
    }

    /**
     * 订阅该事件总线，如果没有生成{@link Topic}将会先生成Topic。
     *
     * @param subscription 订阅消息
     * @return 返回指定Topic下新的Node实例
     * @see #subscribeOnRepeatable(Subscription)
     */
    Flux<Node<C>> subscribe(Subscription subscription);

    /**
     * 订阅该事件总线，如果没有生成{@link Topic}将会先生成Topic。
     * <b>支持重复订阅</b>
     *
     * @param subscriptions 订阅集合
     * @return node stream
     */
    default Flux<Node<C>> subscribeOnRepeatable(List<Subscription> subscriptions) {
        return Flux.fromIterable(subscriptions).flatMap(this::subscribeOnRepeatable);
    }

    /**
     * 订阅该事件总线，如果没有生成{@link Topic}将会先生成Topic。
     * <b>支持重复订阅</b>
     *
     * @param path 主题路径
     * @return node stream
     */
    default Flux<Node<C>> subscribeOnRepeatable(String path) {
        return subscribeOnRepeatable(Subscription.of(path));
    }

    /**
     * 订阅该事件总线，如果没有生成{@link Topic}将会先生成Topic。
     * <b>支持重复订阅</b>
     *
     * @param topicEvent 主题事件
     * @return node stream
     */
    default Flux<Node<C>> subscribeOnRepeatable(TopicEvent topicEvent) {
        return subscribeOnRepeatable(topicEvent.getTopicKey());
    }

    /**
     * 订阅该事件总线，如果没有生成{@link Topic}将会先生成Topic。
     * <b>支持重复订阅</b>
     *
     * @param topicKey 主题键
     * @return node stream
     */
    default Flux<Node<C>> subscribeOnRepeatable(TopicKey topicKey) {
        return subscribeOnRepeatable(topicKey.getSubscription());
    }

    /**
     * 订阅该事件总线，如果没有生成{@link Topic}将会先生成Topic。
     * <b>支持重复订阅（对一个Topic不会在生成新的订阅节点）</b>
     *
     * @param subscription 订阅消息
     * @return node stream
     */
    Flux<Node<C>> subscribeOnRepeatable(Subscription subscription);

    /**
     * 向消息指定{@link Topic}解除当前{@link Node}。<br/>
     * {@link Node#doLift(LongConsumer)}}事件
     *
     * @param listenerId 监听id
     * @param topicKey   topicKey
     */
    default void unSubscribe(Long listenerId, TopicKey topicKey) {
        unSubscribe(listenerId, topicKey.getPath());
    }

    /**
     * 向消息指定{@link Topic}解除当前{@link Node}。<br/>
     * {@link Node#doLift(LongConsumer)}}事件
     *
     * @param listenerId 监听id
     * @param topic      消息主题
     */
    void unSubscribe(Long listenerId, String topic);

    /**
     * 从事件总线释放指定的topic
     *
     * @param topicKey topic唯一标识
     */
    default void releaseTopic(TopicKey topicKey) {
        releaseTopic(topicKey.getPath());
    }

    /**
     * 从事件总线释放指定的topic
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
     * 向事件总线发布数据，数据将会在当前路径Topic中所有{@link Node#doEmmit(Consumer)}被接受
     *
     * @param subscription 订阅信息数据
     * @param context      主题上下文
     */
    default void publish(Subscription subscription, C context) {
        publish(subscription.getPath(), context);
    }

    /**
     * 向事件总线发布数据，数据将会在当前路径Topic中所有{@link Node#doEmmit(Consumer)}被接受
     *
     * @param topicEvent topicEvent
     */
    default void publish(TopicEvent topicEvent) {
        topicEvent.getEventContext().getEventTracer().push(topicEvent);
        publish(topicEvent.getTopicKey().getPath(), (C) topicEvent.getEventContext());
    }

    /**
     * 向事件总线发布数据，数据将会在当前路径Topic中所有{@link Node#doEmmit(Consumer)}被接受
     *
     * @param path    订阅主题路径
     * @param context 主题上下文
     */
    default void publish(String path, C context) {
        publishOnFlux(path, context).subscribe();
    }

    /**
     * 向事件总线发布数据，数据将会在当前路径Topic中所有{@link Node#doEmmit(Consumer)}被接受
     *
     * @param topicEvent topicEvent
     */
    default Flux<Topic<C>> publishOnFlux(TopicEvent topicEvent) {
        topicEvent.getEventContext().getEventTracer().push(topicEvent);
        return publishOnFlux(topicEvent.getTopicKey().getPath(), (C) topicEvent.getEventContext());
    }

    /**
     * 批量向事件总线发布数据，数据将会在当前路径Topic中所有{@link Node#doEmmit(Consumer)}被接受
     *
     * @param topicEvents topicEvents
     * @return Topic - flux
     */
    default Flux<Topic<C>> publishOnFluxForMulti(List<TopicEvent> topicEvents) {
        return Flux.fromIterable(topicEvents)
                .flatMap(this::publishOnFlux)
                .distinct();
    }

    /**
     * 向事件总线发布数据，数据将会在当前路径Topic中所有{@link Node#doEmmit(Consumer)}被接受
     *
     * @param path    订阅主题路径
     * @param context 主题上下文
     * @return flux
     */
    Flux<Topic<C>> publishOnFlux(String path, C context);

    /**
     * 先进行订阅之后发布数据
     *
     * @param topicEvent topicEvent
     * @return Topic - flux
     */
    default Flux<Topic<C>> subThenPub(TopicEvent topicEvent) {
        subscribeOnRepeatable(topicEvent).subscribe();
        return publishOnFlux(topicEvent);
    }

    /**
     * 判断指定的主题是否存在事件总线中
     *
     * @param topic the topic
     * @return true 存在 false 不存在
     */
    Mono<Boolean> contains(String topic);
}
