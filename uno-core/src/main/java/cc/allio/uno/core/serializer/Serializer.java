package cc.allio.uno.core.serializer;

import cc.allio.uno.core.exception.SerializationException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 序列化器接口，通过spi进行获取实例对象，目前可以支持protostuff与jackson两种方式
 *
 * @author jw
 * @date 2021/12/2 16:05
 */
public interface Serializer {

    /**
     * 序列化化操作，返回序列化后的数组
     *
     * @param obj 一个java对象
     * @return byte[]
     */
    byte[] serialize(Object obj);

    /**
     * 默认返回Object对象反序列化方法
     *
     * @param bytes 字节数组
     * @return object实例
     */
    default Object deserialize(byte[] bytes) {
        return deserialize(bytes, Object.class);
    }

    /**
     * 返回map类型的反序列化数据
     *
     * @param bytes      反序列化的二进制数据
     * @param keyClass   Key-class对象
     * @param valueClass value-class对象
     * @param <K>        Key-泛型
     * @param <V>        Value-泛型
     * @return Map实例
     * @throws SerializationException 序列化失败时抛出
     */
    <K, V> Map<K, V> deserializeForMap(byte[] bytes, Class<K> keyClass, Class<V> valueClass);

    /**
     * 返回list类型的数据
     *
     * @param bytes        字节数组
     * @param elementClass 元素Class对象
     * @param <T>          每一个元素泛型
     * @return List实例
     * @throws SerializationException 序列化失败时抛出
     */
    <T> List<T> deserializeForList(byte[] bytes, Class<T> elementClass);

    /**
     * 返回Set类型的数据
     *
     * @param bytes        字节数组
     * @param elementClass 元素Class对象
     * @param <T>          每一个元素泛型
     * @return Set实例
     * @throws SerializationException 序列化失败时抛出
     */
    <T> Set<T> deserializeForSet(byte[] bytes, Class<T> elementClass);

    /**
     * 反序列化，返回由字节数组反序列化的java实体对象
     *
     * @param <T>    实体泛型
     * @param bytes  字节数组
     * @param except 期望获取实体的类型
     * @return 实例对象
     */
    <T> T deserialize(byte[] bytes, Class<T> except);
}
