package cc.allio.uno.core.util.comparator;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;

/**
 * 比较器生成器
 *
 * @author jiangwei
 * @date 2022/7/10 16:13
 * @since 1.0
 */
public class ComparatorGenerator {

    /**
     * 更具类型生成Comparator
     *
     * @param clazz class类型
     * @param <T>   范型
     * @return Comparator实例
     */
    public static <T> Comparator<T> generator(Class<T> clazz) {
        if (clazz.isAssignableFrom(Integer.class)) {
            return (Comparator<T>) new IntegerComparator();
        } else if (clazz.isAssignableFrom(Byte.class)) {
            return (Comparator<T>) new ByteComparator();
        } else if (clazz.isAssignableFrom(Boolean.class)) {
            return (Comparator<T>) new BooleanComparator();
        } else if (clazz.isAssignableFrom(Long.class)) {
            return (Comparator<T>) new LongComparator();
        } else if (clazz.isAssignableFrom(Short.class)) {
            return (Comparator<T>) new ShortComparator();
        } else if (clazz.isAssignableFrom(String.class)) {
            return (Comparator<T>) new StringComparator();
        } else if (clazz.isAssignableFrom(BigDecimal.class)) {
            return (Comparator<T>) new BigDecimalComparator();
        } else if (clazz.isAssignableFrom(Collection.class)) {
            return (Comparator<T>) new CollectionComparator<>();
        } else if (clazz.isAssignableFrom(Float.class)) {
            return (Comparator<T>) new FloatComparator();
        }
        return (Comparator<T>) new ObjectComparator();
    }
}
