package cc.allio.uno.core.serializer;

import cc.allio.uno.core.BaseTestCase;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

class JacksonSerializerTest extends BaseTestCase {

    private Serializer serializer;

    Map<String, Object> data = new HashMap<>();

    @Override
    protected void onInit() throws Throwable {
        serializer = SerializerHolder.holder().get(JacksonSerializer.class);
        data.put("map", "map");
    }

    @Test
    void serialize() {
        byte[] serialize = serializer.serialize(data);

        Map<String, Object> deserialize = serializer.deserializeForMap(serialize, String.class, Object.class);
        deserialize.forEach((key, value) -> {
            assertEquals("map", key);
            assertEquals("map", value);
        });
    }

    @Test
    void testRegisterWorker() {
        String content = "{ \"id\": 1,\"itemName\": \"theItem\"}";

        ((JacksonSerializer) serializer).registerWorker(Item.class.getName(), worker -> {
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Item.class, new ItemDeserializer());
            worker.registerModule(module);
        });
        byte[] serialize = serializer.serialize(content);
        Item item = serializer.deserialize(serialize, Item.class);
        assertEquals(2, item.getId());
        assertEquals("theItem", item.getItemName());
    }

    @Data
    @AllArgsConstructor
    static class Item {

        private Integer id;
        private String itemName;
    }

    static class ItemDeserializer extends StdDeserializer<Item> {

        private ObjectMapper mapper = new ObjectMapper();

        public ItemDeserializer() {
            this(null);

        }

        protected ItemDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Item deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonNode node = mapper.readTree(jp.getText());
            int id = 2;
            String itemName = node.get("itemName").asText();
            return new Item(id, itemName);
        }
    }
}
