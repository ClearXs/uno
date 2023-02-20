package cc.allio.uno.core.metadata.convert;

import cc.allio.uno.core.metadata.mapping.MappingMetadata;
import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.metadata.CompositeMetadata;
import cc.allio.uno.core.metadata.Metadata;
import cc.allio.uno.core.serializer.JsonNodeEnhancer;
import cc.allio.uno.core.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * 抽象JSON转换器，提供JSON方式转换
 *
 * @author jiangwei
 * @date 2022/9/13 11:36
 * @since 1.1.0
 */
@Slf4j
public abstract class AbstractJsonConverter<T extends Metadata> extends AbstractRichConverter<T> {

    /**
     * excludeNotNecessaryFilter 是否需要排除使用{@link Metadata#getMapping() }key作为过滤条件
     */
    private final boolean excludeNotNecessaryFilter;

    protected AbstractJsonConverter() {
        this(null);
    }

    protected AbstractJsonConverter(Class<? extends T> convertType) {
        this(convertType, false);
    }

    protected AbstractJsonConverter(Class<? extends T> convertType, boolean excludeNotNecessaryFilter) {
        this.excludeNotNecessaryFilter = excludeNotNecessaryFilter;
        setConvertType(convertType);
    }

    @Override
    public T execute(ApplicationContext context, Object value) throws Throwable {
        JsonNode root = JsonUtil.readTree(value.toString());
        return execute(root);
    }

    /**
     * <ul>
     *     <li>实例化元数据</li>
     *     <li>判断元数据否是符合类型，如果是则进行复合数据创建</li>
     * </ul>
     *
     * @param root json数据
     * @return Metadata
     * @throws Throwable 赋值过程中执行错误抛出异常
     */
    public T execute(JsonNode root) throws Throwable {
        Class<? extends T> convertType = getConvertType();
        if (convertType == null) {
            throw new IllegalArgumentException("convert type must not null");
        }
        T metadata = convertType.newInstance();
        return execute(root, metadata);
    }

    /**
     * 指给定的元数据进行转换
     * <ul>
     *     <li>实例化元数据</li>
     *     <li>判断元数据否是符合类型，如果是则进行复合数据创建</li>
     * </ul>
     *
     * @param root     json数据
     * @param metadata 元数据
     * @return Metadata
     */
    public T execute(JsonNode root, T metadata) throws Throwable {
        if (metadata instanceof CompositeMetadata) {
            List<T> composeSequential = (List<T>) ((CompositeMetadata) metadata).getCompositeMetadata();
            composeSequential.forEach(actual -> executeAssignmentAction(root, actual, excludeNotNecessaryFilter));
        } else {
            executeAssignmentAction(root, metadata, excludeNotNecessaryFilter);
        }
        return metadata;
    }

    /**
     * 执行赋值的动作
     *
     * @param root                      json root节点
     * @param metadata                  元数据
     * @param excludeNotNecessaryFilter 排除必要过滤数据
     */
    protected void executeAssignmentAction(JsonNode root, T metadata, boolean excludeNotNecessaryFilter) {
        ObjectWrapper wrapper = new ObjectWrapper(metadata);
        JsonNodeEnhancer jsonEnhancer = new JsonNodeEnhancer(root);
        MappingMetadata mappingMetadata = metadata.getMapping();
        Flux.fromStream(mappingMetadata.entrySet().stream())
                .flatMap(mapping -> {
                    String name = mapping.getKey().getName();
                    Object expected = jsonEnhancer.asValue(name);
                    return executeAssignmentAction(name, expected, metadata, wrapper, excludeNotNecessaryFilter);
                })
                .onErrorContinue((ex, o) -> log.debug("error {} converter continue", ex.getMessage()))
                // 设置那些没有映射入值的字段,设置为默认值
                .thenMany(
                        Flux.fromStream(Optional.ofNullable(metadata.getUndefinedValues()).orElse(Maps.newHashMap()).entrySet().stream()))
                .flatMap(entry -> wrapper.set(mappingMetadata.get(entry.getKey()).getName()))
                // 触发默认赋值动作
                .then(executeAssignmentDefaultAction(metadata, wrapper))
                .subscribe();
    }

    /**
     * 赋值默认值动作
     *
     * @param sequential 时序数据实例
     * @param wrapper    sequential对象包装器
     */
    protected abstract Mono<Void> executeAssignmentDefaultAction(T sequential, ObjectWrapper wrapper);

}
