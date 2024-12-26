package cc.allio.uno.core.metadata.mapping;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 按照{@link MappingFieldConverter#keyConverter}的{@link MappingFieldConverter}工厂
 *
 * @author j.x
 * @since 1.1.4
 */
public class KeyMappingFieldConverterFactory {

    private KeyMappingFieldConverterFactory() {
    }

    private static final Map<String, MappingFieldConverter<?>> mappingFieldConverterCaches = Maps.newHashMap();

    static {
        putMappingFieldConverter(MappingFieldConverter.ARRAY_STRING_MAPPING_FIELD, new ArrayStringMappingFieldConverter());
        putMappingFieldConverter(MappingFieldConverter.GENERIC_FIELD_MAPPING_FIELD, new DefaultMappingFieldConverter());
    }


    public static void putMappingFieldConverter(String keyConverter, MappingFieldConverter<?> mappingFieldConverter) {
        mappingFieldConverterCaches.put(keyConverter, mappingFieldConverter);
    }

    /**
     * 获取{@link MappingFieldConverter}实例
     *
     * @return MappingFieldConverter
     */
    public static MappingFieldConverter<?> getMappingFieldConverter(String keyConverter) {
        return mappingFieldConverterCaches.get(keyConverter);
    }
}
