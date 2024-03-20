package cc.allio.uno.core.metadata.endpoint.source;

import cc.allio.uno.core.util.JsonUtils;
import com.google.common.collect.Lists;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 默认聚合数据源
 *
 * @author j.x
 * @date 2023/4/27 17:34
 * @since 1.1.4
 */
public class DefaultAggregationSource<T> implements AggregationSource<T, Source<?>> {

    private final List<Source<?>> sources = Lists.newArrayList();

    private final Class<T> entityClass;

    public DefaultAggregationSource(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void registerSource(Source<?> source) {
        sources.add(source);
    }

    @Override
    public void registerSources(List<Source<?>> sources) {
        this.sources.addAll(sources);
    }

    @Override
    public List<Source<?>> getSources() {
        return sources;
    }

    @Override
    public <S extends Source<?>> Optional<S> getSource(Class<S> sourceClass) {
        return (Optional<S>) sources.stream()
                .filter(source -> sourceClass.isAssignableFrom(source.getClass()))
                .findFirst();
    }

    @Override
    public void subscribe(Consumer<T> next) {
        sources.forEach(s ->
                s.subscribe(o -> {
                    String json = JsonUtils.toJson(o);
                    next.accept(JsonUtils.parse(json, entityClass));
                }));
    }

    @Override
    public void register(ApplicationContext context) {
        sources.forEach(s -> s.register(context));
    }
}
