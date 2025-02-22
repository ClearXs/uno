package cc.allio.uno.core.bus;

import cc.allio.uno.core.bus.event.Event;
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
 * @see Event
 * @see DefaultEventBus
 */
public interface EventBus<C extends EventContext> {

    /**
     * @see #hasTopic(Subscription)
     */
    default boolean hasTopic(String path) {
        return hasTopic(TopicKey.of(path));
    }

    /**
     * 根据指定的订阅信息判断是否存在订阅的主题
     *
     * @param subscription subscription
     * @return true if existing, false otherwise
     * @see #findTopic(TopicKey)
     */
    default boolean hasTopic(Subscription subscription) {
        return hasTopic(subscription.getPath());
    }

    /**
     * @see #hasTopic(Subscription)
     */
    default boolean hasTopic(TopicKey topicKey) {
        Long topicCount = findTopic(topicKey).count().block();
        return topicCount != null && topicCount != 0;
    }

    /**
     * 根据主题路径获取指定的主题实例
     *
     * @param path 主题路径
     * @return Topic stream or empty stream
     */
    default Flux<Topic<C>> findTopic(String path) {
        return findTopic(TopicKey.of(path));
    }

    /**
     * 根据主题路径获取指定的主题实例
     *
     * @param subscription subscription
     * @return Topic stream or empty stream
     */
    default Flux<Topic<C>> findTopic(Subscription subscription) {
        return findTopic(subscription.getTopicKey());
    }

    /**
     * 根据主题路径获取指定的主题实例
     *
     * @param topicKey the topic key
     * @return Topic stream or empty stream
     */
    Flux<Topic<C>> findTopic(TopicKey topicKey);

    /**
     * 批量订阅事件总线
     *
     * @param subscriptions 订阅集合
     * @return 多源的数据流
     * @see #subscribe(Subscription)
     */
    default Flux<C> subscribe(List<Subscription> subscriptions) {
        return Flux.fromIterable(subscriptions).flatMap(this::subscribe);
    }

    /**
     * @see #subscribe(String)
     */
    default Flux<C> subscribe(TopicKey topicKey) {
        return subscribe(Subscription.of(topicKey));
    }

    /**
     * 订阅该事件总线，如果没有生成{@link Topic}将会先生成Topic。
     *
     * @param path 主题（可以是通配符/test/*，订阅test下所有主题）
     * @return 返回指定Topic下新的Node实例
     */
    default Flux<C> subscribe(String path) {
        return subscribe(Subscription.of(path));
    }

    /**
     * 订阅该事件总线，如果没有生成{@link Topic}将会先生成Topic。
     *
     * @param topicEvent 主题事件（可以是通配符/test/*，订阅test下所有主题）
     * @return 返回指定Topic下新的Node实例
     */
    default Flux<C> subscribe(TopicEvent topicEvent) {
        return subscribe(topicEvent.getTopicKey());
    }

    /**
     * 订阅该事件总线，如果没有生成{@link Topic}将会先生成Topic。
     *
     * @param subscription 订阅消息
     * @return 返回指定Topic下新的Node实例
     * @see #subscribeOnRepeatable(Subscription)
     */
    Flux<C> subscribe(Subscription subscription);

    /**
     * 订阅该事件总线，如果没有生成{@link Topic}将会先生成Topic。
     * <b>支持重复订阅</b>
     *
     * @param subscriptions 订阅集合
     * @return node stream
     */
    default Flux<C> subscribeOnRepeatable(List<Subscription> subscriptions) {
        return Flux.fromIterable(subscriptions).flatMap(this::subscribeOnRepeatable);
    }

    /**
     * 订阅该事件总线，如果没有生成{@link Topic}将会先生成Topic。
     * <b>支持重复订阅</b>
     *
     * @param path 主题路径
     * @return node stream
     */
    default Flux<C> subscribeOnRepeatable(String path) {
        return subscribeOnRepeatable(Subscription.of(path));
    }

    /**
     * 订阅该事件总线，如果没有生成{@link Topic}将会先生成Topic。
     * <b>支持重复订阅</b>
     *
     * @param topicEvent 主题事件
     * @return node stream
     */
    default Flux<C> subscribeOnRepeatable(TopicEvent topicEvent) {
        return subscribeOnRepeatable(topicEvent.getTopicKey());
    }

    /**
     * 订阅该事件总线，如果没有生成{@link Topic}将会先生成Topic。
     * <b>支持重复订阅</b>
     *
     * @param topicKey 主题键
     * @return node stream
     */
    default Flux<C> subscribeOnRepeatable(TopicKey topicKey) {
        return subscribeOnRepeatable(Subscription.of(topicKey));
    }

