package cc.allio.uno.core.metadata.endpoint;

import cc.allio.uno.core.metadata.endpoint.source.JsonSource;
import cc.allio.uno.core.metadata.Metadata;
import cc.allio.uno.core.metadata.endpoint.source.Source;
import cc.allio.uno.core.metadata.endpoint.source.SourceCollector;
import cc.allio.uno.core.metadata.endpoint.source.SourceConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * {@link Source}数据源包装类
 *
 * @author jiangwei
 * @date 2022/9/27 15:55
 * @since 1.1.0
 */
@Slf4j
class SourceEndpoint<T extends Metadata> {

    private final JsonSource source;

    private final SourceCollector<T> collector;

    private final SourceConverter<T> converter;

    private final ApplicationContext context;

    SourceEndpoint(
            JsonSource source,
            SourceCollector<T> collector,
            SourceConverter<T> converter,
            ApplicationContext context) {
        this.source = source;
        this.collector = collector;
        this.converter = converter;
        this.context = context;
    }

    /**
     * 注册数据源
     */
    void register() {
        source.register(context);
    }

    void subscribe() {
        source.subscribe(json -> {
            try {
                synchronized (source) {
                    T metadata = converter.execute(context, json);
                    collector.collect(metadata);
                }
            } catch (Throwable ex) {
                log.error("Source Endpoint subscribe data failed", ex);
            }
        });
    }
}
