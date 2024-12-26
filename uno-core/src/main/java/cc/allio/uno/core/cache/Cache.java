package cc.allio.uno.core.cache;

import cc.allio.uno.core.bean.BeanInfoWrapper;

import java.beans.IntrospectionException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiPredicate;

/**
 * 缓存
 *
 * @author j.x
 * @see InMemoryCache
 * @see ConcurrentMemoryCache
 * @since 1.0
 */
public interface Cache<T> {

    /**
     * 放入缓存
     *
     * @param cache 缓存数据
     * @return 如果往插入失败时返回null否则返回插入的实体
     * @throws NullPointerException 放入缓存失败时抛出
     */
    T put(T cache);

    /**
     * 批量放入缓存
     *
     * @param caches 数据集合
     * @throws NullPointerException 入参缓存集合为Null时抛出
     */
    void putAll(List<T> caches);

    /**
     * 获取指定索引位置的缓存数据
     *
     * @param index 索引位置
     * @return 指定索引处的数据
     * @throws IndexOutOfBoundsException 根据索引不能找到数据时抛出
     */
    T get(int index);

    /**
     * 在某个范围内获取缓存数据
     *
     * @param start 起点
     * @param end   数据终点
     * @return 不能修改的List数据结果
     * @throws IndexOutOfBoundsException 根据索引不能找到数据时抛出
     */
    List<T> get(int start, int end);

    /**
     * 获取缓存的全部数据
     *
     * @return 不能修改的List数据结果，如果没有数据默认返回{@link java.util.Collections#EMPTY_LIST}
     */
    List<T> get();

    /**
     * 删除指定位置的缓存
     *
     * @param index 缓存的索引位置
     * @throws UnsupportedOperationException 在缓存中不支持移除操作时抛出
     * @throws IndexOutOfBoundsException     索引不再缓存内时抛出
     */
    void remove(int index);

    /**
     * 根据指定对象删除缓存的数据
     *
     * @param o 等待删除的数据
     * @throws NullPointerException 等待删除数据为空时抛出
     */
    void remove(T o);

    /**
     * 根据指定对象删除缓存的数据，使用指定的比较器判断删除，获取删除数据的索引下标，如果下标是存在的，则进行删除
     *
     * @param o          等待删除的数据
     * @param comparator 判断两个对象是否为一个对象
     * @throws NullPointerException 当校验参数错误时抛出
     */
    default void remove(T o, BiPredicate<T, T> comparator) {
        if (Objects.isNull(o) || Objects.isNull(comparator)) {
            throw new NullPointerException("");
        }
        Long index = index(o, comparator);
        if (index > -1) {
            remove(index.intValue());
        }
    }

    /**
     * 释放缓存
     */
    void clear();

    /**
     * 获取默认的comparator
     *
     * @return comparator实例对象
     */
    BiPredicate<T, T> comparator();

    // ---------------------------- Default Method ----------------------------

    /**
     * 根据指定数据查找他在缓存中的索引
     *
     * @param o 指定数据，需要实现{@link Object#equals(Object)}方法
     * @return 指定索引位置或者-1
     * @see Object#equals(Object)
     */
    default Long index(T o) {
        return index(o, comparator());
    }

    /**
     * 根据指定数据查找他在缓存中的索引
     *
     * @param o          指定数据，需要实现{@link Object#equals(Object)}方法
     * @param comparator 判断两个对象是否为一个对象
     * @return 指定索引位置或者-1
     * @see Object#equals(Object)
     */
    default Long index(T o, BiPredicate<T, T> comparator) {
        List<T> cacheAll = get();
        for (int i = 0; i < cacheAll.size(); i++) {
            if (comparator == null) {
                comparator = Objects::equals;
            }
            boolean test = comparator.test(o, cacheAll.get(i));
            if (test) {
                return (long) i;
            }
        }
        return -1L;
    }

    /**
     * 根据对象某个字段的名称从缓存中获取这个对象，从查找的列表中获取第一个
     *
     * @param expectedType 期望类型
     * @param field        字段名称
     * @param value        该字段的值
     * @return 获取到的对象实例
     * @throws NullPointerException 当不存在于缓存中时抛出
     */
    default T getByKey(Class<T> expectedType, String field, Object value) {
        List<T> lists = get();
        return lists.stream()
                .filter(o -> {
                    try {
                        BeanInfoWrapper<?> infoWrapper = new BeanInfoWrapper<>(expectedType);
                        AtomicReference<Object> fieldValue = new AtomicReference<>();
                        infoWrapper.get(o, field)
                                .subscribe(fieldValue::set);
                        return fieldValue.get().equals(value);
                    } catch (IntrospectionException | NullPointerException e) {
                        throw new NullPointerException(e.getMessage());
                    }
                })
                .findFirst()
                .orElseThrow(() -> new NullPointerException(String.format("Field Name: %s dose isn't cache", field)));

    }

    /**
     * 缓存大小
     *
     * @return 缓存大小
     */
    default Long size() {
        return (long) get().size();
    }

    /**
     * 删除指定集合内的数据
     *
     * @param c 待删除的集合数据
     * @see #remove(Object)
     */
    default void removeAll(Collection<T> c) {
        c.forEach(this::remove);
    }

}
