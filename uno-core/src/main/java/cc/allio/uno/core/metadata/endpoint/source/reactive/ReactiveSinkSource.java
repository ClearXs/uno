package cc.allio.uno.core.metadata.endpoint.source.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * reactive sink
 *
 * @author jiangwei
 * @date 2023/4/27 17:50
 * @since 1.1.4
 */
public abstract class ReactiveSinkSource<C> implements ReactiveSource<C> {

    private FluxSink<C> sink;
    private final Flux<C> source;

    protected ReactiveSinkSource() {
        this.source = Flux.create(sink -> this.sink = sink);
    }

    @Override
    public Flux<C> subscribe() {
        return source;
    }

    /**
     * 子类调用
     */
    public void next(C c) {
        if (sink != null) {
            sink.next(c);
        }
    }
}
