package cc.allio.uno.core.util;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.reflect.Instantiation;
import cc.allio.uno.core.reflect.InstantiationBuilder;
import cc.allio.uno.core.reflect.InstantiationFeature;
import cc.allio.uno.core.reflect.ReflectTools;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.function.Supplier;

/**
 * 与{@link Class}有关的工具集
 *
 * @author j.x
 */
@Slf4j
public class ClassUtils extends org.springframework.util.ClassUtils {

    /**
     * 获取class对象只有一个的泛型全限定类名
     *
     * @param clazz class对象
     * @param <T>   对象范型
     * @return 返回一个泛型对象或者空字符串
     * @throws NullPointerException 当class对象没有泛型时抛出异常
     * @see ReflectTools#getGenericTypeByIndex(Class, Class, int)
     * @deprecated
     */
    @Deprecated(since = "1.1.7")
    public static <T> String getSingleGenericClassName(@NonNull Class<? extends T> clazz) {
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType pt) {
            Type[] typeArguments = pt.getActualTypeArguments();
            if (typeArguments.length == 0) {
                throw new NullPointerException(String.format("Target %s can't find generic", clazz.getName()));
            }
            return typeArguments[0].getTypeName();
        }
        return "";
    }

    /**
     * 获取指定class中多个范型class name
     *
     * @param clazz clazz
     * @return 返回多个范型classname
     * @see ReflectTools#getGenericTypeByIndex(Class, Class, int)
     * @deprecated
     */
    @Deprecated(since = "1.1.7")
    public static String[] getMultiGenericClassName(Class<?> clazz) {
        if (clazz == null) {
            return new String[0];
        }
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType pt) {
            Type[] typeArguments = pt.getActualTypeArguments();
            if (typeArguments.length == 0) {
                throw new NullPointerException(String.format("Target %s can't find generic", clazz.getName()));
            }
            return Arrays.stream(typeArguments).map(Type::getTypeName).toArray(String[]::new);
        }
        return new String[0];
    }

    /**
     * 获取真实泛型类型（获取存在的第一个）
     *
     * @return 实体的Class对象，当实体类型不存在时默认返回Object类型
     * @throws ClassNotFoundException 当指定的全限定类名没有找到时抛出
     * @see ReflectTools#getGenericTypeByIndex(Class, Class, int)
     * @deprecated
     */
    @Deprecated(since = "1.1.7")
    public static Class<?> getSingleActualGenericType(Class<?> clazz) throws ClassNotFoundException {
        String expectClassname = getSingleGenericClassName(clazz);
        Object exceptType = AccessController.doPrivileged(
                (PrivilegedAction<Object>) () -> {
                    try {
                        return Class.forName(expectClassname, false, Thread.currentThread().getContextClassLoader());
                    } catch (ClassNotFoundException e) {
                        return e;
                    }
                });
        if (exceptType instanceof ClassNotFoundException notfound) {
            throw new ClassNotFoundException(notfound.getMessage());
        }
        return (Class<?>) exceptType;
    }

    /**
     * 获取真实泛型类型
     *
     * @param clazz 类型
     * @return 实体的Class对象，当实体类型不存在时默认返回Object类型
     * @see ReflectTools#getGenericTypeByIndex(Class, Class, int)
     * @deprecated
     */
    @Deprecated(since = "1.1.7")
    public static Class<?>[] getMultiActualGenericType(Class<?> clazz) throws ClassNotFoundException {
        List<Class<?>> actualTypes = Lists.newArrayList();
        String[] multiGenericClassName = getMultiGenericClassName(clazz);
        for (String gClassName : multiGenericClassName) {
            Object exceptType = AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                try {
                    return Class.forName(gClassName, false, Thread.currentThread().getContextClassLoader());
                } catch (ClassNotFoundException e) {
                    return e;
                }
            });
            if (exceptType instanceof ClassNotFoundException notfound) {
                throw new ClassNotFoundException(notfound.getMessage());
            }
            actualTypes.add((Class<?>) exceptType);
        }
        return actualTypes.toArray(new Class[0]);
    }

    /**
     * 获取指定字段上的泛型类型
     *
     * @param field 某个字段实例
     * @return 泛型数组
     * @see ReflectTools#getGenericTypeByIndex(Class, Class, int)
     * @deprecated
     */
    @Deprecated(since = "1.1.7")
    public static Type[] getFieldGenericType(Field field) {
        Type maybeType = field.getGenericType();
        if (maybeType instanceof ParameterizedType) {
            return ((ParameterizedType) maybeType).getActualTypeArguments();
        }
        return new Type[]{};
    }

    /**
     * 获取数组class的类型
     *
     * @param arrayClass 数组class
     * @return type for class
     */
    public static Class<?> getArrayClassType(Class<?> arrayClass) {
        if (arrayClass.isArray()) {
            String canonicalName = arrayClass.getName();
            // 形如[Lcc.allio.uno.core.type.TypeValueTest$ENUM;的形式
            String headType = canonicalName.substring(2);
            String fullyQualifiedName = headType.substring(0, headType.indexOf(StringPool.SEMICOLON));
            Object exceptType = AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                try {
                    return Class.forName(fullyQualifiedName, false, Thread.currentThread().getContextClassLoader());
                } catch (ClassNotFoundException e) {
                    return e;
                }
            });
            if (exceptType instanceof ClassNotFoundException) {
                return null;
            }
            return (Class<?>) exceptType;
        }
        return null;
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
                .toList()
                .toArray(new Class<?>[]{});
    }

    /**
     * 获取指定class对象中目标方法实例
     *
     * @param clazz            class
     * @param targetMethodName 目标方法名称
     * @param args             方法的参数
     * @return Method
     */
    public static Method getMethod(Class<?> clazz, String targetMethodName, Object... args) {
        Class<?>[] argsClass = objectToClass(args);
        return getMethod(clazz, targetMethodName, argsClass);
    }

    /**
     * 获取指定class对象中目标方法实例
     *
     * @param clazz            class
     * @param targetMethodName 目标方法名称
     * @param argsClass        方法的参数
     * @return Method
     */
    public static Method getMethod(Class<?> clazz, String targetMethodName, Class<?>[] argsClass) {
        Method method = null;
        try {
            method = clazz.getMethod(targetMethodName, argsClass);
        } catch (NoSuchMethodException e) {
            try {
                method = clazz.getDeclaredMethod(targetMethodName, argsClass);
            } catch (NoSuchMethodException e2) {
                throw Exceptions.unchecked(e2);
            }
        }
        if (method == null) {
            Set<Method> allMethods = new HashSet<>();
            allMethods.addAll(Arrays.asList(clazz.getMethods()));
            allMethods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
            List<Method> targetMethods =
                    allMethods.stream()
                            .filter(m -> targetMethodName.equals(m.getName()) && argsClass.length == m.getParameterCount())
                            .toList();
            if (CollectionUtils.isNotEmpty(targetMethods)) {
                method = targetMethods.get(0);
            }
        }
        return method;
    }

    /**
     * 设置构造器可访问
     *
     * @param constructor constructor
     */
    public static void setAccessible(Constructor<?> constructor) {
        setAccessible(constructor, true);
    }

    /**
     * 设置构造器可访问
     *
     * @param constructor constructor
     * @param flag        flag
     */
    public static void setAccessible(Constructor<?> constructor, boolean flag) {
        constructor.setAccessible(flag);
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
     * @deprecated 1.1.4版本后删除，使用{@link AnnotatedElementUtils#hasAnnotation(AnnotatedElement, Class)}
     */
    @Deprecated(since = "1.1.4", forRemoval = true)
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

    /**
     * 根据class对象批量创建对象，并排查哪些空的对象。
     *
     * @param classes classes
     * @return List
     */
    public static <T> List<T> newInstanceListExcludeNull(Class<? extends T>... classes) {
        if (ObjectUtils.isEmpty(classes)) {
            return Collections.emptyList();
        }
        return newInstanceListExcludeNull(Lists.newArrayList(classes));
    }

    /**
     * 根据class对象批量创建对象，并排查哪些空的对象。
     *
     * @param classes classes
     * @return List
     */
    public static <E> List<E> newInstanceListExcludeNull(List<Class<? extends E>> classes) {
        if (CollectionUtils.isEmpty(classes)) {
            return Collections.emptyList();
        }
        InstantiationBuilder<E> instantiationBuilder = InstantiationBuilder.builder();
        return instantiationBuilder.addMultiForInstanceClasses(classes.toArray(new Class[]{}))
                .setExcludeNull(true)
                .build()
                .create();
    }

    /**
     * 根据class对象批量创建对象
     *
     * @param classes      classes
     * @param errorDefault 错误参数
     * @return List
     */
    public static <E> List<E> newInstanceList(Collection<Class<? extends E>> classes, Supplier<? extends E> errorDefault) {
        if (CollectionUtils.isEmpty(classes)) {
            return Collections.emptyList();
        }
        InstantiationBuilder<E> instantiationBuilder = InstantiationBuilder.builder();
        return instantiationBuilder
                .addMultiForInstanceClasses(classes.toArray(new Class[]{}))
                .setIfErrorDefaultValue(errorDefault)
                .build()
                .create();
    }

    /**
     * 根据class对象批量创建对象
     *
     * @param classes  classes
     * @param features 实例化特性
     * @return List
     */
    public static <E> List<E> newInstanceList(Collection<Class<? extends E>> classes, InstantiationFeature<E>... features) {
        if (CollectionUtils.isEmpty(classes)) {
            return Collections.emptyList();
        }
        InstantiationBuilder<E> instantiationBuilder = InstantiationBuilder.builder();
        Instantiation<E> build = instantiationBuilder.addMultiForInstanceClasses(classes.toArray(new Class[]{})).build();
        for (InstantiationFeature<E> feature : features) {
            build.addFeature(feature);
        }
        return build.create();
    }

    /**
     * @see #newInstanceIfErrorDefault(Class, Object[], Supplier)
     */
    public static <T> T newInstance(@NonNull Class<T> clazz, Object... constructorParameters) {
        return newInstanceIfErrorDefault(clazz, constructorParameters, () -> null);
    }

    /**
     * 根据class对象创建新实例，当实例化时产生错误，则根据给定参数的值返回结果。<b>该方法创建实例不会抛出任何异常</b>
     *
     * @param clazz                 class，非空
     * @param constructorParameters 创建实例传递的构造参数
     * @param errorDefault          错误时给的默认值
     * @param <T>                   参数类型
     * @return instance or null
     */
    public static <T> T newInstanceIfErrorDefault(@NonNull Class<? extends T> clazz, Object[] constructorParameters, Supplier<T> errorDefault) {
        InstantiationBuilder<T> instantiationBuilder = InstantiationBuilder.builder();
        return instantiationBuilder.addOneInstanceClass(clazz)
                .setConstructorParameters(constructorParameters)
                .setIfErrorDefaultValue(errorDefault)
                .build()
                .createOne();
    }

}
