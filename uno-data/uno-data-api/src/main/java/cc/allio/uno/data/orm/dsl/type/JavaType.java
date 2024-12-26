package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.api.EqualsTo;

import java.io.Serializable;

/**
 * java type定义
 *
 * @author j.x
 * @see TypeRegistry
 * @since 1.1.4
 */
public interface JavaType<T> extends Serializable, EqualsTo<Class<?>> {

    /**
     * 获取Java类型对应 class 实例
     *
     * @return class 实例
     */
    Class<T> getJavaType();

    /**
     * 获取当前Java类型对应的SQL字段的默认长度
     *
     * @return SQL length or default 0
     */
    default int getDefaultSQLLength() {
        return 0;
    }

    /**
     * 获取当前Java类型对应SQL字段的默认精度
     *
     * @return SQL precision or default 0
     */
    default int getDefaultSQLPrecision() {
        return 0;
    }

    /**
     * 获取当前Java类型对应的SQL的默认刻度
     *
     * @return SQL scale or default 0
     */
    default int getDefaultSQLScale() {
        return 0;
    }

}
