package cc.allio.uno.core.util;

import org.springframework.util.ClassUtils;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class的一些操作
 *
 * @author jw
 * @date 2021/12/5 11:00
 */
public class ClassUtil extends ClassUtils {

    private ClassUtil() {

    }

    /**
     * 获取class对象只有一个的泛型全限定类名
     *
     * @param clazz class对象
     * @param <T>   对象范型
     * @return 返回一个泛型对象或者空字符串
     * @throws NullPointerException 当class对象没有泛型时抛出异常
     */
    public static <T> String getSingleGenericClassName(Class<? extends T> clazz) {
        if (clazz == null) {
            return "";
        }
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
            if (typeArguments.length == 0) {
                throw new NullPointerException(String.format("Target %s can't find generic", clazz.getName()));
            }
            return typeArguments[0].getTypeName();
        }
        return "";
    }

    /**
     * 获取目标Class对象所有的泛型数组
     *
     * @param clazz 待转换的Class对象
     * @param <T>   对象范型
     * @return 获取到的字符串数组
     */
    public static <T> String[] getAllGenericClassNames(Class<? extends T> clazz) {
        if (clazz == null) {
            return new String[0];
        }
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
            if (typeArguments.length == 0) {
                throw new NullPointerException(String.format("target %s can't find generic", clazz.getName()));
            }

            return Arrays.stream(typeArguments)
                    .flatMap(argument -> {
                        if (argument instanceof TypeVariableImpl) {
                            return Stream.of(((TypeVariableImpl<?>) argument).getBounds());
                        }
                        return Stream.of(argument);
                    })
                    .map(Type::getTypeName)
                    .collect(Collectors.toList())
                    .toArray(new String[]{});
        }
        return new String[0];
    }

    /**
     * 获取真实泛型类型（获取存在的第一个）
     *
     * @return 实体的Class对象，当实体类型不存在时默认返回Object类型
     * @throws ClassNotFoundException 当指定的全限定类名没有找到时抛出
     */
    public static Class<?> getSingleActualGenericType(Class<?> clazz) throws ClassNotFoundException {
        String expectClassname = getSingleGenericClassName(clazz);
        Object exceptType = AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            try {
                return Class.forName(expectClassname, false, Thread.currentThread().getContextClassLoader());
            } catch (ClassNotFoundException e) {
                return e;
            }
        });
        if (exceptType instanceof ClassNotFoundException) {
            throw new ClassNotFoundException(((ClassNotFoundException) exceptType).getMessage());
        }
        return (Class<?>) exceptType;
    }

    /**
     * 获取指定字段上的泛型类型
     *
     * @param field 某个字段实例
     * @return 泛型数组
     */
    public static Type[] getFieldGenericType(Field field) {
        Type maybeType = field.getGenericType();
        if (maybeType instanceof ParameterizedType) {
            return ((ParameterizedType) maybeType).getActualTypeArguments();
        }
        return new Type[]{};
    }


    /**
     * {@code Object}类型的参数转为Class对象
     *
     * @param args 参数类型
     * @return class[] 对象
     */
    public static Class<?>[] objectToClass(Object... args) {
        return Arrays.stream(args)
                .map(Object::getClass)
                .collect(Collectors.toList())
                .toArray(new Class<?>[]{});
    }

    public static Method getMethod(Class<?> clazz, String targetMethodName, Object... args) {
        Class<?>[] argsClass = objectToClass(args);
        return getMethod(clazz, targetMethodName, argsClass);
    }

    public static Method getMethod(Class<?> clazz, String targetMethodName, Class<?>[] argsClass) {
        Method method = null;
        try {
            method = clazz.getMethod(targetMethodName, argsClass);
        } catch (NoSuchMethodException e) {
            try {
                method = clazz.getDeclaredMethod(targetMethodName, argsClass);
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            }
        }
        if (method == null) {
            Set<Method> allMethods = new HashSet<>();
            allMethods.addAll(Arrays.asList(clazz.getMethods()));
            allMethods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
            List<Method> targetMethods = allMethods.stream()
                    .filter(m -> targetMethodName.equals(m.getName()) && argsClass.length == m.getParameterCount())
                    .collect(Collectors.toList());
            if (Collections.isNotEmpty(targetMethods)) {
                method = targetMethods.get(0);
            }
        }
        return method;
    }

    /**
     * 设置方法可访问
     *
     * @param method 方法实例
     */
    public static void setAccessible(Method method) {
        setAccessible(method, true);
    }

    /**
     * 设置方法可访问
     *
     * @param method 方法实例
     * @param flag   标志
     */
    public static void setAccessible(Method method, boolean flag) {
        method.setAccessible(flag);
    }

    /**
     * 设置字段可访问
     *
     * @param field 字段实例
     */
    public static void setAccessible(Field field) {
        setAccessible(field, true);
    }

    /**
     * 设置字段可访问
     *
     * @param field 字段实例
     * @param flag  标志
     */
    public static void setAccessible(Field field, boolean flag) {
        field.setAccessible(flag);
    }

    /**
     * 判断是否包含某个注解类型
     *
     * @param instanceClass  某个对象实例
     * @param annotationType 具体的注解类型
     * @param <T>            需要判断的范型
     * @return true 存在 false 不存在
     */
    public static <T extends Annotation> boolean containsAnnotation(Class<?> instanceClass, Class<T> annotationType) {
        try {
            getAnnotation(instanceClass, annotationType);
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * 获取对象上某个注解的实例
     *
     * @param instanceClass  实例对象
     * @param annotationType 注解类型
     * @param <T>            注解的范型
     * @return 注解实例对象或者Null
     * @throws NullPointerException 详情看{@link Class#getDeclaredAnnotation(Class)}
     */
    public static <T extends Annotation> T getAnnotation(Class<?> instanceClass, Class<T> annotationType) {
        return instanceClass.getDeclaredAnnotation(annotationType);
    }
}
