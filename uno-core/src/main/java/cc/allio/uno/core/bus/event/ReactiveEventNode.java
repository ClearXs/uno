package cc.allio.uno.core.bus.event;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 基于 reactive 事件处理
 *
 * @author j.x
 * @date 2023/5/19 13:24
 * @since 1.1.4
 */
@Slf4j
public class ReactiveEventNode<C> extends EventNode<C> {

    public ReactiveEventNode(Long subscribeId, String topic) {
        super(subscribeId, topic);
    }

    @Override
    public Mono<C> update(Listener<C>[] listeners, Context<C> eventContext) {
        return Flux.fromArray(listeners)
                .doOnNext(l -> l.listen(this, eventContext.getSource()))
                .onErrorContinue((err, l) -> log.error("Listener Callback error, Belong Topic: {}", getTopic(), err))
                .then(Mono.just(eventContext.getSource()));
    }

}
