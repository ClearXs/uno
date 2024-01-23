package cc.allio.uno.core.bean;

import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.FieldUtils;
import com.google.common.collect.Lists;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 定义值 wrapper对象
 *
 * @author jiangwei
 * @date 2023/4/17 18:39
 * @see BeanInfoWrapper 根据{@link BeanInfo}提取bean对象的属性进而实现值提取动作
 * @see ObjectWrapper 接收某个具体的对象（一般为pojo）
 * @see MapWrapper 基于{@link Map}实现
 * @since 1.1.4
 */
public interface ValueWrapper {

    Empty EMPTY_VALUE = new Empty();

    /**
     * 给定字段判断是否包含当前bean中是否包含该字段
     *
     * @param name 字段名称
     * @return true 包含 false 不包含
     */
    Boolean contains(String name);

    /**
     * 根据字段名称查找这个字段对应的Descriptor
     *
     * @param name 字段名称
     * @return 查找到Descriptor实例
     * @throws NullPointerException name为null时抛出
     */
    default PropertyDescriptor find(String name) {
        return find(name, Object.class);
    }

    /**
     * 根据字段名称查找这个字段对应的Descriptor
     *
     * @param name  字段名称
     * @param clazz 属性类型
     * @return 查找到Descriptor实例
     * @throws NullPointerException name为null时抛出
     */
    PropertyDescriptor find(String name, Class<?> clazz);

    /**
     * 获取对象某个字段的值
     *
     * @param field 字段
     * @return 单数据源对象，如果原始数据为null，则返回{@link #EMPTY_VALUE}，需要使用方进行判断
     * @see BeanInfoWrapper#get(Object, String)
     */
    default Mono<Object> get(Field field) {
        return get(field.getName());
    }

    /**
     * 获取对象某个字段的值
     *
     * @param name 字段名称
     * @return 单数据源对象
     * @see BeanInfoWrapper#get(Object, String)
     */
    default Mono<Object> get(String name) {
        return get(name, Object.class);
    }

    /**
     * 获取对象某个字段的值
     *
     * @param name      字段名称
     * @param fieldType 字段类型
     * @return 单数据源对象
     * @see BeanInfoWrapper#get(Object, String, Class)
     */
    <T> Mono<T> get(String name, Class<T> fieldType);

    /**
     * 强制获取对象某个字段的值
     *
     * @param name 字段名称
     * @return 该对象这个字段的值或者null
     */
    default Object getForce(String name) {
        AtomicReference<Object> ref = new AtomicReference<>();
        get(name).subscribe(ref::set);
        return ref.get();
    }

    /**
     * 获取{@link Optional}的value
     *
     * @param name 字段名称
     * @return Optional
     */
    default Optional<Object> getOptional(String name) {
        return Optional.ofNullable(getForce(name));
    }

    /**
     * 获取{@link Optional}的value
     *
     * @param name      字段名称
     * @param fieldType 字段类型
     * @param <T>       字段类型
     * @return Optional
     */
    default <T> Optional<T> getOptional(String name, Class<T> fieldType) {
        return Optional.ofNullable(getForce(name, fieldType));
    }

    /**
     * 强制获取对象某个字段的值
     *
     * @param name      字段名称
     * @param fieldType 字段Class
     * @param <F>       字段类型
     * @return 该对象这个字段的值或者null
     * @see BeanInfoWrapper#getForce(Object, String, Class)
     */
    default <F> F getForce(String name, Class<F> fieldType) {
        AtomicReference<F> ref = new AtomicReference<>();
        get(name, fieldType).subscribe(ref::set);
        return ref.get();
    }

    /**
     * 向目标对象设置字段的值。不强行覆盖原先存在的值.
     *
     * @param name  字段名称
     * @param value 值集合数组
     * @throws NullPointerException 当值集合存在null时抛出异常
     * @see BeanInfoWrapper#set(Object, String, Object...)
     */
    Mono<Object> set(String name, Object... value);

