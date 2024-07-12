package cc.allio.uno.core.reflect;

import cc.allio.uno.core.util.Requires;
import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Stream;

/**
 * definition how to find {@link ParameterizedType}. and by rawType({@link Class}) and by index and else...
 *
 * @author j.x
 * @date 2024/4/4 17:58
 * @since 1.1.8
 */
@Getter
public class ParameterizedFinder {

    private final Object byReflectionType;
    private final List<ParameterizedType> parameterizedTypes;

    public ParameterizedFinder(Object byReflectionType, List<ParameterizedType> parameterizedTypes) {
        Requires.isNotNull(byReflectionType, "original reflection type");
        Requires.isNotNull(parameterizedTypes, "parameterized type");
        this.byReflectionType = byReflectionType;
        this.parameterizedTypes = parameterizedTypes;
    }

    /**
     * find first actual type
     *
     * @return the actual generic type, maybe null
     * @see #findAll()
     */
    public Class<?> findFirst() {
        Class<?>[] actualTypes = findAll();
        if (actualTypes.length > 0) {
            return actualTypes[0];
        }
        return null;
    }

    /**
     * base on {@link Class} raw type get first actual type
     *
     * @param rawType the raw type, not null.
     * @return the actual generic type, maybe null
     * @see #find(Class)
     */
    public Class<?> findFirst(Class<?> rawType) {
        Class<?>[] actualTypes = find(rawType);
        if (actualTypes.length > 0) {
            return actualTypes[0];
        }
        return null;
    }

    /**
     * base on {@link Class} raw type and actual type index get one actual type
     *
     * @param rawType the raw type, not nul
     * @param index   the index
     * @return the actual generic type, maybe is null
     */
    public Class<?> findIndex(Class<?> rawType, int index) {
        Class<?>[] actualTypes = find(rawType);
        if (index > actualTypes.length - 1) {
            throw new ArrayIndexOutOfBoundsException(String.format("actual types length is %s, but index is %s", actualTypes.length, index));
        }
        return actualTypes[index];
    }

    /**
     * base on {@link Class} raw type get actual generic type
     *
     * @param rawType the raw type, not null
     * @return not null actual generic type array
     */
    public Class<?>[] find(Class<?> rawType) {
        Requires.isNotNull(rawType, "rawType");
        return parameterizedTypes
                .stream()
                .filter(p -> {
                    if (p.getRawType() instanceof Class<?>) {
                        Class<?> rawClass = (Class<?>) p.getRawType();
                        return rawType.isAssignableFrom(rawClass);
                    }
                    return false;
                })
                .flatMap(p -> Stream.of(p.getActualTypeArguments()))
                .filter(t -> Class.class.isAssignableFrom(t.getClass()))
                .map(Class.class::cast)
                .toArray(Class[]::new);
    }

    /**
     * find all actual type, whatever raw type
     *
     * @return actual generic type array
     */
    public Class<?>[] findAll() {
        return parameterizedTypes
                .stream()
                .flatMap(p -> Stream.of(p.getActualTypeArguments()))
                .filter(t -> Class.class.isAssignableFrom(t.getClass()))
                .map(Class.class::cast)
                .toArray(Class[]::new);
    }
}
