package cc.allio.uno.core.metadata;

import cc.allio.uno.core.metadata.convert.ConverterFactory;
import cc.allio.uno.core.User;
import cc.allio.uno.uno.core.metadata.mapping.*;
import com.google.common.collect.Maps;
import reactor.core.publisher.Mono;

import java.util.Map;

public class UserMetadata extends User implements Metadata {

    private final MappingMetadata mappingMetadata;
    private final Map<MappingField, Object> mappingValues;

    public UserMetadata() {
        this.mappingValues = Maps.newHashMap();
        this.mappingMetadata = new DefaultMappingMetadata(ConverterFactory.createConverter(this.getClass()));
        mappingMetadata.addMapping(MappingField.builder().name("id").build(), MappingField.builder().name("id").build());
        mappingMetadata.addMapping(MappingField.builder().name("name").build(), MappingField.builder().name("name").defaultValue("default").build());
        mappingMetadata.addMapping(MappingField.builder().name("type").build(), MappingField.builder().name("type").converter(KeyMappingFieldConverterFactory.getMappingFieldConverter(MappingFieldConverter.ARRAY_STRING_MAPPING_FIELD)).build());
    }

    @Override
    public MappingMetadata getMapping() {
        return mappingMetadata;
    }

    @Override
    public Map<String, Object> getUndefinedValues() {
        return null;
    }

    @Override
    public Mono<Object> putMappingValue(MappingField mappingField, Object value) {
        mappingValues.put(mappingField, value);
        return Mono.just(value);
    }

    @Override
    public <T> Mono<T> getMappingValue(MappingField mappingField, Class<T> type) {
        return Mono.justOrEmpty(mappingValues.get(mappingField)).cast(type);
    }
}
