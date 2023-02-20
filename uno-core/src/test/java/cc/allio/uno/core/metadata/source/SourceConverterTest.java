package cc.allio.uno.core.metadata.source;

import cc.allio.uno.core.metadata.UserMetadata;
import cc.allio.uno.core.util.JsonUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SourceConverterTest extends Assertions {

    @Test
    void testConverterName() throws Throwable {
        TestSourceConverter converter = new TestSourceConverter();
        ObjectNode jsonNode = (ObjectNode) JsonUtil.empty();
        jsonNode.put("name", "name");
        UserMetadata metadata = converter.execute(null, jsonNode);
        assertEquals(metadata.getName(), "name");
    }

    @Test
    void testConvertDefault() throws Throwable {
        TestSourceConverter converter = new TestSourceConverter();
        ObjectNode jsonNode = (ObjectNode) JsonUtil.empty();
        UserMetadata metadata = converter.execute(null, jsonNode);
        assertEquals(metadata.getId(), "id");
    }
}
