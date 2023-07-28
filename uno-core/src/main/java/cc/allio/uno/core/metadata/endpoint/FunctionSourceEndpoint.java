package cc.allio.uno.core.metadata.endpoint;

import cc.allio.uno.core.metadata.endpoint.source.JsonSource;
import cc.allio.uno.core.metadata.Metadata;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.context.ApplicationContext;

import java.util.function.Consumer;

/**
 * 数据源函数接收
 *
 * @author jiangwei
 * @date 2023/4/11 12:34
 * @since 1.1.4
 */
public class FunctionSourceEndpoint<T extends Metadata> implements SourceEndpoint<T> {

    ApplicationContext applicationContext;
    JsonSource source;
    Consumer<JsonNode> receiver;

    public FunctionSourceEndpoint(ApplicationContext applicationContext, JsonSource source, Consumer<JsonNode> receiver) {
        this.applicationContext = applicationContext;
        this.source = source;
        this.receiver = receiver;
    }

    @Override
    public void register() {
        source.register(applicationContext);
    }

    @Override
    public void subscribe() {
        source.subscribe(receiver);
    }
}
