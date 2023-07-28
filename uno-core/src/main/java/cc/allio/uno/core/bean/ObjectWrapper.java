package cc.allio.uno.core.bean;

import cc.allio.uno.core.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * POJO对象包装器，作用是反射设置获取该对象的字段值，并可以获取这个类中所有方法等
 *
 * @author jiangwei
 * @date 2022/5/21 10:17
 * @since 1.0
 */
public class ObjectWrapper implements ValueWrapper {

    /**
     * 解析的目标对象
     */
    private final Object instance;

    private final BeanInfoWrapper<Object> wrapper;

    public ObjectWrapper(Object instance) {
        if (Objects.isNull(instance)) {
            throw new NullPointerException("Instance Must not null");
        }
        try {
            this.wrapper = new BeanInfoWrapper<>((Class<Object>) instance.getClass());
            this.instance = instance;
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    public ObjectWrapper(Class<Object> instanceClass) {
        if (Objects.isNull(instanceClass)) {
            throw new NullPointerException("InstanceClass Must not null");
        }
        try {
            this.wrapper = new BeanInfoWrapper<>(instanceClass);
            this.instance = null;
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
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
    public Flux<Tuple2<String, Object>> findAllValues() {
        return findAll()
                .flatMap(propertyDescriptor ->
                        get(propertyDescriptor.getName())
                                .map(value -> Tuples.of(propertyDescriptor.getName(), value)));
    }

    /**
     * 强制获取该pojo所有的值
     *
     * @return Map
     */
    @Override
    public Map<String, Object> findAllValuesForce() {
        AtomicReference<List<Tuple2<String, Object>>> ref = new AtomicReference<>();
        findAllValues().collectList().subscribe(ref::set);
        List<Tuple2<String, Object>> allValues = ref.get();
        if (CollectionUtils.isEmpty(allValues)) {
            return Collections.emptyMap();
        }
        return allValues
                .stream()
                .collect(Collectors.toMap(Tuple2::getT1, Tuple2::getT2));
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
}
