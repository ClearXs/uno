package cc.allio.uno.core.metadata.mapping;

import cc.allio.uno.core.metadata.Metadata;
import cc.allio.uno.core.metadata.convert.Converter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 默认实现
 *
 * @author jiangwei
 * @date 2022/9/13 13:30
 * @since 1.1.0
 */
public class DefaultMappingMetadata extends LinkedHashMap<MappingField, MappingField> implements MappingMetadata {

    private final Converter<? extends Metadata> converter;

    public DefaultMappingMetadata(Converter<? extends Metadata> converter) {
        this(Collections.emptyMap(), converter);
    }

    public DefaultMappingMetadata(Map<MappingField, MappingField> otherMappings, Converter<? extends Metadata> converter) {
        putAll(otherMappings);
        this.converter = converter;
    }

    @Override
    public void addMapping(MappingField source, MappingField target) {
        if (!containsKey(source.getName())) {
            put(source, target);
        }
    }

    @Override
    public MappingField get(String sourceFieldName) {
        return Optional.ofNullable(get(MappingField.builder().name(sourceFieldName).build()))
                .orElseThrow(() -> new NullPointerException(String.format("According to %s getValue Mapping relationship Failed, does not exist", sourceFieldName)));
    }

    @Override
    public void remove(String sourceFieldName) {
        remove(MappingField.builder().name(sourceFieldName).build());
    }

    @Override
    public boolean containsKey(String sourceFieldName) {
        return containsKey(MappingField.builder().name(sourceFieldName).build());
    }

    @Override
    public Converter<? extends Metadata> getConverter() {
        return converter;
    }
}
