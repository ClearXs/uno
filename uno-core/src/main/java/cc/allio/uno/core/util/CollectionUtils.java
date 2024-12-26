package cc.allio.uno.core.util;

import com.google.common.collect.Sets;

import java.util.*;

/**
 * 集合常用工具方法
 *
 * @author j.x
 */
public class CollectionUtils extends org.springframework.util.CollectionUtils {

    private CollectionUtils() {

    }

    /**
     * 创建一个ArrayList
     *
     * @param elements 元素
     * @param <E>      集合类型
     * @return ArrayList
     */
    public static <E> List<E> newArrayList(E... elements) {
        if (elements == null || elements.length == 0) {
            return java.util.Collections.emptyList();
        }
        List<E> arrayList = new ArrayList<>(elements.length);
        java.util.Collections.addAll(arrayList, elements);
        return arrayList;
    }

    /**
     * 创建一个ArrayList
     *
     * @param elements Iterable
     * @param <E>      集合的类型
     * @return List
     */
    public static <E> List<E> newArrayList(Iterable<? extends E> elements) {
        if (elements == null) {
            throw new NullPointerException("to array list error");
        }
        return elements instanceof CollectionUtils
                ? new ArrayList<>((Collection<? extends E>) elements)
                : newArrayList(elements.iterator());
    }

    /**
     * @param elements
     * @param <E>
     * @return
     */
    public static <E> List<E> newArrayList(Iterator<? extends E> elements) {
        if (elements == null) {
            throw new NullPointerException("to array list error");
        }
        ArrayList<E> arrayList = new ArrayList<>();
        while (elements.hasNext()) {
            arrayList.add(elements.next());
        }
        return arrayList;
    }

    /**
     * map(entry)-> list
     *
     * @return Map.Entry的List数据
     */
    public static <K, V> List<V> toList(Map<K, V> map) {
        if (isEmpty(map)) {
            return java.util.Collections.emptyList();
        }
        Collection<V> values = map.values();
        return newArrayList(values.iterator());
    }

    public static <T> List<String> toList(T[] array) {
        List<String> list = new ArrayList<>();
        if (array == null || array.length == 0) {
            return list;
        }
        for (T t : array) {
            list.add(t.toString());
        }
        return list;
    }

    /**
     * 判断集合是否不为空
     *
     * @param collection collection
     * @param <E>        集合元素
     * @return true 不为空 false 为空
     */
    public static <E> boolean isNotEmpty(Collection<E> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断Map是为不为空
     *
     * @param map map
     * @param <K> Key
     * @param <V> Value
     * @return true 不为空 false 为空
     */
    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return !isEmpty(map);
    }

    /**
     * Map集合转换Properties实例
     *
     * @param map map
     * @return Properties实例
     */
    public static Properties mapToProperties(Map<?, ?> map) {
        Properties properties = new Properties();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            properties.setProperty(entry.getKey().toString(), entry.getValue().toString());
        }
        return properties;
    }

    /**
     * 两个集合求并集
     *
     * @param c1  c1
     * @param c2  c2
     * @param <V> 集合元素类型
     * @return 求并集后的集合
     */
    public static <V> Collection<V> union(Collection<V> c1, Collection<V> c2) {
        if (isEmpty(c2) || isEmpty(c1)) {
            return Collections.emptyList();
        }
        Set<V> union = Sets.newHashSet();
        union.addAll(c1);
        union.addAll(c2);
        return union;
    }

    /**
     * 两个集合求交集
     *
     * @param c1  c1
     * @param c2  c2
     * @param <V> 集合元素类型
     * @return 求交集后的元素集合
     */
    public static <V> Collection<V> intersection(Collection<V> c1, Collection<V> c2) {
        Set<V> intersection = Sets.newHashSet();
        for (V v : c1) {
            if (c2.contains(v)) {
                intersection.add(v);
            }
        }
        return intersection;
    }

    /**
     * 以c1作为源求与c2的差集
     *
     * @param c1  c1
     * @param c2  c2
     * @param <V> 集合中的元素
     * @return 求差集后的集合
     */
    public static <V> Collection<V> complement(Collection<V> c1, Collection<V> c2) {
        Set<V> complement = Sets.newHashSet();
        for (V v : c1) {
            if (!c2.contains(v)) {
                complement.add(v);
            }
        }
        return complement;
    }
}
