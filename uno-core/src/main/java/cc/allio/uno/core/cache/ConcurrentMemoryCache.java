package cc.allio.uno.core.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiPredicate;

/**
 * 实用线程安全的{@link CopyOnWriteArraySet}作为缓存
 * <b>需要放入的缓存实现{@link Object#equals(Object)}</b>
 *
 * @author jiangwei
 * @date 2022/2/9 15:15
 * @since 1.0
 */
public class ConcurrentMemoryCache<T> implements Cache<T> {

    /**
     * 并发缓存
     */
    private final CopyOnWriteArraySet<T> buffer = new CopyOnWriteArraySet<>();

    @Override
    public T put(T cache) {
        boolean put = buffer.add(cache);
        if (put) {
            return cache;
        }
        return null;
    }

    @Override
    public void putAll(List<T> caches) {
        buffer.addAll(caches);
    }

    @Override
    public T get(int index) {
        return toList().get(index);
    }

    @Override
    public List<T> get(int start, int end) {
        return toList().subList(start, end);
    }

    @Override
    public List<T> get() {
        return Collections.unmodifiableList(toList());
    }

    @Override
    public void remove(int index) {
        remove(get(index));
    }

    @Override
    public void remove(T o) {
        buffer.remove(o);
    }

    @Override
    public void clear() {
        buffer.clear();
    }

    @Override
    public BiPredicate<T, T> comparator() {
        return null;
    }

    @Override
    public void removeAll(Collection<T> c) {
        buffer.removeAll(c);
    }

    private List<T> toList() {
        return new ArrayList<>(buffer);
    }
}
