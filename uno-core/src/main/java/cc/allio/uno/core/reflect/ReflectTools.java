package cc.allio.uno.core.reflect;

import cc.allio.uno.core.concurrent.LockContext;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.util.ObjectUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 反射相关的工具集合
 *
 * @author j.x
 * @date 2023/11/30 11:57
 * @since 1.0.0
 */
@Slf4j
public final class ReflectTools {

    // for obj class as key， value as generic type mapping
    static final Map<BinaryClassKey, Class<?>[]> OBJ_CLASS_MAPPING_GENERIC_TYPES = Maps.newConcurrentMap();
    static final Lock lock = new ReentrantLock();

    /**
     * @see #obtainGenericTypes(Class, Class)
     */
    public static Class<?> getGenericType(@NonNull Object obj, @NonNull Class<?> superClassOrInterface) {
        return getGenericType(obj, superClassOrInterface, 0);
    }

    /**
     * @see #obtainGenericTypes(Class, Class)
     */
    public static Class<?> getGenericType(@NonNull Object obj, @NonNull Class<?> superClassOrInterface, int index) {
        Class<?> clazz = obj.getClass();
        return getGenericTypeByIndex(clazz, superClassOrInterface, index);
    }

    /**
     * get specific generic type by index
     *
     * @param index determinate generic type index, ignore if indistinct
     * @return index generic type or null
     * @see #obtainGenericTypes(Class, Class)
     */
    public static Class<?> getGenericTypeByIndex(Class<?> objClass, @NonNull Class<?> superClassOrInterface, int index) {
        Class<?>[] genericTypes = obtainGenericTypes(objClass, superClassOrInterface);
        if (genericTypes.length - 1 >= index) {
            return genericTypes[index];
        }
        return null;
    }

    /**
     * 判断指定的范型是否存在
     *
     * @return true if contains
     * @throws NullPointerException obj or superClassOrInterface is null then throws
     * @see #obtainGenericTypes(Class, Class)
     */
    public static boolean containsGenericType(Class<?> objClass, Class<?> superClassOrInterface, Class<?> specificType) {
        Class<?> type = getGenericTypeBySpecificType(objClass, superClassOrInterface, specificType);
        return type != null;
    }

    /**
     * get specific generic type by index
     *
     * @return specific type or null
     * @see #obtainGenericTypes(Class, Class)
     */
    public static <T> Class<T> getGenericTypeBySpecificType(Class<?> objClass, @NonNull Class<?> superClassOrInterface, Class<T> specificType) {
        if (specificType == null) {
            return null;
        }
        Class<?>[] genericTypes = obtainGenericTypes(objClass, superClassOrInterface);
        for (Class<?> genericType : genericTypes) {
            if (specificType.isAssignableFrom(genericType)) {
                return (Class<T>) genericType;
            }
        }
        return null;
    }

    /**
     * 获取给定Class对象的，获取该Class对象的范型，并判定是否是给定类型superClassOrInterface的子类型。
     * <p>如果是则会从该类型从提取出范型，从给定的index中返回该范型。</p>
     *
     * @param objClass              给定类型Class对象
     * @param superClassOrInterface 需给定的父类或者接口Class对象，用此来判断从何处获取范型Class
     * @return specific generic type array
     * @throws NullPointerException obj or superClassOrInterface is null then throws
     */
    public static Class<?>[] obtainGenericTypes(Class<?> objClass, Class<?> superClassOrInterface) {
        if (objClass == null || superClassOrInterface == null) {
            throw Exceptions.unNull("obj class or superClassOrInterface is null");
        }
        return LockContext.lock(lock)
                .lockReturn(() -> {
                    BinaryClassKey binaryClassKey = BinaryClassKey.of(objClass, superClassOrInterface);
                    return OBJ_CLASS_MAPPING_GENERIC_TYPES.computeIfAbsent(binaryClassKey, key -> {
                        List<ParameterizedType> genericTypes = findGenericTypes(objClass);
                        // 默认取第一个匹配的
                        Type superType = genericTypes.stream()
                                .filter(parameterizedType -> {
                                    if (parameterizedType.getRawType() instanceof Class<?> rawClass) {
                                        return superClassOrInterface.isAssignableFrom(rawClass);
                                    }
                                    return false;
                                })
                                .findFirst()
                                .orElse(null);
                        if (superType instanceof ParameterizedType parameterizedSuperType) {
                            Type[] actualTypeArguments = parameterizedSuperType.getActualTypeArguments();
                            try {
                                return Arrays.stream(actualTypeArguments)
                                        // t must be class type
                                        .filter(t -> Class.class.isAssignableFrom(t.getClass()))
                                        .map(Class.class::cast)
                                        .toArray(Class[]::new);
                            } catch (Throwable ex) {
                                log.error("obtain generic type by obj class {} from super class {}, happen class cast error", objClass.getName(), superClassOrInterface.getName(), ex);
                                return new Class[0];
                            }
                        }
                        return new Class[0];
                    });

                })
                .unchecked();
    }

    /**
     * 获取某个Class对象执行的SuperClass或者Interface的范型数量长度
     *
     * @param objClass              给定类型Class对象
     * @param superClassOrInterface 需给定的父类或者接口Class对象，用此来判断从何处获取范型Class
     * @throws NullPointerException obj or superClassOrInterface is null then throws
     */
    public static int getGenericTypeLength(Class<?> objClass, Class<?> superClassOrInterface) {
        return obtainGenericTypes(objClass, superClassOrInterface).length;
    }

    /**
     * 从给定的Class对象中获取{@link ParameterizedType}类型，该方法将会递归查找所有范型父类以及范型接口
     *
     * @param clazz clazz
     * @return list of {@link ParameterizedType}
     */
    static List<ParameterizedType> findGenericTypes(Class<?> clazz) {
        List<ParameterizedType> types = Lists.newArrayList();
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass != null) {
            if (genericSuperclass instanceof Class<?> superClass && !Object.class.isAssignableFrom(superClass)) {
                types.addAll(findGenericTypes(superClass));
            }
            if (genericSuperclass instanceof ParameterizedType parameterizedSuperclass) {
                types.add(parameterizedSuperclass);
            }
        }
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        if (ObjectUtils.isNotEmpty(genericInterfaces)) {
            for (Type genericInterface : genericInterfaces) {
                if (genericInterface instanceof Class<?> superInterface) {
                    types.addAll(findGenericTypes(superInterface));
                }
                if (genericInterface instanceof ParameterizedType parameterizedType) {
                    types.add(parameterizedType);
                }
            }
        }
        return types;
    }

    @Data
    @AllArgsConstructor(staticName = "of")
    static class BinaryClassKey {
        private final Class<?> cls1;
        private final Class<?> cls2;
    }
}
