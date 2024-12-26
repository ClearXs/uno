package cc.allio.uno.core.metadata.endpoint.source;

import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * 基于{@link reactor.core.publisher.FluxSink}的数据源
 *
 * @author j.x
 * @since 1.1.2
 */
public class SinkSource extends JsonSource {
    FluxSink<String> sink;

    @Override
    public void register(ApplicationContext context) {
        Flux.<String>create(emmit -> sink = emmit)
                .doOnNext(this::next)
                .subscribe();
    }

    public void publish(String json) {
        sink.next(json);
    }
}
