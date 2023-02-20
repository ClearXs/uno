package cc.allio.uno.core.bus;

import cc.allio.uno.core.bus.event.EventNode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * 基础MessageBus，使用{@link Topic}作为消息传递。实现{@link #subscribe(Subscription)}、{@link #unSubscribe(Long, String)}，{@link #publish(String, MessageContext)}方法
 *
 * @author jiangwei
 * @date 2022/12/12 16:03
 * @since 1.1.2
 */
@Slf4j
public abstract class BaseMessageBus<C extends MessageContext> implements MessageBus<C> {

    protected final Topics<C> topics = new Topics<>();

    @Override
    public Flux<EventNode<C>> subscribe(Subscription subscription) {
        return topics.lookup(subscription.getPath())
                .switchIfEmpty(
                        topics.link(subscription)
                                .onErrorContinue((error, o) -> log.error("topic link on error", error)))
                .flatMap(Topic::findNode);
    }

    @Override
    public void unSubscribe(Long listenerId, @NonNull String topic) {
        topics.lookup(topic)
                .doOnNext(busTopic -> busTopic.discard(listenerId))
                .subscribe();
    }

    @Override
    public void releaseTopic(String topic) {
        topics.unlink(topic).block();
    }

    @Override
    public void publish(String path, C context) {
        topics.lookup(path)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(topic -> topic.emmit(context))
                .onErrorContinue((error, topic) -> log.error("bus publish error", error))
                .subscribe();
    }
}
