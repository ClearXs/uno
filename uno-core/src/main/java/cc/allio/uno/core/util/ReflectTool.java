package cc.allio.uno.core.util;

import lombok.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.Stream;

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
     * @throws NullPointerException obj or superClassOrInterface is null then throws
     * @see #getGenericType(Object, Class, int)
     */
    public static Class<?> getGenericType(@NonNull Object obj, @NonNull Class<?> superClassOrInterface) {
        return getGenericType(obj, superClassOrInterface, 0);
    }

    /**
     * 获取某个实体的泛型。<b>给定{@param obj}必须是实现的子类，如果是lambda则不能无法获取</b>
     *
     * @param obj                   实体
     * @param superClassOrInterface 声明的类或接口
     * @param index                 声明泛型的顺序
     * @return class or null
     * @throws NullPointerException obj or superClassOrInterface is null then throws
     */
    public static Class<?> getGenericType(@NonNull Object obj, @NonNull Class<?> superClassOrInterface, int index) {
        Class<?> clazz = obj.getClass();

        Stream.Builder<Type> genericTypeBuilder = Stream.builder();

        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass != null) {
            genericTypeBuilder.add(genericSuperclass);
        }

        Type[] genericInterfaces = clazz.getGenericInterfaces();

        if (ObjectUtils.isNotEmpty(genericInterfaces)) {
            for (Type genericInterface : genericInterfaces) {
                genericTypeBuilder.add(genericInterface);
            }
        }

        Type superType = genericTypeBuilder.build()
                .filter(type -> {
                    if (type instanceof ParameterizedType parameterizedType
                            && parameterizedType.getRawType() instanceof Class<?> rawClass) {
                        return superClassOrInterface.isAssignableFrom(rawClass);
                    }
                    return false;
                })
                .findFirst()
                .orElse(null);

        if (superType != null) {
            Type[] actualTypeArguments = ((ParameterizedType) superType).getActualTypeArguments();
            if (actualTypeArguments.length > index) {
                return (Class<?>) actualTypeArguments[index];
            }
        }
        return null;
    }
}
