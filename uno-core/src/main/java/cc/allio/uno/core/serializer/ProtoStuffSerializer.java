package cc.allio.uno.core.serializer;

import com.google.auto.service.AutoService;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ProtoStuff序列化器
 * @author j.x
 */
@AutoService(Serializer.class)
public class ProtoStuffSerializer extends AbstractSerializer<Schema<?>> {

    private static final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    @Override
    public byte[] serialize(Object obj) {
        Schema<Object> schema = RuntimeSchema.getSchema((Class<Object>) obj.getClass());
        byte[] data;
        try {
            data = ProtostuffIOUtil.toByteArray(obj, schema, BUFFER);
        } finally {
            BUFFER.clear();
        }
        return data;
    }

    @Override
    public <K, V> Map<K, V> deserializeForMap(byte[] bytes, Class<K> keyClass, Class<V> valueClass) {
        return null;
    }

    @Override
    public <T> List<T> deserializeForList(byte[] bytes, Class<T> elementClass) {
        return null;
    }

    @Override
    public <T> Set<T> deserializeForSet(byte[] bytes, Class<T> elementClass) {
        return null;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> except) {
        Schema<T> schema = RuntimeSchema.getSchema(except);
        T t = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, t, schema);
        return t;
    }

    @Override
    public Schema<?> newWorker() {
        return null;
    }

    @Override
    protected byte[] doSerialize(Schema<?> worker, Object obj) {
        return new byte[0];
    }

    @Override
    protected <T> T doDeserialize(Schema<?> worker, Class<T> type, byte[] data) {
        return null;
    }

    @Override
    protected Schema<?> workerDefault() {
        return null;
    }

}
