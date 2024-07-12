package cc.allio.uno.core.reflect;

import com.google.common.collect.Streams;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Drawn to method
 *
 * @author j.x
 * @date 2024/4/4 18:00
 * @since 1.1.8
 */
public class DrawnMethod implements DrawnGeneric<Method> {

    @Override
    public ParameterizedFinder drawn(Method reflectType) {
        Type genericReturnType = reflectType.getGenericReturnType();
        Type[] genericExceptionTypes = reflectType.getGenericExceptionTypes();
        Type[] genericParameterTypes = reflectType.getGenericParameterTypes();
        List<ParameterizedType> parameterizedTypes =
                Streams.concat(
                                Stream.of(genericReturnType),
                                Stream.of(genericExceptionTypes),
                                Stream.of(genericParameterTypes)
                        )
                        .filter(type -> ParameterizedType.class.isAssignableFrom(type.getClass()))
                        .map(ParameterizedType.class::cast)
                        .collect(Collectors.toList());
        return new ParameterizedFinder(reflectType, parameterizedTypes);
    }
}
