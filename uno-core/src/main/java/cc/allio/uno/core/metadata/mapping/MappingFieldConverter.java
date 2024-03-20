package cc.allio.uno.core.metadata.mapping;

import cc.allio.uno.core.metadata.convert.Converter;

/**
 * 映射字段转换器
 *
 * @author j.x
 * @date 2022/12/14 12:55
 * @see DefaultMappingFieldConverter
 * @see ArrayStringMappingFieldConverter
 * @since 1.1.3
 */
public interface MappingFieldConverter<T> extends Converter<T> {

    /**
     * Array Mapping Field
     */
    String ARRAY_STRING_MAPPING_FIELD = "array_string_mapping_field";

    /**
     * 通用 Mapping映射器
     */
    String GENERIC_FIELD_MAPPING_FIELD = "default_field";

    /**
     * 获取映射的字段
     *
     * @return
     */
    MappingField getMappingField();

    /**
     * 关键字Key-Converter
     *
     * @return String
     */
    String keyConverter();
}
