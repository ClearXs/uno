package cc.allio.uno.core.metadata.mapping;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 按照{@link MappingFieldConverter#keyConverter}的{@link MappingFieldConverter}工厂
 *
 * @author jiangwei
 * @date 2023/1/3 11:20
 * @since 1.1.4
 */
public class KeyMappingFieldConverterFactory {

    private KeyMappingFieldConverterFactory() {
    }

    public static Map<String, MappingFieldConverter<?>> mappingFieldConverter = Maps.newHashMap();

    static {
        mappingFieldConverter.put(MappingFieldConverter.ARRAY_STRING_MAPPING_FIELD, new ArrayStringMappingFieldConverter());
        mappingFieldConverter.put(MappingFieldConverter.GENERIC_FIELD_MAPPING_FIELD, new DefaultMappingFieldConverter());
    }

    /**
     * 获取{@link MappingFieldConverter}实例
     *
     * @return MappingFieldConverter
     */
    public static MappingFieldConverter<?> getMappingFieldConverter(String keyConverter) {
        return mappingFieldConverter.get(keyConverter);
    }
}
