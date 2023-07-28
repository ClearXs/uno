package cc.allio.uno.core.metadata.mapping;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.core.metadata.UserMetadata;
import cc.allio.uno.core.metadata.convert.Converter;
import cc.allio.uno.core.metadata.convert.ConverterFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class MappingFieldConverterTest extends BaseTestCase {

    @Test
    void testArrayConverter() throws Throwable {
        Converter<UserMetadata> converter = ConverterFactory.createConverter(UserMetadata.class);
        ObjectNode jsonNode = JsonUtils.empty();
        JsonNode types = JsonUtils.readTree("[\"288.57\",\"288.57\",\"288.57\",\"288.57\",\"288.57\",\"288.57\"]");
        jsonNode.put("type", types);
        UserMetadata userMetadata = converter.execute(null, jsonNode);
        assertEquals("288.57", userMetadata.getType());
    }

    /**
     * Test Case: 根据{@link MappingField}获取对应的value
     */
    @Test
    void testGetMappingValue() throws Throwable {
        Converter<UserMetadata> converter = ConverterFactory.createConverter(UserMetadata.class);
        ObjectNode jsonNode = JsonUtils.empty();
        jsonNode.put("name", "name");

        UserMetadata metadata = converter.execute(null, jsonNode);
        metadata.getMappingValue(MappingField.builder().name("name").build())
                .as(StepVerifier::create)
                .expectNext("name")
                .verifyComplete();
    }

    @Test
    void testDefaultValue() throws Throwable {
        Converter<UserMetadata> converter = ConverterFactory.createConverter(UserMetadata.class);
        ObjectNode jsonNode = JsonUtils.empty();
        UserMetadata metadata = converter.execute(null, jsonNode);
        metadata.getMappingValue(MappingField.builder().name("name").build())
                .as(StepVerifier::create)
                .expectNext("default")
                .verifyComplete();
    }
}
