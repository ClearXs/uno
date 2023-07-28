package cc.allio.uno.core.serializer;

import cc.allio.uno.core.exception.SerializationException;
import cc.allio.uno.core.util.JsonUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Jackson序列化器，要求反序列化的对象有<b>默认构造器</b>
 *
 * @author jw
 * @date 2021/12/3 11:10
 */
@AutoService(Serializer.class)
public class JacksonSerializer extends AbstractSerializer<ObjectMapper> {

    /**
     * 使用默认配置对象映射
     */
    private static ObjectMapper defaultMapper;

    @Override
    protected byte[] doSerialize(ObjectMapper worker, Object obj) {
        try {
            return worker.writeValueAsBytes(obj);
        } catch (JsonProcessingException ex) {
            throw new SerializationException(ex);
        }
    }

    @Override
    protected <T> T doDeserialize(ObjectMapper worker, Class<T> type, byte[] data) {
        try {
            return worker.readValue(data, type);
        } catch (IOException ex) {
            if (ex instanceof JsonParseException && String.class.equals(type)) {
                return (T) new String(data, StandardCharsets.UTF_8);
            }
            throw new SerializationException(ex);
        }
    }

    @Override
    public <K, V> Map<K, V> deserializeForMap(byte[] bytes, Class<K> keyClass, Class<V> valueClass) {
        ObjectMapper worker = workerDefault();
        MapType mapType = worker.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
        try {
            return worker.readValue(bytes, mapType);
        } catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }

    @Override
    public <T> List<T> deserializeForList(byte[] bytes, Class<T> elementClass) {
        ObjectMapper worker = workerDefault();
        CollectionType collectionType = worker.getTypeFactory().constructCollectionType(List.class, elementClass);
        try {
            return worker.readValue(bytes, collectionType);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public <T> Set<T> deserializeForSet(byte[] bytes, Class<T> elementClass) {
        ObjectMapper worker = workerDefault();
        CollectionType collectionType = worker.getTypeFactory().constructCollectionType(Set.class, elementClass);
        try {
            return worker.readValue(bytes, collectionType);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public synchronized ObjectMapper workerDefault() {
        if (defaultMapper == null) {
            defaultMapper = newWorker();
        }
        return defaultMapper;
    }

    @Override
    public ObjectMapper newWorker() {
        return JsonUtils.newJsonMapper();
    }
}
