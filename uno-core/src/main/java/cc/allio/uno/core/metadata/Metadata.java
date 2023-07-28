package cc.allio.uno.core.metadata;

import cc.allio.uno.core.metadata.mapping.MappingField;
import cc.allio.uno.core.metadata.mapping.MappingMetadata;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 元数据接口定义
 *
 * @author jiangwei
 * @date 2022/11/21 22:25
 * @since 1.1.1
 */
public interface Metadata {

    /**
     * 获取映射元数据
     *
     * @return 映射元数据实例
     */
    MappingMetadata getMapping();

    /**
     * 获取无法解析元数据
     *
     * @return map实例，key 无法解析key，value 实例数据
     */
    Map<String, Object> getValues();

    /**
     * 放入映射值，以{@link MappingField}作为Key，把数据放入其中。
     *
     * @param fieldName 字段名称
     * @param value     映射值
     * @return Mono<Object>值
     */
    default Mono<Object> putMappingValue(String fieldName, Object value) {
        return putMappingValue(MappingField.builder().name(fieldName).build(), value);
    }

    /**
     * 放入映射值，以{@link MappingField}作为Key，把数据放入其中。
     *
     * @param mappingField 映射字段
     * @param value        映射值
     * @return Mono<Object>值
     */
    default Mono<Object> putMappingValue(MappingField mappingField, Object value) {
        return Mono.just(value);
    }

    /**
     * 获取MappingValue
     *
     * @param fieldName 目标映射字段
     * @return Mono<Object>
     */
    default Mono<Object> getMappingValue(String fieldName) {
        return getMappingValue(MappingField.builder().name(fieldName).build(), Object.class);
    }

    /**
     * 获取MappingValue
     *
     * @param mappingField MappingField
     * @return Mono<Object>
     */
    default Mono<Object> getMappingValue(MappingField mappingField) {
        return getMappingValue(mappingField, Object.class);
    }

    /**
     * 获取MappingValue
     *
     * @param fieldName 目标映射字段
     * @param type      字段类型
     * @param <T>       字段泛型
     * @return Mono<T>
     * @see #getMappingValue(MappingField, Class)
     */
    default <T> Mono<T> getMappingValue(String fieldName, Class<T> type) {
        return getMappingValue(MappingField.builder().name(fieldName).build(), type);
    }

    /**
     * 获取MappingValue
     *
     * @param mappingField MappingField
     * @param type         字段类型
     * @param <T>          字段泛型
     * @return Mono<T>
     * @throws NullPointerException 当mappingField or type为空时抛出
     * @throws ClassCastException   当type转换失败时抛出
     */
    default <T> Mono<T> getMappingValue(MappingField mappingField, Class<T> type) {
        return Mono.empty();
    }
}
