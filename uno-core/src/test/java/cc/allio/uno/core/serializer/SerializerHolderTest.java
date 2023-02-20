package cc.allio.uno.core.serializer;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;

class SerializerHolderTest extends BaseTestCase {

    private SerializerHolder holder;

    @Override
    protected void onInit() throws Throwable {
        holder = SerializerHolder.holder();
    }

    /**
     * 测试能够获取默认对象
     */
    @Test
    void testGetDefault() {
        Serializer serializer = holder.get();
        assertNotNull(serializer);
        assertEquals(ProtoStuffSerializer.class.getName(), serializer.getClass().getName());
    }

    /**
     * 获取JacksonSerializer序列化器
     */
    @Test
    void testGetJacksonSerializer() {
        Serializer serializer = holder.get(JacksonSerializer.class);
        assertNotNull(serializer);
        assertEquals(JacksonSerializer.class.getName(),serializer.getClass().getName());
    }

    @Override
    protected void onDown() throws Throwable {

    }
}
