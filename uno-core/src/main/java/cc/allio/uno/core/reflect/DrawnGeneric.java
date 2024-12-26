package cc.allio.uno.core.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * drawn reflection type to actual generic type
 *
 * @author j.x
 * @since 1.1.8
 */
public interface DrawnGeneric<T> {

    /**
     * returns {@link ParameterizedType} of list base on reflect type.
     *
     * @param reflectType like as {@link Class}, {@link Method} ...
     * @return the {@link ParameterizedType} list
     */
    ParameterizedFinder drawn(T reflectType);
}
