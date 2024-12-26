package cc.allio.uno.core.metadata;

import cc.allio.uno.core.metadata.convert.ConverterFactory;
import cc.allio.uno.core.metadata.mapping.DefaultMappingMetadata;
import cc.allio.uno.core.metadata.mapping.MappingField;
import cc.allio.uno.core.metadata.mapping.MappingMetadata;
import cc.allio.uno.core.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

/**
 * JSON 源数据
 *
 * @author j.x
 */
public class JsonMetadata implements Metadata {

    private final Map<String, Object> values;
    private final DefaultMappingMetadata metadata;

    public JsonMetadata(JsonNode jsonNode) {
        this.values = JsonUtils.readMap(jsonNode.toString(), String.class, Object.class);
        this.metadata = new DefaultMappingMetadata(ConverterFactory.createConverter());
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String key = entry.getKey();
            metadata.addMapping(MappingField.builder().name(key).build(), MappingField.builder().name(key).build());
        }
    }

    public JsonMetadata(Map<String, Object> source) {
        this.metadata = new DefaultMappingMetadata(ConverterFactory.createConverter());
        this.values = source;
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            String key = entry.getKey();
            metadata.addMapping(MappingField.builder().name(key).build(), MappingField.builder().name(key).build());
        }
    }

    @Override
    public MappingMetadata getMapping() {
        return metadata;
    }

    @Override
    public Map<String, Object> getValues() {
        return values;
    }
}
