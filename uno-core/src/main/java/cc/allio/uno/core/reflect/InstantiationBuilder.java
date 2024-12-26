package cc.allio.uno.core.reflect;

import cc.allio.uno.core.api.Self;
import cc.allio.uno.core.util.ObjectUtils;

import java.util.function.Supplier;

/**
 * Instantiation Builder
 *
 * @author j.x
 * @since 1.1.7
 */
public class InstantiationBuilder<I> implements Self<InstantiationBuilder<I>> {

    private Class<? extends I>[] waitForInstanceClasses;
    private Object[] constructorParameters;
    private Supplier<? extends I> ifErrorDefaultValue;
    private boolean isExcludeNull;

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
        return self();
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
        return self();
    }

    /**
     * create parameters
     *
     * @param constructorParameters constructorParameters
     * @return self
     */
    public InstantiationBuilder<I> setConstructorParameters(Object[] constructorParameters) {
        this.constructorParameters = constructorParameters;
        return this;
    }

    /**
     * set creating has error use of default value
     *
     * @param defaultValue the defaultValue
     * @return self
     */
    public InstantiationBuilder<I> setIfErrorDefaultValue(Supplier<? extends I> defaultValue) {
        this.ifErrorDefaultValue = defaultValue;
        return self();
    }

    /**
     * set exclude null
     *
     * @param excludeNull excludeNull
     * @return self
     */
    public InstantiationBuilder<I> setExcludeNull(boolean excludeNull) {
        isExcludeNull = excludeNull;
        return self();
    }

    /**
     * return and create {@link Instantiation}
     *
     * @return {@link Instantiation}
     */
    public Instantiation<I> build() {
        if (ObjectUtils.isEmpty(waitForInstanceClasses)) {
            throw new UnsupportedOperationException("At least one classes");
        }
        return new Instantiation<>(waitForInstanceClasses, constructorParameters, ifErrorDefaultValue, isExcludeNull);
    }

    /**
     * create a {@link InstantiationBuilder} instance
     *
     * @param <I> instance type
     * @return InstantiationBuilder
     */
    public static <I> InstantiationBuilder<I> builder() {
        return new InstantiationBuilder<>();
    }
}
