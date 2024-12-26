package cc.allio.uno.core.reflect;

import cc.allio.uno.core.util.ObjectUtils;
import com.google.common.collect.Lists;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Drawn to class
 *
 * @author j.x
 * @since 1.1.8
 */
public class DrawnClass implements DrawnGeneric<Class<?>> {

    @Override
    public ParameterizedFinder drawn(Class<?> reflectType) {
        List<ParameterizedType> parameterizedTypes = drawnClass(reflectType);
        return new ParameterizedFinder(reflectType, parameterizedTypes);
    }

    /**
     * 从给定的Class对象中获取{@link ParameterizedType}类型，该方法将会递归查找所有范型父类以及范型接口
     *
     * @param reflectType the reflection class
     * @return the {@link ParameterizedType} list
     */
    List<ParameterizedType> drawnClass(Class<?> reflectType) {
        List<ParameterizedType> types = Lists.newArrayList();
        Type genericSuperclass = reflectType.getGenericSuperclass();
        if (genericSuperclass != null) {
            if (genericSuperclass instanceof Class<?> superClass && !Object.class.isAssignableFrom(superClass)) {
                List<ParameterizedType> superParameterizedType = drawnClass(superClass);
                types.addAll(superParameterizedType);
            }
            if (genericSuperclass instanceof ParameterizedType parameterizedSuperclass) {
                types.add(parameterizedSuperclass);
            }
        }
        Type[] genericInterfaces = reflectType.getGenericInterfaces();
        if (ObjectUtils.isNotEmpty(genericInterfaces)) {
            for (Type genericInterface : genericInterfaces) {
                if (genericInterface instanceof Class<?> superInterface) {
                    List<ParameterizedType> superParameterizedType = drawnClass(superInterface);
                    types.addAll(superParameterizedType);
                }
                if (genericInterface instanceof ParameterizedType parameterizedType) {
                    types.add(parameterizedType);
                }
            }
        }
        return types;
    }
}
