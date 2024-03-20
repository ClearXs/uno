package cc.allio.uno.core.metadata.endpoint.source;

import cc.allio.uno.core.serializer.JsonNodeEnhancer;
import cc.allio.uno.core.metadata.Metadata;
import cc.allio.uno.core.metadata.convert.Converter;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.context.ApplicationContext;

/**
 * 数据源转换
 *
 * @author j.x
 * @date 2022/9/27 15:13
 * @since 1.0
 */
public interface SourceConverter<T extends Metadata> extends Converter<T> {

    /**
     * 数据处理准备阶段，其目的是准备一些必要的数据
     *
     * @param context Spring上下文对象，用于获取Spring的Bean
     * @param value   json数据
     * @return 预处理后的数据
     * @throws Throwable 数据处理出错抛出的异常
     * @see JsonNodeEnhancer
     */
    default JsonNode prepare(ApplicationContext context, JsonNode value) throws Throwable {
        return value;
    }

    /**
     * 数据转换为时序数据真实动作。
     *
     * @param context Spring上下文对象，用于获取Spring的Bean
     * @param value   json数据
     * @return 时序数据实例
     * @throws Throwable 数据处理出错抛出的异常
     * @see JsonNodeEnhancer
     */
    T doConvert(ApplicationContext context, JsonNode value) throws Throwable;

    @Override
    default T execute(ApplicationContext context, Object value) throws Throwable {
        if (!(value instanceof JsonNode)) {
            throw new InstantiationException("value not not null and type JsonNode");
        }
        JsonNode prepareJsonNode = prepare(context, (JsonNode) value);
        return doConvert(context, prepareJsonNode);
    }
}
