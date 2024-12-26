package cc.allio.uno.core.reflect;

import cc.allio.uno.core.api.OptionalContext;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.core.util.ObjectUtils;
import cc.allio.uno.core.util.Values;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.function.Supplier;

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
 * @author j.x
 * @since 1.1.7
 */
@Slf4j
public class Instantiation<I> {

    // 进行实例化的Class对象
    @Getter
    private Class<? extends I>[] waitForInstanceClasses;
    // 构造器参数集合
    private final OptionalContext constructorParameters;
    // 发生错误时实例化异常值
    private final Supplier<? extends I> ifErrorDefaultValue;
    // 实例化后是否排序异常值
    @Getter
    private final boolean excludeNull;
    // 实例化时拓展的拓展
    private final List<InstantiationFeature<I>> features;
    public final Supplier<? extends I> nullValue = () -> null;

    Instantiation(Class<? extends I>[] waitForInstanceClasses,
                  Object[] constructorParameters,
                  Supplier<? extends I> ifErrorDefaultValue,
                  boolean excludeNull) {
        this.waitForInstanceClasses = waitForInstanceClasses;
        this.constructorParameters = OptionalContext.immutable(Values.expand(constructorParameters));
        this.ifErrorDefaultValue = ifErrorDefaultValue;
        this.excludeNull = excludeNull;
        this.features = Lists.newArrayList();
    }

    public void rewriteInstanceClasses(Class<? extends I>[] waitForInstanceClasses) {
        this.waitForInstanceClasses = waitForInstanceClasses;
    }

    /**
     * 添加实例化特性
     *
     * @param feature feature
     */
    public void addFeature(InstantiationFeature<I> feature) {
        features.add(feature);
    }

    /**
     * 只创建一个实例
     *
     * @return Object
     */
    public I createOne() {
        List<I> objects;
        try {
            objects = create();
        } catch (Throwable ex) {
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
        for (InstantiationFeature<I> feature : features) {
            feature.execute(this);
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
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (constructorParameters.matchAll(parameterTypes)) {
                Object[] args = constructorParameters.getAllType(parameterTypes);
                try {
                    ClassUtils.setAccessible(constructor);
                    return (I) constructor.newInstance(args);
                } catch (Throwable ex) {
                    log.error("New instance be problem by constructor. now try to empty parameter create instance, " +
                            "the class {} parameters {}", clazz.getName(), args, ex);
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
