package cc.allio.uno.core.util;

import cc.allio.uno.core.type.Types;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 与值相关的操作
 *
 * @author j.x
 * @since 1.1.7
 */
public final class Values {

    /**
     * expand values if value is a array
     *
     * @param coll the {@link Collection}
     * @param <V>  the value type
     * @return value array
     * @see #expand(Object[])
     */
    public static <V> V[] expandCollection(Collection<V> coll) {
        return (V[]) expand(coll.toArray());
    }

    /**
     * 返回数组
     *
     * @see #streamExpand(Object[])
     */
    @SafeVarargs
    public static <V> V[] expand(V... values) {
        return (V[]) streamExpand(values).toArray(Object[]::new);
    }

    /**
     * 返回{@link Collection}
     *
     * @see #streamExpand(Object[])
     */
    @SafeVarargs
    public static <V> Collection<V> collectionExpand(V... values) {
        return streamExpand(values).toList();
    }

    /**
     * 给定values可变参数，如果里面存在：
     * <ul>
     *     <li>数组</li>
     *     <li>{@link java.util.Collection}</li>
     * </ul>
     * 则把数据进行转换。返回流数据
     *
     * @param values values
     * @param <V>    值类型
     * @return value of stream
     */
    @SafeVarargs
    public static <V> Stream<V> streamExpand(V... values) {
        if (values == null) {
            return Stream.empty();
        }
        return Arrays.stream(values)
                .flatMap(v -> {
                    Class<V> valueClass = (Class<V>) v.getClass();
                    if (Types.isArray(valueClass)) {
                        Stream.Builder<V> builder = Stream.builder();
                        for (V o : ((V[]) v)) {
                            builder.add(o);
                        }
                        return builder.build();
                    } else if (Types.isCollection(valueClass)) {
                        Stream.Builder<V> builder = Stream.builder();
                        ((Collection<V>) v).forEach(builder::add);
                        return builder.build();
                    }
                    return Stream.of(v);
                });
    }


    /**
     * functional mapping getter to setter
     * <p>for example: {@code mapping(User::getName, User::setName)}</p>
     *
     * @param getter the getter function object
     * @param setter the setter function object
     * @param <T>    the value type
     */
    public static <T> void mapping(Supplier<T> getter, Consumer<T> setter) {
        setter.accept(getter.get());
    }
}
