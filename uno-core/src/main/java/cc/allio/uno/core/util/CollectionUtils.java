package cc.allio.uno.core.util;

import java.util.*;

/**
 * 集合常用工具方法
 *
 * @author jw
 * @date 2021/12/5 11:02
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
     * 根据对象的equals过滤数据
     */
    @SafeVarargs
    public static <V> Collection<V> filter(Collection<V> collection, V... filters) throws IllegalAccessException, InstantiationException {
        Collection<V> copyCollection = collection.getClass().newInstance();
        copyCollection.addAll(collection);
        Collection<V> filterCollection = collection.getClass().newInstance();
        for (V filter : filters) {
            if (collection.contains(filter)) {
                filterCollection.add(filter);
            }
        }
        copyCollection.removeAll(filterCollection);
        return copyCollection;
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
     */
    public static <V> boolean union(Collection<V> source, Collection<V> target) {
        if (isEmpty(target) || isEmpty(source)) {
            return false;
        }
        boolean changed = false;
        for (V o : target) {
            boolean contains = source.contains(o);
            if (!contains) {
                source.add(o);
                changed = true;
            }
        }
        return changed;
    }

}