    /**
     * 向目标对象设置字段的值.
     *
     * @param name          字段名称
     * @param value         值集合数组
     * @param forceCoverage 是否进行强行覆盖
     * @throws NullPointerException 当值集合存在null时抛出异常
     * @see BeanInfoWrapper#setCoverage(Object, String, boolean, Object...)
     */
    Mono<Object> setCoverage(String name, boolean forceCoverage, Object... value);

    /**
     * 向目标对象设置字段的值
     *
     * @param name  字段名称
     * @param value 值集合数组
     * @throws NullPointerException 当值集合存在null时抛出异常
     * @see #set(String, Object...)
     */
    default void setForce(String name, Object... value) {
        set(name, value).subscribe();
    }

    /**
     * 向目标对象设置字段的值
     *
     * @param name          字段名称
     * @param value         值集合数组
     * @param forceCoverage 是否进行强行覆盖
     * @throws NullPointerException 当值集合存在null时抛出异常
     * @see #setCoverage(String, boolean, Object...)
     */
    default void setForceCoverage(String name, boolean forceCoverage, Object... value) {
        setCoverage(name, forceCoverage, value).subscribe();
    }

    /**
     * 获取所有PropertyDescriptor
     *
     * @return PropertyDescriptor
     */
    Flux<PropertyDescriptor> findAll();

    /**
     * 强制获取所有的获取所有PropertyDescriptor
     *
     * @return 获取所有PropertyDescriptor
     */
    default List<PropertyDescriptor> findAllForce() {
        AtomicReference<List<PropertyDescriptor>> ref = new AtomicReference<>();
        findAll().collectList().subscribe(ref::set);
        return ref.get();
    }

    /**
     * 获取该pojo所有的值
     *
     * @return tuple2
     */
    Flux<Tuple2<String, Object>> findTupleValues();

    /**
     * 强制获取该pojo所有的值
     *
     * @return Map
     */
    default Map<String, Object> findMapValuesForce() {
        AtomicReference<List<Tuple2<String, Object>>> ref = new AtomicReference<>();
        findTupleValues().collectList().subscribe(ref::set);
        List<Tuple2<String, Object>> values = ref.get();
        if (CollectionUtils.isNotEmpty(values)) {
            return values.stream().collect(Collectors.toMap(Tuple2::getT1, Tuple2::getT2));
        }
        return Collections.emptyMap();
    }

    /**
     * 获取引用的目标实体的所有值
     */
    default Flux<Object> findValues() {
        return findTupleValues().map(Tuple2::getT2);
    }

    /**
     * @see #findValues()
     */
    default List<Object> findValuesForce() {
        List<String> fields = findNamesForce();
        List<Object> values = Lists.newArrayList();
        for (String field : fields) {
            Object value = getForce(field);
            if (value == null) {
                value = EMPTY_VALUE;
            }
            values.add(value);
        }
        return values;
    }

    /**
     * 获取引用的目标实体的所有字段名称
     */
    default Flux<String> findNames() {
        return findTupleValues().map(Tuple2::getT1);
    }

    /**
     * @see #findNames()
     */
    default List<String> findNamesForce() {
        Object target = getTarget();
        return Arrays.stream(FieldUtils.getAllFields(target.getClass()))
                .map(Field::getName)
                .toList();
    }

    /**
     * 获取引用的目标实体
     *
     * @return 实体
     */
    Object getTarget();

    /**
     * 给定一个对象，判断该对象是否是{@link #EMPTY_VALUE}，如果是，则返回null，否则返回原始对象
     *
     * @param ori ori
     * @return null or ori
     */
    static Object restore(Object ori) {
        if (EMPTY_VALUE.equals(ori)) {
            return null;
        }
        return ori;
    }

    /**
     * 根据给定的对象获取ValueWrapper实例
     *
     * @param o o
     * @return ValueWrapper
     */
    static ValueWrapper get(Object o) {
        if (Types.isBean(o.getClass())) {
            return new ObjectWrapper(o);
        } else if (Types.isMap(o.getClass())) {
            return new MapWrapper((Map<String, Object>) o);
        }
        throw new UnsupportedOperationException(String.format("unsupport %s value wrapper", o.getClass()));
    }
}