    /**
     * 订阅该事件总线，如果没有生成{@link Topic}将会先生成Topic。
     * <b>支持重复订阅（对一个Topic不会在生成新的订阅节点）</b>
     *
     * @param subscription 订阅消息
     * @return node stream
     */
    Flux<C> subscribeOnRepeatable(Subscription subscription);

    /**
     * 向消息指定{@link Topic}解除当前{@link Node}。<br/>
     * {@link Node#doLift(LongConsumer)}}事件
     *
     * @param subscription the subscription.
     */
    default Flux<Void> unSubscribe(Subscription subscription) {
        return unSubscribe(subscription.getId(), subscription.getTopicKey());
    }

    /**
     * 向消息指定{@link Topic}解除当前{@link Node}。<br/>
     * {@link Node#doLift(LongConsumer)}}事件
     *
     * @param subscribeId 监听id
     * @param topicKey    消息主题
     */
    Flux<Void> unSubscribe(Long subscribeId, TopicKey topicKey);


    /**
     * 从事件总线释放指定的topic
     *
     * @param path the path
     */
    default void release(String path) {
        release(TopicKey.of(path));
    }

    /**
     * 从事件总线释放指定的topic
     *
     * @param topicKey topic唯一标识
     */
    void release(TopicKey topicKey);

    /**
     * 同一个上下文，批量发布数据
     *
     * @param subscriptions 订阅信息集合
     * @param context       时序上下文
     * @see #publish(Subscription, C)
     */
    default Flux<Topic<C>> batchPublish(List<TopicKey> topicKeys, C context) {
        return Flux.fromIterable(topicKeys)
                .flatMap(topicKey -> publish(topicKey, context));
    }

    /**
     * 向事件总线发布数据，数据将会在当前路径Topic中所有{@link Node#doEmmit(Consumer)}被接受
     *
     * @param subscription 订阅信息数据
     * @param context      主题上下文
     */
    default Flux<Topic<C>> publish(Subscription subscription, C context) {
        return publish(subscription.getPath(), context);
    }

    /**
     * 向事件总线发布数据，数据将会在当前路径Topic中所有{@link Node#doEmmit(Consumer)}被接受
     *
     * @param topicEvent topicEvent
     */
    default Flux<Topic<C>> publish(TopicEvent topicEvent) {
        topicEvent.getEventContext().getEventTracer().push(topicEvent);
        return publish(topicEvent.getTopicKey().getPath(), (C) topicEvent.getEventContext());
    }

    /**
     * 向事件总线发布数据，数据将会在当前路径Topic中所有{@link Node#doEmmit(Consumer)}被接受
     *
     * @param path    订阅主题路径
     * @param context 主题上下文
     */
    default Flux<Topic<C>> publish(String path, C context) {
        return publishOnFlux(path, context);
    }

    /**
     * 向事件总线发布数据，数据将会在当前路径Topic中所有{@link Node#doEmmit(Consumer)}被接受
     *
     * @param topicKey the topic key
     * @param context  主题上下文
     */
    default Flux<Topic<C>> publish(TopicKey topicKey, C context) {
        return publishOnFlux(topicKey.getPath(), context);
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
    default Flux<Topic<C>> publishOnFlux(String path, C context) {
        return publishOnFlux(TopicKey.of(path), context);
    }

    /**
     * 向事件总线发布数据，数据将会在当前路径Topic中所有{@link Node#doEmmit(Consumer)}被接受
     *
     * @param topicKey 订阅主题路径
     * @param context  主题上下文
     * @return flux
     */
    Flux<Topic<C>> publishOnFlux(TopicKey topicKey, C context);

    /**
     * 先进行订阅之后发布数据
     *
     * @param topicEvent topicEvent
     * @return Topic - flux
     */
    default Flux<Topic<C>> subThenPub(TopicEvent topicEvent) {
        return subscribeOnRepeatable(topicEvent).thenMany(publishOnFlux(topicEvent));
    }

    /**
     * 判断指定的主题是否存在事件总线中
     *
     * @param path the topic path
     * @return true 存在 false 不存在
     */
    default Mono<Boolean> contains(String path) {
        return contains(TopicKey.of(path));
    }

    /**
     * 判断指定的主题是否存在事件总线中
     *
     * @param topicKey the topic key
     * @return true 存在 false 不存在
     */
    Mono<Boolean> contains(TopicKey topicKey);
}
