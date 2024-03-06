package cc.allio.uno.core.api;

import java.util.Collection;
import java.util.Optional;

/**
 * Java中接口类型不存在{@link Object#equals(Object)}方法，该接口的定义为了能够使得Java接口能够有类似的方法。
 * <p>另外一个目的是提供显示的接口来实现{@code equals}方法，避免在调试环节不知道为何两个对象相等。</p>
 *
 * @author jiangwei
 * @date 2024/2/16 20:17
 * @since 1.1.7
 */
public interface EqualsTo<T> {

    /**
     * 类似于{@link Object#equals(Object)}
     *
     * @param other other
     * @return ture if equivalent
     */
    boolean equalsTo(T other);

    /**
     * 判断给定的集合中是否包含该对象
     *
     * @param collection collection
     * @return T
     */
    default boolean in(Collection<T> collection) {
        return collection.stream().anyMatch(this::equalsTo);
    }

    /**
     * 判断当前对象是否在给定的collection，如果在则返回相等的对象
     *
     * @param collection collection
     * @return optional T
     */
    default Optional<T> filter(Collection<T> collection) {
        return collection.stream().filter(this::equalsTo).findFirst();
    }
}
