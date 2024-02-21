package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.core.bean.ValueWrapper;
import cc.allio.uno.data.orm.dsl.type.DBType;

import java.lang.annotation.*;
import java.util.Objects;

/**
 * DSL操作
 *
 * @author jiangwei
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
     * @return SQLOperator
     */
    T parse(String dsl);

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
     * @return
     */
    default Object getValueIfNullThenNullValue(Object originalValue) {
        return Objects.requireNonNullElse(originalValue, ValueWrapper.EMPTY_VALUE);
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
