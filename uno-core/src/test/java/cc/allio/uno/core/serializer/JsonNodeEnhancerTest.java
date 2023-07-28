package cc.allio.uno.core.serializer;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.util.JsonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

public class JsonNodeEnhancerTest extends BaseTestCase {

    @Test
    void testHas() {
        ObjectNode node = (ObjectNode) JsonUtils.empty();
        node.put("test", "test");
        JsonNodeEnhancer enhancer = new JsonNodeEnhancer(node);
        assertTrue(enhancer.has("test"));

    }

    @Override
    protected void onInit() throws Throwable {

    }

    @Override
    protected void onDown() throws Throwable {

    }
}
