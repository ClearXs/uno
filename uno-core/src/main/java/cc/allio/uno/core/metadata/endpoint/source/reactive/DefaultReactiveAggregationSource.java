package cc.allio.uno.core.metadata.endpoint.source.reactive;

import cc.allio.uno.core.util.JsonUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

/**
 * aggregation default impl
 *
 * @author jiangwei
 * @date 2023/4/27 18:00
 * @since 1.1.4
 */
@Slf4j
public class DefaultReactiveAggregationSource<T> extends ReactiveSinkSource<T> implements ReactiveAggregationSource<T, ReactiveSource<?>> {

    private final List<ReactiveSource<?>> sources = Lists.newArrayList();

    private final Class<T> entityClass;

    public DefaultReactiveAggregationSource(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void registerSource(ReactiveSource<?> source) {
        this.sources.add(source);
    }

    @Override
    public void registerSources(List<ReactiveSource<?>> sources) {
        this.sources.addAll(sources);
    }

    @Override
    public List<ReactiveSource<?>> getSources() {
        return sources;
    }

    @Override
    public <S extends ReactiveSource<?>> Optional<S> getSource(Class<S> sourceClass) {
        return (Optional<S>) sources.stream()
                .filter(source -> sourceClass.isAssignableFrom(source.getClass()))
                .findFirst();
    }

    @Override
    public Flux<T> subscribe() {
        Flux<T> upstream = Flux.fromIterable(sources)
                .flatMap(ReactiveSource::subscribe)
                .map(o -> {
                    if (String.class.isAssignableFrom(o.getClass())) {
                        return JsonUtils.parse((String) o, entityClass);
                    } else {
                        return JsonUtils.parse(JsonUtils.toJson(o), entityClass);
                    }
                })
                .onErrorContinue((err, o) -> log.error("source {} subscribe err ", o, err));
        return super.subscribe().mergeWith(upstream);

    }

    @Override
    public void register(ApplicationContext context) {
        sources.forEach(s -> s.register(context));
    }
}
