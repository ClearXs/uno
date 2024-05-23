package cc.allio.uno.core.bean;

import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.CollectionUtils;
import com.google.common.collect.Maps;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * Object包装器，作用是反射设置获取该对象的字段值，并可以获取这个类中所有方法等
 *
 * @author j.x
 * @date 2022/5/21 10:17
 * @since 1.0
 */
public class BeanWrapper implements ValueWrapper {

    /**
     * 解析的目标对象
     */
    private final Object instance;
    private final BeanInfoWrapper<Object> wrapper;

    public BeanWrapper(Object instance) {
        if (Objects.isNull(instance)) {
            throw new NullPointerException("Instance Must not null");
        }
        Class<Object> beanClass = (Class<Object>) instance.getClass();
        try {
            this.wrapper = new BeanInfoWrapper<>(beanClass);
            this.instance = instance;
        } catch (IntrospectionException ex) {
            throw Exceptions.unchecked(ex);
        }
    }

    public BeanWrapper(Class<Object> instanceClass) {
        if (Objects.isNull(instanceClass)) {
            throw new NullPointerException("InstanceClass Must not null");
        }
        try {
            this.wrapper = new BeanInfoWrapper<>(instanceClass);
            this.instance = null;
        } catch (IntrospectionException ex) {
            throw Exceptions.unchecked(ex);
        }
    }

    /**
     * 给定字段判断是否包含当前bean中是否包含该字段
     *
     * @param name 字段名称
     * @return true 包含 false 不包含
     */
    @Override
    public Boolean contains(String name) {
        return wrapper.contains(name);
    }

    @Override
    public PropertyDescriptor find(String name, Class<?> clazz) {
        return wrapper.find(name);
    }

    /**
     * 获取对象某个字段的值
     *
     * @param name      字段名称
     * @param fieldType 字段类型
     * @return 单数据源对象
     * @see BeanInfoWrapper#get(Object, String, Class)
     */
    @Override
    public <T> Mono<T> get(String name, Class<T> fieldType) {
        return wrapper.get(instance, name, fieldType);
    }

    /**
     * 向目标对象设置字段的值。不强行覆盖原先存在的值.
     *
     * @param name  字段名称
     * @param value 值集合数组
     * @throws NullPointerException 当值集合存在null时抛出异常
     * @see BeanInfoWrapper#set(Object, String, Object...)
     */
    @Override
    public Mono<Object> set(String name, Object... value) {
        return setCoverage(name, false, value);
    }

    /**
     * 向目标对象设置字段的值.
     *
     * @param name          字段名称
     * @param value         值集合数组
     * @param forceCoverage 是否进行强行覆盖
     * @throws NullPointerException 当值集合存在null时抛出异常
     * @see BeanInfoWrapper#setCoverage(Object, String, boolean, Object...)
     */
    @Override
    public Mono<Object> setCoverage(String name, boolean forceCoverage, Object... value) {
        return wrapper.setCoverage(instance, name, forceCoverage, value);
    }

    /**
     * 获取所有PropertyDescriptor
     *
     * @return PropertyDescriptor
     */
    @Override
    public Flux<PropertyDescriptor> findAll() {
        return wrapper.findAll();
    }

    /**
     * 获取该pojo所有的值
     *
     * @return tuple2
     */
    @Override
    public Flux<Tuple2<String, Object>> findTupleValues() {
        return findAll()
                .flatMap(propertyDescriptor ->
                        get(propertyDescriptor.getName()).map(value -> Tuples.of(propertyDescriptor.getName(), value)));
    }

    /**
     * 强制获取该pojo所有的值
     *
     * @return Map
     */
    @Override
    public Map<String, Object> findMapValuesForce() {
        List<Tuple2<String, Object>> allValues = findTupleValues().collectList().block();
        if (CollectionUtils.isEmpty(allValues)) {
            return Collections.emptyMap();
        }
        Map<String, Object> result = Maps.newHashMap();
        for (Tuple2<String, Object> allValue : allValues) {
            String key = allValue.getT1();
            Object value = allValue.getT2();
            if (EMPTY_VALUE.equals(value)) {
                value = null;
            }
            result.put(key, value);
        }
        return result;
    }

    /**
     * 获取引用的目标实体
     *
     * @return 实体
     */
    @Override
    public Object getTarget() {
        return instance;
    }

    /**
     * 设置字段值，如果字段值不存在，则不抛出异常
     *
     * @param instance instance
     * @param name     name
     * @param value    value
     */
    public static void setValue(Object instance, String name, Object... value) {
        BeanWrapper wrapper = new BeanWrapper(instance);
        if (Boolean.TRUE.equals(wrapper.contains(name))) {
            wrapper.setForce(name, value);
        }
    }

    /**
     * 获取字段值
     *
     * @param instance instance
     * @param name     name
     * @return value or null
     */
    public static Object getValue(Object instance, String name) {
        BeanWrapper wrapper = new BeanWrapper(instance);
        if (Boolean.TRUE.equals(wrapper.contains(name))) {
            return wrapper.getForce(name);
        }
        return null;
    }

    /**
     * 获取字段值
     *
     * @param instance instance
     * @param name     name
     * @return value or null
     */
    public static <T> T getValue(Object instance, String name, Class<T> fieldType) {
        BeanWrapper wrapper = new BeanWrapper(instance);
        if (Boolean.TRUE.equals(wrapper.contains(name))) {
            return wrapper.getForce(name, fieldType);
        }
        return null;
    }

    /**
     * create a new {@link BeanWrapper} from bean instance
     *
     * @param beanInstance the bean instance
     * @return {@link BeanWrapper} instance
     * @throws NullPointerException if bean instance is null
     */
    public static BeanWrapper of(Object beanInstance) {
        if (beanInstance == null) {
            throw Exceptions.unNull("bean instance is not null");
        }
        Class<?> beanClass = beanInstance.getClass();
        if (!Types.isBean(beanClass)) {
            throw Exceptions.unOperate("bean must be a bean");
        }
        return new BeanWrapper(beanInstance);
    }

    /**
     * create a new {@link BeanWrapper} from bean class
     *
     * @param beanClass the bean class
     * @return {@link BeanWrapper} instance
     */
    public static BeanWrapper of(Class<?> beanClass) {
        if (!Types.isBean(beanClass)) {
            throw Exceptions.unOperate("bean must be a bean");
        }
        return new BeanWrapper(beanClass);
    }
}
