package cc.allio.uno.core.util;

import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.util.convert.UnoConverter;
import cc.allio.uno.core.bean.BeanCopier;
import cc.allio.uno.core.bean.BeanMap;
import cc.allio.uno.core.bean.BeanProperty;
import cc.allio.uno.core.type.Types;
import com.google.common.collect.Maps;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.Nullable;

import java.util.*;
import java.util.Collections;

/**
 * Bean实用的工具方法
 *
 * @author j.x
 * @date 2022/1/29 16:05
 * @since 1.0
 */
@Slf4j
public final class BeanUtils extends org.springframework.beans.BeanUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    public BeanUtils() {
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        BeanUtils.context = context;
    }

    public static <T> T getBean(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        return context.getBean(clazz);
    }

    public static <T> T getBean(String beanId) {
        if (beanId == null) {
            return null;
        }
        return (T) context.getBean(beanId);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        if (null == beanName || "".equals(beanName.trim())) {
            return null;
        }
        if (clazz == null) {
            return null;
        }
        return context.getBean(beanName, clazz);
    }

    public static <T> T getBeanOrDefault(Class<T> clazz, T defaultValue) {
        if (clazz == null) {
            return defaultValue;
        }
        try {
            return context.getBean(clazz);
        } catch (NullPointerException | NoSuchBeanDefinitionException e) {
            log.error("getValue bean failed, return default value");
            return defaultValue;
        }
    }

    public static ApplicationContext getContext() {
        if (context == null) {
            return null;
        }
        return context;
    }

    public static Optional<ApplicationContext> getOptionContext() {
        return Optional.ofNullable(getContext());
    }

    public static void publishEvent(ApplicationEvent event) {
        if (context == null) {
            return;
        }
        try {
            context.publishEvent(event);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    /**
     * 实例化对象
     *
     * @param clazz 类
     * @param <T>   泛型标记
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> clazz) {
        return (T) instantiateClass(clazz);
    }

    /**
     * 实例化对象
     *
     * @param clazzStr 类名
     * @param <T>      泛型标记
     * @return 对象
     */
    public static <T> T newInstance(String clazzStr) {
        try {
            Class<?> clazz = ClassUtils.forName(clazzStr, null);
            return newInstance(clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据名称获取配置
     *
     * @param name 配置名称
     * @return 获取的String类型的配置
     * @throws NullPointerException {@link ApplicationContext}对象为null抛出
     * @see org.springframework.core.env.Environment#getProperty(String)
     */
    public static String getProperties(String name) {
        return context.getEnvironment().getProperty(name);
    }


    /**
     * 根据名称获取期望的配置，
     *
     * @param name         配置名称
     * @param defaultValue 默认值
     * @return 获取的String类型的配置
     * @throws NullPointerException {@link ApplicationContext}对象为null抛出
     */
    public static String getProperties(String name, String defaultValue) {
        return getProperties(name, String.class, defaultValue);
    }

    /**
     * 根据名称获取期望的配置，如果没有则返回默认值
     *
     * @param name       配置名称
     * @param expectType 期望类型的Class对象
     * @param <T>        期望的类型
     * @return 获取的期望类型的配置
     * @throws NullPointerException {@link ApplicationContext}对象为null抛出
     * @see org.springframework.core.env.Environment#getProperty(String, Class)
     */
    public static <T> T getProperties(String name, Class<T> expectType) {
        return getProperties(name, expectType, null);
    }

    /**
     * 根据名称获取期望的配置，如果没有则返回默认值
     *
     * @param name         配置名称
     * @param expectType   期望类型的Class对象
     * @param defaultValue 默认值
     * @param <T>          期望的类型
     * @return 获取的期望类型的配置
     * @throws NullPointerException {@link ApplicationContext}对象为null抛出
     */
    public static <T> T getProperties(String name, Class<T> expectType, T defaultValue) {
        return context.getEnvironment().getProperty(name, expectType, defaultValue);
    }

    /**
     * 获取Bean的属性, 支持 propertyName 多级 ：test.user.name
     *
     * @param bean         bean
     * @param propertyName 属性名
     * @return 属性值
     */
    @Nullable
    public static Object getProperty(@Nullable Object bean, String propertyName) {
        if (bean == null) {
            return null;
        }
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
        return beanWrapper.getPropertyValue(propertyName);
    }

    /**
     * 设置Bean属性, 支持 propertyName 多级 ：test.user.name
     *
     * @param bean         bean
     * @param propertyName 属性名
     * @param value        属性值
     */
    public static void setProperty(Object bean, String propertyName, Object value) {
        Objects.requireNonNull(bean, "bean Could not null");
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
        beanWrapper.setPropertyValue(propertyName, value);
    }

    /**
     * 深复制
     *
     * <p>
     * 支持 map bean
     * </p>
     *
     * @param source 源对象
     * @param <T>    泛型标记
     * @return T
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public static <T> T clone(@Nullable T source) {
        if (source == null) {
            return null;
        }
        return (T) BeanUtils.copy(source, source.getClass());
    }

    /**
     * copy 对象属性，默认不使用Convert
     *
     * <p>
     * 支持 map bean copy
     * </p>
     * <note>
     * <b>
     * 复制的目标对象的CLass不能是匿名类
     * </b>
     * </note>
     *
     * @param source 源对象
     * @param clazz  类名
     * @param <T>    泛型标记
     * @return T
     * @throws NullPointerException 当source为null时抛出
     */
    @Nullable
    public static <T> T copy(@Nullable Object source, Class<T> clazz) {
        if (source == null) {
            throw Exceptions.unNull("source is empty");
        }
        if (Types.isMap(clazz)) {
            return (T) Maps.newHashMap((Map) source);
        }
        return BeanUtils.copy(source, source.getClass(), clazz);
    }

    /**
     * copy 对象属性，默认不使用Convert
     *
     * <p>
     * 支持 map bean copy
     * </p>
     *
     * @param source      源对象
     * @param sourceClazz 源类型
     * @param targetClazz 转换成的类型
     * @param <T>         泛型标记
     * @return T
     * @throws NullPointerException 当source为null时抛出
     */
    @Nullable
    public static <T> @NonNull T copy(@Nullable Object source, Class sourceClazz, Class<T> targetClazz) {
        if (source == null) {
            throw Exceptions.unNull("source is empty");
        }
        BeanCopier copier = BeanCopier.create(sourceClazz, targetClazz, false);
        T to = newInstance(targetClazz);
        copier.copy(source, to, null);
        return to;
    }

    /**
     * copy 列表对象，默认不使用Convert
     *
     * <p>
     * 支持 map bean copy
     * </p>
     *
     * @param sourceList  源列表
     * @param targetClazz 转换成的类型
     * @param <T>         泛型标记
     * @return T
     */
    public static <T> List<T> copy(@Nullable Collection<?> sourceList, Class<T> targetClazz) {
        if (sourceList == null || sourceList.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        List<T> outList = new ArrayList<>(sourceList.size());
        Class<?> sourceClazz = null;
        for (Object source : sourceList) {
            if (source == null) {
                continue;
            }
            if (sourceClazz == null) {
                sourceClazz = source.getClass();
            }
            T bean = BeanUtils.copy(source, sourceClazz, targetClazz);
            outList.add(bean);
        }
        return outList;
    }

    /**
     * 拷贝对象
     *
     * <p>
     * 支持 map bean copy
     * </p>
     *
     * @param source     源对象
     * @param targetBean 需要赋值的对象
     */
    public static void copy(@Nullable Object source, @Nullable Object targetBean) {
        if (source == null || targetBean == null) {
            return;
        }
        BeanCopier copier = BeanCopier
                .create(source.getClass(), targetBean.getClass(), false);

        copier.copy(source, targetBean, null);
    }

    /**
     * 拷贝对象，source 属性做 null 判断，Map 不支持，map 会做 instanceof 判断，不会
     *
     * <p>
     * 支持 bean copy
     * </p>
     *
     * @param source     源对象
     * @param targetBean 需要赋值的对象
     */
    public static void copyNonNull(@Nullable Object source, @Nullable Object targetBean) {
        if (source == null || targetBean == null) {
            return;
        }
        BeanCopier copier = BeanCopier
                .create(source.getClass(), targetBean.getClass(), false, true);

        copier.copy(source, targetBean, null);
    }

    /**
     * 拷贝对象并对不同类型属性进行转换
     *
     * <p>
     * 支持 map bean copy
     * </p>
     *
     * @param source      源对象
     * @param targetClazz 转换成的类
     * @param <T>         泛型标记
     * @return T
     */
    @Nullable
    public static <T> T copyWithConvert(@Nullable Object source, Class<T> targetClazz) {
        if (source == null) {
            return null;
        }
        return BeanUtils.copyWithConvert(source, source.getClass(), targetClazz);
    }

    /**
     * 拷贝对象并对不同类型属性进行转换
     *
     * <p>
     * 支持 map bean copy
     * </p>
     *
     * @param source      源对象
     * @param sourceClazz 源类
     * @param targetClazz 转换成的类
     * @param <T>         泛型标记
     * @return T
     */
    @Nullable
    public static <T> T copyWithConvert(@Nullable Object source, Class<?> sourceClazz, Class<T> targetClazz) {
        if (source == null) {
            return null;
        }
        BeanCopier copier = BeanCopier.create(sourceClazz, targetClazz, true);
        T to = newInstance(targetClazz);
        copier.copy(source, to, new UnoConverter(sourceClazz, targetClazz));
        return to;
    }

    /**
     * 拷贝列表并对不同类型属性进行转换
     *
     * <p>
     * 支持 map bean copy
     * </p>
     *
     * @param sourceList  源对象列表
     * @param targetClazz 转换成的类
     * @param <T>         泛型标记
     * @return List
     */
    public static <T> List<T> copyWithConvert(@Nullable Collection<?> sourceList, Class<T> targetClazz) {
        if (sourceList == null || sourceList.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        List<T> outList = new ArrayList<>(sourceList.size());
        Class<?> sourceClazz = null;
        for (Object source : sourceList) {
            if (source == null) {
                continue;
            }
            if (sourceClazz == null) {
                sourceClazz = source.getClass();
            }
            T bean = BeanUtils.copyWithConvert(source, sourceClazz, targetClazz);
            outList.add(bean);
        }
        return outList;
    }

    /**
     * Copy the property values of the given source bean into the target class.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>This is just a convenience method. For more complex transfer needs,
     *
     * @param source      the source bean
     * @param targetClazz the target bean class
     * @param <T>         泛型标记
     * @return T
     * @throws BeansException if the copying failed
     */
    @Nullable
    public static <T> T copyProperties(@Nullable Object source, Class<T> targetClazz) throws BeansException {
        if (source == null) {
            return null;
        }
        T to = newInstance(targetClazz);
        BeanUtils.copyProperties(source, to);
        return to;
    }

    /**
     * Copy the property values of the given source bean into the target class.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>This is just a convenience method. For more complex transfer needs,
     *
     * @param sourceList  the source list bean
     * @param targetClazz the target bean class
     * @param <T>         泛型标记
     * @return List
     * @throws BeansException if the copying failed
     */
    public static <T> List<T> copyProperties(@Nullable Collection<?> sourceList, Class<T> targetClazz) throws BeansException {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> outList = new ArrayList<>(sourceList.size());
        for (Object source : sourceList) {
            if (source == null) {
                continue;
            }
            T bean = BeanUtils.copyProperties(source, targetClazz);
            outList.add(bean);
        }
        return outList;
    }

    /**
     * 将对象装成map形式
     *
     * @param bean 源对象
     * @return {Map}
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(@Nullable Object bean) {
        if (bean == null) {
            return new HashMap<>(0);
        }
        return BeanMap.create(bean);
    }

    /**
     * 将map 转为 bean
     *
     * @param beanMap   map
     * @param valueType 对象类型
     * @param <T>       泛型标记
     * @return {T}
     */
    public static <T> T toBean(Map<String, Object> beanMap, Class<T> valueType) {
        Objects.requireNonNull(beanMap, "beanMap Could not null");
        T to = newInstance(valueType);
        if (beanMap.isEmpty()) {
            return to;
        }
        BeanUtils.copy(beanMap, to);
        return to;
    }

    /**
     * 给一个Bean添加字段
     *
     * @param superBean 父级Bean
     * @param props     新增属性
     * @return {Object}
     */
    @Nullable
    public static Object generator(@Nullable Object superBean, BeanProperty... props) {
        if (superBean == null) {
            return null;
        }
        Class<?> superclass = superBean.getClass();
        Object genBean = generator(superclass, props);
        BeanUtils.copy(superBean, genBean);
        return genBean;
    }

    /**
     * 给一个class添加字段
     *
     * @param superclass 父级
     * @param props      新增属性
     * @return {Object}
     */
    public static Object generator(Class<?> superclass, BeanProperty... props) {
        BeanGenerator generator = new BeanGenerator();
        generator.setSuperclass(superclass);
        generator.setUseCache(true);
        for (BeanProperty prop : props) {
            generator.addProperty(prop.getName(), prop.getType());
        }
        return generator.create();
    }


}
