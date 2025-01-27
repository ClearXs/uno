package cc.allio.uno.core.bus;

import cc.allio.uno.core.bus.event.Node;
import cc.allio.uno.core.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

/**
 * 基础EventBus，使用{@link Topic}作为消息传递。实现{@link #subscribe(Subscription)}、{@link EventBus#unSubscribe(Long, TopicKey)}
 *
 * @author j.x
 * @since 1.1.2
 */
@Slf4j
public abstract class BaseEventBus<C extends EventContext> implements EventBus<C> {

    // 主题管理器
    protected final Topics<C> topics = new Topics<>();

    @Override
    public Flux<Topic<C>> findTopic(TopicKey topicKey) {
        return topics.lookup(topicKey);
    }

    @Override
    public Flux<C> subscribe(Subscription subscription) {
        return topics.lookup(subscription.getTopicKey())
                .switchIfEmpty(Mono.defer(() -> topics.link(subscription.getTopicKey(), this)))
                .flatMap(t -> t.addSubscriber(subscription))
                .flatMap(Node::onNext);
    }

    @Override
    public Flux<C> subscribeOnRepeatable(Subscription subscription) {
        return topics.lookup(subscription.getTopicKey())
                .flatMap(Topic::getNode)
                .switchIfEmpty(
                        Mono.defer(() ->
                                topics.link(subscription.getTopicKey(), this).flatMap(t -> t.addSubscriber(subscription))))
                .flatMap(Node::onNext);
    }

    @Override
    public Flux<Void> unSubscribe(Long subscribeId, TopicKey topicKey) {
        return topics.lookup(topicKey)
                .flatMap(busTopic -> busTopic.discard(subscribeId));
    }

    @Override
    public void release(TopicKey topicKey) {
        topics.unlink(topicKey).subscribe();
    }

    @Override
    public Flux<Topic<C>> publishOnFlux(TopicKey topicKey, C context) {
        return Flux.defer(() ->
                        topics.lookup(topicKey)
                                .publishOn(Schedulers.fromExecutor(Executors.newVirtualThreadPerTaskExecutor()))
                                .doOnNext(topic -> {
                                    if (log.isDebugEnabled()) {
                                        log.debug("Topic path {} is finder, now publish", topicKey);
                                    }
                                    // 存入 上下文数据
                                    context.put(EventContext.TOPIC_PATH_KEY, topicKey);
                                    context.put(EventContext.TOPIC_KEY, topic);
                                    topic.emmit(context);
                                })
                )
                .onErrorContinue((error, topic) -> log.error("bus publish error", error));
    }

    @Override
    public Mono<Boolean> contains(TopicKey topicKey) {
        return topics.lookup(topicKey)
                .collectList()
                .flatMap(t -> Mono.just(CollectionUtils.isNotEmpty(t)))
                .switchIfEmpty(Mono.defer(() -> Mono.just(Boolean.FALSE)));
    }
}
