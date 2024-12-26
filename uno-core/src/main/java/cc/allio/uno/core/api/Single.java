package cc.allio.uno.core.api;

import cc.allio.uno.core.exception.Exceptions;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * description 'single' value transfer and filter.
 *
 * @author j.x
 * @since 0.1.1
 * @see Stream
 */
public interface Single<E> {

    /**
     * from {@link List} single
     *
     * @param list the list
     * @return the {@link Single} instance
     */
    static <E> Single<E> from(List<E> list) {
        if (list == null) {
            throw Exceptions.unNull("list");
        }
        return new InternalSingle<>(list.stream());
    }

    /**
     * from {@link Set} single
     *
     * @param set the set
     * @return the {@link Single} instance
     */
    static <E> Single<E> from(Set<E> set) {
        if (set == null) {
            throw Exceptions.unNull("set");
        }
        return new InternalSingle<>(set.stream());
    }

    /**
     * from {@link Map} single
     *
     * @param map the map
     * @return the {@link Single} instance
     */
    static <E, K, V> Single<E> from(Map<K, V> map) {
        if (map == null) {
            throw Exceptions.unNull("map");
        }
        return (Single<E>) new InternalSingle<>(map.entrySet().stream());
    }

    /**
     * from {@link Stream} single
     *
     * @param stream the stream
     * @return the {@link Single} instance
     */
    static <E> Single<E> from(Stream<E> stream) {
        if (stream == null) {
            throw Exceptions.unNull("map");
        }
        return new InternalSingle<>(stream);
    }

    /**
     * single elements trigger map operator
     *
     * @param func the map function
     * @return the {@link Single} instance
     */
    <V> Single<V> map(Function<E, V> func);

    /**
     * single elements trigger filter operator
     *
     * @param predicate the predicate function
     * @return the {@link Single} instance
     */
    Single<E> filter(Predicate<E> predicate);

    /**
     * foreach all elements, if one of through predicate, then return element of {@link Optional} instance, otherwise
     * return empty
     *
     * @param predicate the element predicate
     * @return the {@link Optional} instance
     */
    Optional<E> forReturn(Predicate<E> predicate);

    class InternalSingle<E> implements Single<E>, Self<InternalSingle<E>> {

        private Stream<E> stream;

        public InternalSingle(Stream<E> stream) {
            this.stream = stream;
        }

        @Override
        public <V> Single<V> map(Function<E, V> func) {
            return new InternalSingle<>(stream.map(func));
        }

        @Override
        public Single<E> filter(Predicate<E> predicate) {
            this.stream = stream.filter(predicate);
            return self();
        }

        @Override
        public Optional<E> forReturn(Predicate<E> predicate) {
            List<E> elements = stream.toList();
            for (E element : elements) {
                if (predicate.test(element)) {
                    return Optional.ofNullable(element);
                }
            }
            return Optional.empty();
        }
    }
}
