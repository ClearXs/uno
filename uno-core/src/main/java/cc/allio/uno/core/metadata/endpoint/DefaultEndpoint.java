package cc.allio.uno.core.metadata.endpoint;

import cc.allio.uno.core.metadata.endpoint.source.JsonSource;
import cc.allio.uno.core.metadata.endpoint.source.Source;
import cc.allio.uno.core.metadata.endpoint.source.SourceCollector;
import cc.allio.uno.core.metadata.endpoint.source.SourceConverter;
import cc.allio.uno.core.metadata.Metadata;
import com.google.common.collect.Lists;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 默认实现端点
 *
 * @author jiangwei
 * @date 2022/9/27 17:46
 * @since 1.1.0
 */
public class DefaultEndpoint<T extends Metadata> implements Endpoint<T> {

    private ApplicationContext applicationContext;
    private SourceConverter<T> converter;
    private SourceCollector<T> collector;
    private final List<JsonSource> sources = Lists.newArrayList();

    @Override
    public void registerSource(JsonSource source) {
        this.sources.add(source);
    }

    @Override
    public void registerSources(List<JsonSource> sources) {
        this.sources.addAll(Lists.newArrayList(sources));
    }

    @Override
    public List<JsonSource> getSources() {
        return sources;
    }

    @Override
    public <S extends Source<?>> Optional<S> getSource(Class<S> sourceClass) {
        return (Optional<S>) sources.stream()
                .filter(source -> sourceClass.isAssignableFrom(source.getClass()))
                .findFirst();

    }

    @Override
    public void setCollector(SourceCollector<T> collector) {
        this.collector = collector;
    }

    @Override
    public SourceCollector<T> getCollector() {
        return collector;
    }

    @Override
    public void setConverter(SourceConverter<T> converter) {
        this.converter = converter;
    }

    @Override
    public SourceConverter<T> getConverter() {
        return converter;
    }

    @Override
    public void open() throws Exception {
        List<SourceEndpoint<T>> wrappers = sources.stream()
                .map(source -> new DirectSourceEndpoint<>(source, collector, converter, applicationContext))
                .collect(Collectors.toList());
        wrappers.forEach(SourceEndpoint::subscribe);
        registration(wrappers);
    }

    void registration(List<SourceEndpoint<T>> sources) {
        sources.forEach(SourceEndpoint::register);
    }

    @Override
    public void close() throws Exception {
        // TODO NOTHING
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 子类获取ApplicationContext实例
     */
    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
