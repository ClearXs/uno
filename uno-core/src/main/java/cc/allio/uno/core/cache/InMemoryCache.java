package cc.allio.uno.core.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * 基于内存的缓存
 *
 * @author jiangwei
 * @date 2022/2/9 14:25
 * @since 1.0
 */
public class InMemoryCache<T> implements Cache<T> {

    private final List<T> buffer = new ArrayList<>();

    @Override
    public T put(T cache) {
        buffer.add(cache);
        return cache;
    }

    @Override
    public void putAll(List<T> caches) {
        buffer.addAll(caches);
    }

    @Override
    public T get(int index) {
        return buffer.get(index);
    }

    @Override
    public List<T> get(int start, int end) {
        return buffer.subList(start, end);
    }

    @Override
    public List<T> get() {
        return Collections.unmodifiableList(buffer);
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
    public void remove(int index) {
        buffer.remove(index);
    }

    @Override
    public void remove(T o) {
        buffer.remove(o);
    }

    @Override
    public void removeAll(Collection<T> c) {
        buffer.removeAll(c);
    }
}
