package cc.allio.uno.core.serializer;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 序列化对象持有者，通过使用spi来获取对象。
 *
 * @author jw
 * @date 2021/12/2 16:23
 */
public class SerializerHolder {

    /**
     * 序列化持有者持有者
     */
    private final Map<String, Serializer> serializers = new ConcurrentHashMap<>();

    private static final SerializerHolder holder = new SerializerHolder();

    private static final Class<JacksonSerializer> DEFAULT = JacksonSerializer.class;

    private SerializerHolder() {
        // 饿加载
        ServiceLoader<Serializer> load = ServiceLoader.load(Serializer.class);
        for (Serializer serializer : load) {
            serializers.putIfAbsent(serializer.getClass().getName(), serializer);
        }
    }

    /**
     * 获取默认序列化器 - JacksonSerializer
     *
     * @return 默认序列化器
     * @see JacksonSerializer
     */
    public Serializer get() {
        return serializers.getOrDefault(DEFAULT.getName(), new JacksonSerializer());
    }

    /**
     * 尝试从spi中获取期望的序列化器，如果存在则返回，
     *
     * @param except 期望的序列化器
     * @return 返回寻找到的序列化器，如果没有找到则返回默认的序列化器
     */
    public Serializer get(Class<? extends Serializer> except) {
        if (except == null) {
            throw new NullPointerException("except Serializer is null");
        }
        if (except.equals(DEFAULT)) {
            return get();
        } else {
            return serializers.computeIfAbsent(except.getName(), key -> get());
        }
    }

    public static SerializerHolder holder() {
        return holder;
    }

}
