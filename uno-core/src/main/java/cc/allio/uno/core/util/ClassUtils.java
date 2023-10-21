package cc.allio.uno.core.util;

import cc.allio.uno.core.StringPool;
import com.google.common.collect.Lists;
import lombok.NonNull;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class的一些操作
 *
 * @author jw
 * @date 2021/12/5 11:00
 */
public class ClassUtils extends org.springframework.util.ClassUtils {

    private ClassUtils() {

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
     * 获取指定class中多个范型class name
     *
     * @param clazz clazz
     * @return 返回多个范型classname
     */
    public static String[] getMultiGenericClassName(Class<?> clazz) {
        if (clazz == null) {
            return new String[0];
        }
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
            if (typeArguments.length == 0) {
                throw new NullPointerException(String.format("Target %s can't find generic", clazz.getName()));
            }
            return Arrays.stream(typeArguments).map(Type::getTypeName).toArray(String[]::new);
        }
        return new String[0];
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
     * 获取真实泛型类型
     *
     * @param clazz 类型
     * @return 实体的Class对象，当实体类型不存在时默认返回Object类型
     */
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
            if (exceptType instanceof ClassNotFoundException) {
                throw new ClassNotFoundException(((ClassNotFoundException) exceptType).getMessage());
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
    @Deprecated
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
        InstantiationBuilder<E> instantiationBuilder = instantiationBuilder();
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
        InstantiationBuilder<E> instantiationBuilder = instantiationBuilder();
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
    public static <E> List<E> newInstanceList(Collection<Class<? extends E>> classes, Feature<E>... features) {
        if (CollectionUtils.isEmpty(classes)) {
            return Collections.emptyList();
        }
        InstantiationBuilder<E> instantiationBuilder = instantiationBuilder();
        Instantiation<E> build = instantiationBuilder.addMultiForInstanceClasses(classes.toArray(new Class[]{})).build();
        for (Feature<E> feature : features) {
            build.addFeature(feature);
        }
        return build.create();
    }

    /**
     * 根据class对象创建新实例
     *
     * @param clazz                 clazz，非空
     * @param constructorParameters 创建实例传递的构造参数
     * @param <T>                   参数类型
     * @return 实例结果 or null
     */
    public static <T> T newInstance(@NonNull Class<T> clazz, Object... constructorParameters) {
        return newInstanceIfErrorDefault(clazz, constructorParameters, () -> null);
    }

    /**
     * 根据class对象创建新实例，当实例化时产生错误，则根据给定参数的值返回结果
     *
     * @param clazz                 class，非空
     * @param constructorParameters 创建实例传递的构造参数
     * @param errorDefault          错误时给的默认值
     * @param <T>                   参数类型
     * @return 实例
     */
    public static <T> T newInstanceIfErrorDefault(@NonNull Class<? extends T> clazz, Object[] constructorParameters, Supplier<T> errorDefault) {
        InstantiationBuilder<T> instantiationBuilder = instantiationBuilder();
        return instantiationBuilder.addOneInstanceClass(clazz)
                .setConstructorParameters(constructorParameters)
                .setIfErrorDefaultValue(errorDefault)
                .build()
                .createOne();
    }

    /**
     * 创建instantiationBuilder对象
     *
     * @param <I> 对象反省
     * @return InstantiationBuilder实例
     */
    public static <I> InstantiationBuilder<I> instantiationBuilder() {
        return new InstantiationBuilder<>();
    }

    /**
     * Instantiation Builder
     */
    public static class InstantiationBuilder<I> {
        private Class<? extends I>[] waitForInstanceClasses;
        private Object[] constructorParameters;
        private Supplier<? extends I> ifErrorDefaultValue;
        private boolean isExcludeNull;

        InstantiationBuilder() {
        }

        /**
         * 添加一个待实例化的Class对象
         *
         * @param oneInstanceClass oneInstanceClass
         * @return InstantiationBuilder
         */
        public InstantiationBuilder<I> addOneInstanceClass(Class<? extends I> oneInstanceClass) {
            if (waitForInstanceClasses != null) {
                Class<? extends I>[] newInstanceClasses = new Class[waitForInstanceClasses.length + 1];
                System.arraycopy(waitForInstanceClasses, 0, newInstanceClasses, waitForInstanceClasses.length, waitForInstanceClasses.length);
                newInstanceClasses[waitForInstanceClasses.length + 1] = oneInstanceClass;
                this.waitForInstanceClasses = newInstanceClasses;
            } else {
                this.waitForInstanceClasses = new Class[]{oneInstanceClass};
            }
            return this;
        }

        /**
         * 添加多个待实例化的Class对象
         *
         * @param multiInstanceClasses waitForInstanceClasses
         * @return InstantiationBuilder
         */
        public InstantiationBuilder<I> addMultiForInstanceClasses(Class<? extends I>[] multiInstanceClasses) {
            if (waitForInstanceClasses != null) {
                Class<? extends I>[] newInstanceClasses = new Class[waitForInstanceClasses.length + multiInstanceClasses.length];
                System.arraycopy(waitForInstanceClasses, 0, newInstanceClasses, waitForInstanceClasses.length, waitForInstanceClasses.length);
                System.arraycopy(multiInstanceClasses, 0, newInstanceClasses, waitForInstanceClasses.length, multiInstanceClasses.length);
                this.waitForInstanceClasses = newInstanceClasses;
            } else {
                this.waitForInstanceClasses = multiInstanceClasses;
            }
            return this;
        }

        public InstantiationBuilder<I> setConstructorParameters(Object[] constructorParameters) {
            this.constructorParameters = constructorParameters;
            return this;
        }

        public InstantiationBuilder<I> setIfErrorDefaultValue(Supplier<? extends I> ifErrorDefaultValue) {
            this.ifErrorDefaultValue = ifErrorDefaultValue;
            return this;
        }

        public InstantiationBuilder<I> setExcludeNull(boolean excludeNull) {
            isExcludeNull = excludeNull;
            return this;
        }

        public Instantiation<I> build() {
            if (ObjectUtils.isEmpty(waitForInstanceClasses)) {
                throw new UnsupportedOperationException("At least one classes");
            }
            return new Instantiation<>(waitForInstanceClasses, constructorParameters, ifErrorDefaultValue, isExcludeNull);
        }
    }

    /**
     * 实例化。
     * <p>
     * 允许实例化时：
     *     <ul>
     *         <li>对需要进行实例化的对象进行排序</li>
     *         <li>实例化失败时提供的默认值</li>
     *         <li>实例化时提供回调</li>
     *         <li>去除重复</li>
     *         <li>去除null</li>
     *     </ul>
     * </p>
     * <p>
     *     当实例化的class对象为<b>抽象类</b>、<b>接口</b>时它的结果一定为null
     * </p>
     *
     * @see InstantiationBuilder
     */
    public static class Instantiation<I> {

        // 进行实例化的Class对象
        private Class<? extends I>[] waitForInstanceClasses;
        // 构造器参数集合
        private final Object[] constructorParameters;
        // 发生错误时实例化异常值
        private final Supplier<? extends I> ifErrorDefaultValue;
        // 实例化后是否排序异常值
        private final boolean excludeNull;
        // 实例化时拓展的拓展
        private final List<Feature<I>> features;
        public final Supplier<? extends I> nullValue = () -> null;

        Instantiation(Class<? extends I>[] waitForInstanceClasses,
                      Object[] constructorParameters,
                      Supplier<? extends I> ifErrorDefaultValue,
                      boolean excludeNull) {
            this.waitForInstanceClasses = waitForInstanceClasses;
            this.constructorParameters = constructorParameters;
            this.ifErrorDefaultValue = ifErrorDefaultValue;
            this.excludeNull = excludeNull;
            this.features = Lists.newArrayList();
        }

        public void rewriteInstanceClasses(Class<? extends I>[] waitForInstanceClasses) {
            this.waitForInstanceClasses = waitForInstanceClasses;
        }

        public Class<? extends I>[] getWaitForInstanceClasses() {
            return waitForInstanceClasses;
        }

        public Object[] getParameters() {
            return constructorParameters;
        }

        public Supplier<? extends I> getIfErrorDefaultValue() {
            return ifErrorDefaultValue;
        }

        public boolean isExcludeNull() {
            return excludeNull;
        }

        /**
         * 添加实例化特性
         *
         * @param feature feature
         */
        public void addFeature(Feature<I> feature) {
            features.add(feature);
        }

        /**
         * 只创建一个实例
         *
         * @return Object
         */
        public I createOne() {
            List<I> objects = null;
            try {
                objects = create();
            } catch (Throwable e) {
                return nullValue.get();
            }
            if (ObjectUtils.isEmpty(objects)) {
                return ifErrorDefaultValue.get();
            }
            return objects.get(0);
        }

        /**
         * 批量创建
         *
         * @return List
         */
        public List<I> create() {
            if (ObjectUtils.isNotEmpty(features)) {
                for (Feature<I> feature : features) {
                    feature.execute(this);
                }
            }
            List<I> instances = Lists.newArrayList();
            // 空参数创建
            if (ObjectUtils.isEmpty(constructorParameters)) {
                for (Class<? extends I> clazz : waitForInstanceClasses) {
                    I ins = newEmptyParametersInstance(clazz);
                    instances.add(ins);
                }
            } else {
                for (Class<? extends I> clazz : waitForInstanceClasses) {
                    I ins = newInstance(clazz);
                    instances.add(ins);
                }
            }
            // 排除实例为null
            if (excludeNull) {
                List<I> nonNullInstances = Lists.newArrayList();
                for (I instance : instances) {
                    if (ObjectUtils.isNotEmpty(instance)) {
                        nonNullInstances.add(instance);
                    }
                }
                return nonNullInstances;
            }
            return instances;
        }

        /**
         * 创建实例，如果抛出异常则以无参构造器创建
         *
         * @param clazz clazz
         * @return instance or null
         */
        private I newInstance(Class<? extends I> clazz) {
            Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterCount() == constructorParameters.length) {
                    try {
                        setAccessible(constructor);
                        return (I) constructor.newInstance(constructorParameters);
                    } catch (Throwable ex) {
                        // 尝试以空参数进行创建
                        return newEmptyParametersInstance(clazz);
                    }
                }
            }
            return newEmptyParametersInstance(clazz);
        }

        /**
         * 创建参数为空的实例。如果抛出以
         *
         * @return instance or null
         */
        private I newEmptyParametersInstance(Class<? extends I> clazz) {
            // 验证参数
            Supplier<? extends I> exceptionValue = ifErrorDefaultValue == null ? nullValue : ifErrorDefaultValue;
            try {
                return clazz.newInstance();
            } catch (Throwable ex) {
                return exceptionValue.get();
            }
        }
    }

