package cc.allio.uno.core.bean;

import reactor.core.publisher.Mono;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

/**
 * POJO对象包装器，作用是反射设置获取该对象的字段值，并可以获取这个类中所有方法等
 *
 * @author jiangwei
 * @date 2022/5/21 10:17
 * @since 1.0
 */
public class ObjectWrapper {

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

    /**
     * 给定字段判断是否包含当前bean中是否包含该字段
     *
     * @param name 字段名称
     * @return true 包含 false 不包含
     */
    public Boolean contains(String name) {
        return wrapper.contains(name);
    }

    /**
     * 根据字段名称查找这个字段对应的Descriptor
     *
     * @param name 字段名称
     * @return 查找到Descriptor实例
     * @throws NullPointerException name为null时抛出
     */
    public PropertyDescriptor find(String name) {
        return wrapper.find(name);
    }

    /**
     * 获取对象某个字段的值
     *
     * @param field 字段
     * @return 单数据源对象
     * @see BeanInfoWrapper#get(Object, String)
     */
    public Mono<Object> get(Field field) {
        return wrapper.get(instance, field.getName());
    }

    /**
     * 获取对象某个字段的值
     *
     * @param name 字段名称
     * @return 单数据源对象
     * @see BeanInfoWrapper#get(Object, String)
     */
    public Mono<Object> get(String name) {
        return wrapper.get(instance, name);
    }

    /**
     * 获取对象某个字段的值
     *
     * @param name      字段名称
     * @param fieldType 字段类型
     * @return 单数据源对象
     * @see BeanInfoWrapper#get(Object, String, Class)
     */
    public <T> Mono<T> get(String name, Class<T> fieldType) {
        return wrapper.get(instance, name, fieldType);
    }

    /**
     * 强制获取对象某个字段的值
     *
     * @param name 字段名称
     * @return 该对象这个字段的值或者null
     */
    public Object getForce(String name) {
        return wrapper.getForce(instance, name);
    }

    /**
     * 获取{@link Optional}的value
     *
     * @param name 字段名称
     * @return Optional
     */
    public Optional<Object> getOptional(String name) {
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
    public <T> Optional<T> getOptional(String name, Class<T> fieldType) {
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
    public <F> F getForce(String name, Class<F> fieldType) {
        return fieldType.cast(wrapper.getForce(instance, name, fieldType));
    }

    /**
     * 向目标对象设置字段的值。不强行覆盖原先存在的值.
     *
     * @param name  字段名称
     * @param value 值集合数组
     * @throws NullPointerException 当值集合存在null时抛出异常
     * @see BeanInfoWrapper#set(Object, String, Object...)
     */
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
    public Mono<Object> setCoverage(String name, boolean forceCoverage, Object... value) {
        return wrapper.setCoverage(instance, name, forceCoverage, value);
    }

    /**
     * 向目标对象设置字段的值
     *
     * @param name  字段名称
     * @param value 值集合数组
     * @throws NullPointerException 当值集合存在null时抛出异常
     * @see #set(String, Object...)
     */
    public void setForce(String name, Object... value) {
        setForceCoverage(name, false, value);
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
    public void setForceCoverage(String name, boolean forceCoverage, Object... value) {
        setCoverage(name, forceCoverage, value).subscribe();
    }

    /**
     * 获取引用的目标实体
     *
     * @return 实体
     */
    public Object getTarget() {
        return instance;
    }
}
