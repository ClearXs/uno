package cc.allio.uno.core.serializer;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;

/**
 * Protostuff序列化器测试
 *
 * @author j.x
 */
class ProtoStuffSerializerTest extends BaseTestCase {

    Serializer serializer;

    @Override
    protected void onInit() throws Throwable {
        serializer = SerializerHolder.holder().get(ProtoStuffSerializer.class);
    }

    @Test
    void testSerialize() {
        String s = "serializer";
        byte[] bytes = serializer.serialize(s);
        String deserialize = serializer.deserialize(bytes, String.class);
        assertEquals("serializer", deserialize);
    }
}
