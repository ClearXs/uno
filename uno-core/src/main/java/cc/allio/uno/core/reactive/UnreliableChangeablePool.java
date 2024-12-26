package cc.allio.uno.core.reactive;

import cc.allio.uno.core.util.comparator.ObjectComparator;
import com.google.common.collect.Maps;

import java.util.Comparator;
import java.util.Map;

/**
 * 数据资源池
 *
 * @author j.x
 * @since 1.0
 */
public class UnreliableChangeablePool {

    /**
     * 数据资源池
     */
    private static final Map<String, UnreliableChangeable<?>> pool = Maps.newConcurrentMap();

    public static <T> UnreliableChangeable<T> request(String resourceKey) {
        return (UnreliableChangeable<T>) request(resourceKey, new ObjectComparator());
    }


    /**
     * 根据指定的资源key获取实例
     *
     * @param resourceKey 资源key
     * @return UnreliableChangeable实例或者Null
     */
    public static <T> UnreliableChangeable<T> request(String resourceKey, Comparator<T> comparator) {
        return (UnreliableChangeable<T>) pool.computeIfAbsent(resourceKey, s -> UnreliableChangeable.newInstance(comparator));
    }
}
