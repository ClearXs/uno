package cc.allio.uno.core.reflect;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;

/**
 * Drawn to {@link Field}
 *
 * @author j.x
 * @date 2024/4/4 18:00
 * @since 1.1.8
 */
public class DrawnField implements DrawnGeneric<Field> {

    @Override
    public ParameterizedFinder drawn(Field reflectType) {
        Type genericType = reflectType.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            return new ParameterizedFinder(reflectType, Lists.newArrayList(parameterizedType));
        }
        return new ParameterizedFinder(reflectType, Collections.emptyList());
    }
}
