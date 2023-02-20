package cc.allio.uno.core.serializer;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * 增加抽象序列化层，抽出共用方法
 *
 * @author jw
 * @date 2021/12/4 21:18
 */
public abstract class AbstractSerializer<W> implements Serializer {

    /**
     * 增强的序列化工作者，当某些的类型序列化时，需要增强其的能力，增强后的对象放入cache中，再次使用从cache中获取
     */
    private final Map<String, W> workerCache = new ConcurrentHashMap<>();

    private final Lock lock = new ReentrantLock();

    @Override
    public byte[] serialize(Object obj) {
        lock.lock();
        try {
            return doSerialize(getWorker(obj.getClass().getName()), obj);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> except) {
        lock.lock();
        try {
            return doDeserialize(getWorker(except.getName()), except, bytes);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 由序列化器生成Worker，由客户端增强Worker
     *
     * @param name           需要解析类的权限类名称
     * @param enhancedWorker 增加worker的consumer
     */
    public void registerWorker(String name, Consumer<W> enhancedWorker) {
        workerCache.computeIfAbsent(name, key -> {
            W w = newWorker();
            enhancedWorker.accept(w);
            return w;
        });
    }

    /**
     * 由客户端提供Worker，序列化器保存这个Worker
     *
     * @param name   需要解析类的权限类名称
     * @param worker Worker实例
     */
    public void registerWorker(String name, W worker) {
        workerCache.put(name, worker);
    }

    private W getWorker(String name) {
        // 类的全限定名称
        W worker = Optional.ofNullable(workerCache.get(name)).orElseGet(this::workerDefault);
        if (worker == null) {
            throw new NullPointerException(String.format("Expect Serializer Worker is null by %s", name));
        }
        return worker;
    }

    /**
     * 子类具体实现的序列化
     *
     * @param obj    序列化的对象
     * @param worker 序列化工作者的对象
     * @return 序列化完成后的字节数组
     */
    protected abstract byte[] doSerialize(W worker, Object obj);

    /**
     * 子类具体实现的反序列化
     *
     * @param worker 序列化工作者的对象
     * @param type   期望的序列化完成后的Class对象
     * @param data   字节数据
     * @param <T>    反序列化的泛型对象
     * @return 实例对象
     */
    protected abstract <T> T doDeserialize(W worker, Class<T> type, byte[] data);

    /**
     * 子类提供的默认工作者
     *
     * @return 默认实现的工作者
     */
    protected abstract W workerDefault();

    /**
     * 由子类提供新的Worker
     * 由工厂或者提供者产生？
     *
     * @return 实例的Worker
     */
    protected abstract W newWorker();
}