    /**
     * 特性策略
     */
    public interface Feature<I> {

        /**
         * 执行对应特性策略
         *
         * @param instantiation 实例化参数
         */
        void execute(Instantiation<I> instantiation);
    }

    /**
     * 排序策略
     */
    public static class SortFeature<I> implements Feature<I> {

        @Override
        public void execute(Instantiation<I> instantiation) {
            Class<? extends I>[] waitForInstanceClasses = instantiation.getWaitForInstanceClasses();
            AnnotationAwareOrderComparator.sort(waitForInstanceClasses);
            instantiation.rewriteInstanceClasses(waitForInstanceClasses);
        }
    }

    /**
     * 回调策略
     */
    public static class CallbackFeature<I> implements Feature<I> {

        private final Consumer<Instantiation<I>> consumer;

        public CallbackFeature(Consumer<Instantiation<I>> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void execute(Instantiation<I> instantiation) {
            consumer.accept(instantiation);
        }
    }

    /**
     * 去重策略（简单的stream distinct进行去重）
     */
    public static class DeDuplicationFeature<I> implements Feature<I> {

        @Override
        public void execute(Instantiation<I> instantiation) {
            Class<? extends I>[] waitForInstanceClasses = instantiation.getWaitForInstanceClasses();
            instantiation.rewriteInstanceClasses(
                    Stream.of(waitForInstanceClasses).distinct().toArray(Class[]::new)
            );
        }
    }
}
