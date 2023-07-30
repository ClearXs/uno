package cc.allio.uno.core.bean;

import cc.allio.uno.core.type.TypeValue;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.ClassUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * 增强对Bean对象的操作。<b>禁止在Bean上添加{@link lombok.experimental.Accessors}</b>的注解
 * <p>required</p>
 * <ol>
 *     <li>Bean必须要是Public</li>
 * </ol>
 *
 * @author jiangwei
 * @date 2021/12/17 0:05
 * @since 1.0.0
 */
@Getter
@Slf4j
public class BeanInfoWrapper<T> {

    /**
     * java beans解析的Bean对象
     */
    private final BeanInfo beanInfo;

    public BeanInfoWrapper(BeanInfo beanInfo) {
        this.beanInfo = beanInfo;
    }

    public BeanInfoWrapper(@NonNull Class<T> clazz) throws IntrospectionException {
        this.beanInfo = Introspector.getBeanInfo(clazz);
    }

    /**
     * 获取所有PropertyDescriptor
     *
     * @return
     */
    public Flux<PropertyDescriptor> findAll() {
        return Flux.fromArray(beanInfo.getPropertyDescriptors())
                .filter(p -> {
                    String name = p.getName();
                    return !"class".equals(name);
                });
    }

    /**
     * 根据字段名称查找这个字段对应的Descriptor
     *
     * @param name 字段名称
     * @return 查找到Descriptor实例
     * @throws NullPointerException name为null时抛出
     */
    public Mono<PropertyDescriptor> findByName(String name) {
        Assert.notNull(name, "field name must not null");
        return Flux.fromArray(beanInfo.getPropertyDescriptors())
                .filter(descriptor -> descriptor.getName().equals(name))
                .switchIfEmpty(Mono.empty())
                .single()
                .onErrorResume(error -> {
                    if (log.isDebugEnabled()) {
                        log.debug("get field {} descriptor error", name);
                    }
                    return Mono.empty();
                });
    }

    /**
     * 根据字段名称查找这个字段对应的Descriptor
     *
     * @param name 字段名称
     * @return 查找到Descriptor实例
     * @throws NullPointerException name为null时抛出
     */
    public PropertyDescriptor find(String name) {
        AtomicReference<PropertyDescriptor> ref = new AtomicReference<>();
        findByName(name).subscribe(ref::set);
        return ref.get();
    }

    /**
     * 给定字段判断是否包含当前bean中是否包含该字段
     *
     * @param name 字段名称
     * @return true 包含 false 不包含
     */
    public Boolean contains(String name) {
        AtomicBoolean ref = new AtomicBoolean();
        findByName(name).hasElement().subscribe(ref::set);
        return ref.get();
    }

    /**
     * 获取对象某个字段的值
     *
     * @param target 获取字段值的目标对象
     * @param name   字段名称
     * @return 单数据源对象
     */
    public Mono<Object> get(Object target, String name) {
        Assert.notNull(target, "'target' must not null");
        Assert.notNull(name, "'name' must not null");
        return Mono.from(findByName(name))
                .flatMap(descriptor -> read(target, descriptor));
    }

    /**
     * 获取对象某个字段的值
     *
     * @param target 获取字段值的目标对象
     * @param name   字段名称
     * @param <F>    字段类型
     * @return 单数据源对象
     */
    public <F> Mono<F> get(Object target, String name, Class<F> fieldType) {
        return get(target, name).cast(fieldType);
    }

    /**
     * 强制获取某个字段的值
     *
     * @param target 获取字段值的目标对象
     * @param name   字段名称
     * @return 该对象这个字段的值或者null
     */
    public Object getForce(Object target, String name) {
        AtomicReference<Object> ref = new AtomicReference<>();
        get(target, name).subscribe(ref::set);
        return ref.get();
    }

    /**
     * 强制获取对象某个字段的值
     *
     * @param target    获取字段值的目标对象
     * @param name      字段名称
     * @param fieldType 字段类型
     * @param <F>       字段类型范型
     * @return 该对象这个字段的值或者null
     */
    public <F> F getForce(Object target, String name, Class<F> fieldType) {
        AtomicReference<F> ref = new AtomicReference<>();
        get(target, name, fieldType).subscribe(ref::set);
        return ref.get();
    }

