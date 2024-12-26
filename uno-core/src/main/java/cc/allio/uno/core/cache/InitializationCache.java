package cc.allio.uno.core.cache;

import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * 委托{@link Cache}进行初始化的回调
 *
 * @author j.x
 * @see CacheCallable
 * @since 1.0
 */
public class InitializationCache<T> implements Cache<T> {

    /**
     * 委托缓存的目标对象
     */
    private final Cache<T> delegate;

    /**
     * 初始化缓存
     *
     * @param delegate 缓存的代理对象
     * @param callable 缓存回调
     */
    public InitializationCache(Cache<T> delegate, CacheCallable<T> callable) {
        this.delegate = delegate;
        // 初始化回调
        callable.call(this, get());
        // 清除数据
        clear();
    }

    @Override
    public T put(T cache) {
        return delegate.put(cache);
    }

    @Override
    public void putAll(List<T> caches) {
        delegate.putAll(caches);
    }

    @Override
    public T get(int index) {
        return delegate.get(index);
    }

    @Override
    public List<T> get(int start, int end) {
        return delegate.get(start, end);
    }

    @Override
    public List<T> get() {
        return delegate.get();
    }

    @Override
    public void remove(int index) {
        delegate.remove(index);
    }

    @Override
    public void remove(T o) {
        delegate.remove(o);
    }

    @Override
    public Long index(T o) {
        return delegate.index(o);
    }

    @Override
    public void removeAll(Collection<T> c) {
        delegate.removeAll(c);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public BiPredicate<T, T> comparator() {
        return null;
    }

    @Override
    public Long size() {
        return delegate.size();
    }
}
