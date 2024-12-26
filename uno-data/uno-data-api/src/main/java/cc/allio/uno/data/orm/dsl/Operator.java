package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.core.api.Self;
import cc.allio.uno.core.bean.ValueWrapper;
import cc.allio.uno.data.orm.dsl.ddl.*;
import cc.allio.uno.data.orm.dsl.dml.DeleteOperator;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.data.orm.dsl.opeartorgroup.Operators;
import cc.allio.uno.data.orm.dsl.opeartorgroup.WrapperOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;

import java.lang.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * DSL Operator
 *
 * <p>
 *     DSL means Domain Specific Language. description Data Operation.
 * </p>
 *
 * <p>
 *     it fulfil before-current-catch-post model.
 *     <ol>
 *         <li>the before {@link #getBeforeOperatorList()}</li>
 *         <li>the current</li>
 *         <li>the error compensate operator {@link #getCompensateOperatorList()}</li>
 *         <li>the success post operator {@link #getPostOperatorList()} </li>
 *     </ol>
 * </p>
 * <p>
 *     through
 * </p>
 *
 * @author j.x
 * @since 1.1.4
 * @see DeleteOperator
 * @see InsertOperator
 * @see QueryOperator
 * @see UpdateOperator
 * @see AlterTableOperator
 * @see CreateTableOperator
 * @see DropTableOperator
 * @see ExistTableOperator
 * @see ShowColumnsOperator
 * @see ShowTablesOperator
 * @see UnrecognizedOperator
 * @see Operators
 */
public interface Operator<T extends Operator<T>> extends Self<T> {

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
     * support operatorFunc make be able customization.
     *
     * @param operatorFunc create new self operatorFunc, and invoker customize some operate..
     * @return self
     */
    T customize(UnaryOperator<T> operatorFunc);

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
        if (this instanceof WrapperOperator wrapperOperator) {
            T actual = wrapperOperator.getActual();
            return realityType.cast(actual);
        }
        return realityType.cast(this);
    }

    /**
     * obtain {@link MetaAcceptorSet}, the default is empty.
     * <p>probable implement adopt proxy way.</p>
     * <p>otherwise will be adopt customize implementation</p>
     *
     * @return the {@link MetaAcceptorSet} instance, default is null
     */
    default MetaAcceptorSet obtainMetaAcceptorSet() {
        return null;
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

    // ----------------------- executing enhance -----------------------

    /**
     * get current {@link Operator} before execute {@link Operator} list
     *
     * @return the pre {@link Operator} list
     */
    default List<Operator<?>> getBeforeOperatorList() {
        return Collections.emptyList();
    }

    /**
     * get current {@link Operator} after execute {@link Operator} list
     *
     * @return the post {@link Operator} list
     */
    default List<Operator<?>> getPostOperatorList() {
        return Collections.emptyList();
    }

    /**
     * when failure to execute current {@link Operator}, then in capture error execute compensate {@link Operator} list
     *
     * @return the compensation {@link Operator} list
     */
    default List<Operator<?>> getCompensateOperatorList() {
        return Collections.emptyList();
    }

    /**
     * from 'dsl' build new {@link Operator}
     *
     * @param dsl the dsl
     * @return the {@link Operator} instance
     */
    static Operator<?> from(String dsl) {
        return Operators.getUnrecognizedOperator().customize(dsl);
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
