package cc.allio.uno.web.source.reactive;

import cc.allio.uno.core.metadata.endpoint.source.reactive.ReactiveSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * 基于http的 reactive source
 *
 * @author j.x
 * @date 2023/5/15 12:25
 * @since 1.1.4
 */
@Slf4j
public class WebHttpReactiveSource extends BaseReactiveSource implements ReactiveSource<String> {

    private FluxSink<String> theSink;

    public WebHttpReactiveSource(String requestMappingUrl) {
        super(requestMappingUrl);
    }

    @Override
    public Flux<String> subscribe() {
        return Flux.push(sink -> theSink = sink);
    }

    @Override
    protected void next(String nextJson) {
        if (theSink != null) {
            theSink.next(nextJson);
        }
    }

    @Override
    public void register(ApplicationContext context) {
        registryEndpoint(context, getEndpointMethod(), parser);
    }
}
