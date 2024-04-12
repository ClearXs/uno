package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.core.bean.ValueWrapper;
import cc.allio.uno.data.orm.dsl.type.DBType;

import java.lang.annotation.*;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * DSL操作
 *
 * @author j.x
 * @date 2023/4/12 19:44
 * @since 1.1.4
 */
public interface Operator<T extends Operator<T>> {

    /**
     * 获取DSL字符串
     *
     * @return DSL字符串
     */
    String getDSL();

    /**
     * 解析DSL
     *
     * @param dsl dsl
     * @return self
     */
    T parse(String dsl);

    /**
     * @see #customize(String)
     */
    default T customize(Supplier<String> dslSupplier) {
        return customize(dslSupplier.get());
    }

    /**
     * support custom dsl string, assure cover all condition
     *
     * @param dsl the dsl syntax
     * @return self
     */
    default T customize(String dsl) {
        return parse(dsl);
    }

    /**
     * reset operator
     */
    void reset();

    /**
     * set db type
     *
     * @param dbType dbType
     */
    void setDBType(DBType dbType);

    /**
     * 获取DBType
     */
    DBType getDBType();

    /**
     * 如果给定原始为null，则返回{@link ValueWrapper#EMPTY_VALUE}
     *
     * @param originalValue originalValue
     * @return origin value or {@link ValueWrapper#EMPTY_VALUE}
     */
    default Object getValueIfNull(Object originalValue) {
        return Objects.requireNonNullElse(originalValue, ValueWrapper.EMPTY_VALUE);
    }

    /**
     * cast reality type, like as mongodb operator, influxdb operator...
     * <p>thus use it's unique operation </p>
     *
     * @param realityType the reality type
     * @param <O>         reality operator type
     * @return reality operator
     * @throws ClassCastException failed to cast to specifies reality type
     */
    default <O extends Operator<?>> O castReality(Class<O> realityType) {
        return realityType.cast(this);
    }

    /**
     * according to operator type, this type maybe is subtype, so translate specifies operator type
     * <p>rules</p>
     * <ul>
     *     <li>current operator type must be class-type</li>
     *     <li>find operator type first interface if {@link Operator} </li>
     * </ul>
     *
     * @param operatorType operatorType
     * @return {@link Operator} or parent type
     */
    static Class<? extends Operator<?>> getHierarchicalType(Class<? extends Operator<?>> operatorType) {
        if (operatorType.isInterface()) {
            return operatorType;
        }
        Class<?>[] interfaces = operatorType.getInterfaces();
        for (Class<?> hierarchy : interfaces) {
            if (Operator.class.isAssignableFrom(hierarchy)) {
                return (Class<? extends Operator<?>>) hierarchy;
            }
        }
        return operatorType;
    }

    /**
     * Operator 分组注解
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Group {
        String value();
    }
}
