package cc.allio.uno.core.bus;

import cc.allio.uno.core.bus.event.Node;
import cc.allio.uno.core.util.CollectionUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 基础EventBus，使用{@link Topic}作为消息传递。实现{@link #subscribe(Subscription)}、{@link #unSubscribe(Long, String)}
 *
 * @author jiangwei
 * @date 2022/12/12 16:03
 * @since 1.1.2
 */
@Slf4j
public abstract class BaseEventBus<C extends EventContext> implements EventBus<C> {

    // 主题管理器
    protected final Topics<C> topics = new Topics<>();

    @Override
    public Flux<Topic<C>> findTopic(Subscription subscription) {
        return topics.lookup(subscription.getPath());
    }

    @Override
    public Flux<Node<C>> subscribe(Subscription subscription) {
        return topics.lookup(subscription.getPath())
                .switchIfEmpty(Mono.defer(() -> topics.link(subscription)))
                .flatMap(t -> t.addSubscriber(subscription));
    }

    @Override
    public Flux<Node<C>> subscribeOnRepeatable(Subscription subscription) {
        return topics.lookup(subscription.getPath())
                .flatMap(Topic::findNode)
                .switchIfEmpty(Mono.defer(() -> topics.link(subscription).flatMap(t -> t.addSubscriber(subscription))));
    }

    @Override
    public void unSubscribe(Long listenerId, @NonNull String topic) {
        topics.lookup(topic)
                .doOnNext(busTopic -> busTopic.discard(listenerId))
                .subscribe();
    }

    @Override
    public void releaseTopic(String topic) {
        topics.unlink(topic).subscribe();
    }

    @Override
    public Flux<Topic<C>> publishOnFlux(String path, C context) {
        return Flux.defer(() ->
                        topics.lookup(path)
                                .publishOn(Schedulers.boundedElastic())
                                .doOnNext(topic -> {
                                    // 存入 上下文数据
                                    context.putAttribute(EventContext.TOPIC_PATH_KEY, path);
                                    context.putAttribute(EventContext.TOPIC_KEY, topic);
                                    topic.emmit(context);
                                })
                )
                .onErrorContinue((error, topic) -> log.error("bus publish error", error));
    }

    @Override
    public Mono<Boolean> contains(String topic) {
        return topics.lookup(topic)
                .collectList()
                .flatMap(t -> Mono.just(CollectionUtils.isNotEmpty(t)))
                .switchIfEmpty(Mono.defer(() -> Mono.just(Boolean.FALSE)));
    }
}
