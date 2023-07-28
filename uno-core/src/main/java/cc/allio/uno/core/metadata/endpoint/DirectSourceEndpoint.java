package cc.allio.uno.core.metadata.endpoint;

import cc.allio.uno.core.metadata.endpoint.source.JsonSource;
import cc.allio.uno.core.metadata.endpoint.source.Source;
import cc.allio.uno.core.metadata.endpoint.source.SourceCollector;
import cc.allio.uno.core.metadata.endpoint.source.SourceConverter;
import cc.allio.uno.core.metadata.Metadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * {@link Source}数据源包装类。直接进行处理获取
 *
 * @author jiangwei
 * @date 2022/9/27 15:55
 * @since 1.1.0
 */
@Slf4j
public class DirectSourceEndpoint<T extends Metadata> implements SourceEndpoint<T> {

    private final JsonSource source;
    private final SourceCollector<T> collector;
    private final SourceConverter<T> converter;
    private final ApplicationContext context;

    public DirectSourceEndpoint(
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
    @Override
    public void register() {
        source.register(context);
    }

    @Override
    public void subscribe() {
        source.subscribe(json -> {
            try {
                T metadata = null;
                if (converter != null) {
                    metadata = converter.execute(context, json);
                }
                if (metadata != null && collector != null) {
                    collector.collect(metadata);
                }
            } catch (Throwable ex) {
                log.error("Source Endpoint subscribe data failed", ex);
            }
        });
    }
}