    /**
     * 向目标对象设置字段的值，<b>目标字段如果存在值则不进行设置.</b>
     *
     * @param target 设置字段值的目标对象
     * @param name   字段名称
     * @param value  值集合数组
     * @return target实例
     * @throws NullPointerException 当值集合存在null时抛出异常
     * @see #setCoverage(Object, String, boolean, Object...)
     */
    public synchronized Mono<T> set(T target, String name, Object... value) {
        return setCoverage(target, name, false, value);
    }

    /**
     * 向目标对象设置字段的值，目标字段如果存在值则不进行设置
     *
     * @param target        设置字段值的目标对象
     * @param name          字段名称
     * @param value         值集合数组
     * @param forceCoverage 是否进行强行覆盖
     * @return target实例
     * @throws NullPointerException 当值集合存在null时抛出异常
     * @see #write(Object, PropertyDescriptor, boolean, Object...)
     */
    public synchronized Mono<T> setCoverage(T target, String name, boolean forceCoverage, Object... value) {
        Assert.notNull(name, "target must not null");
        return findByName(name)
                .flatMap(descriptor ->
                        write(target, descriptor, forceCoverage, value)
                                .onErrorContinue((error, o) -> {
                                    if (log.isDebugEnabled()) {
                                        log.debug("target {} set field {} value error set empty", target.getClass().getSimpleName(), name);
                                    }
                                }));
    }

    /**
     * 读取指定bean{@link PropertyDescriptor}的ReadMethod方法
     *
     * @param target     读取指定bean
     * @param descriptor 字段对象
     * @return 对象的值 or empty
     */
    private Mono<Object> read(Object target, PropertyDescriptor descriptor) {
        return Mono.justOrEmpty(descriptor.getReadMethod())
                .flatMap(readMethod -> {
                    try {
                        return Mono.justOrEmpty(readMethod.invoke(target));
                    } catch (Throwable ex) {
                        return Mono.error(ex);
                    }
                })
                .onErrorContinue((err, o) -> log.info("Target {} get field {} value error", target.getClass().getSimpleName(), descriptor.getName(), err));
    }

    /**
     * 调用指定bean{@link PropertyDescriptor}的WriteMethod方法，进行字段赋值。
     * <p>根据配置的<code>forceCoverage</code>参数，判断是否进行强行覆盖值（如果字段不为空的化）</p>
     *
     * @param target        写入指定bean
     * @param descriptor    字段对象
     * @param forceCoverage 是否进行强行覆盖
     * @param args          写入参数
     * @return 指定bean
     */
    private Mono<T> write(T target, PropertyDescriptor descriptor, boolean forceCoverage, Object... args) {
        Mono<T> writeMono = Mono.justOrEmpty(descriptor.getWriteMethod())
                .flatMap(writeMethod ->
                        TypeValue.of(writeMethod.getParameterTypes(), args)
                                .map(TypeValue::tryTransfer)
                                .collectList()
                                .flatMap(values -> {
                                    try {
                                        Class<?> propertyType = descriptor.getPropertyType();
                                        if (Types.isArray(propertyType)) {
                                            // 数组赋值
                                            Object[] arrayValues = values.stream()
                                                    .flatMap(o -> {
                                                        if (Types.isArray(o.getClass())) {
                                                            return Stream.of((Object[]) o);
                                                        }
                                                        return Stream.empty();
                                                    })
                                                    .toArray(Object[]::new);
                                            Object o = Array.newInstance(ClassUtils.getArrayClassType(propertyType), arrayValues.length);
                                            for (int i = 0; i < arrayValues.length; i++) {
                                                Array.set(o, i, arrayValues[i]);
                                            }
                                            writeMethod.invoke(target, o);
                                        } else {
                                            writeMethod.invoke(target, values.toArray());
                                        }
                                    } catch (Throwable err) {
                                        if (log.isDebugEnabled()) {
                                            log.debug("Target {} set field {} value error set empty", target.getClass().getSimpleName(), descriptor.getName());
                                        }
                                    }
                                    return Mono.just(target);
                                })
                );
        if (forceCoverage) {
            return writeMono;
        }
        return read(target, descriptor).switchIfEmpty(writeMono).then(Mono.just(target));
    }
}