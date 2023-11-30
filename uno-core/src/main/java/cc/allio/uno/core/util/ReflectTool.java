package cc.allio.uno.core.util;

import lombok.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * 反射相关的工具集合
 *
 * @author j.x
 * @date 2023/11/30 11:57
 * @since 1.0.0
 */
public class ReflectTool {

    private ReflectTool() {
    }


    /**
     * 获取某个实体的泛型
     *
     * @throws NullPointerException obj or superInterface is null then throws
     * @see #getGenericType(Object, Class, int)
     */
    public static Class<?> getGenericType(@NonNull Object obj, @NonNull Class<?> superInterface) {
        return getGenericType(obj, superInterface, 0);
    }

    /**
     * 获取某个实体的泛型
     *
     * @param obj   实体
     * @param index 声明泛型的顺序
     * @return class or null
     * @throws NullPointerException obj or superInterface is null then throws
     */
    public static Class<?> getGenericType(@NonNull Object obj, @NonNull Class<?> superInterface, int index) {
        Class<?> clazz = obj.getClass();
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        Type superType =
                Arrays.stream(genericInterfaces)
                        .filter(type -> {
                            if (type instanceof ParameterizedType parameterizedType) {
                                return parameterizedType.getRawType().equals(superInterface);
                            } else {
                                return false;
                            }
                        })
                        .findFirst()
                        .orElse(null);

        if (superType != null) {
            Type[] actualTypeArguments = ((ParameterizedType) superType).getActualTypeArguments();
            if (actualTypeArguments.length > index) {
                return (Class<?>) actualTypeArguments[index];
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}