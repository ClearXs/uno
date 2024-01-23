package cc.allio.uno.data.orm.dsl.type;

import java.io.Serializable;

/**
 * java type定义
 *
 * @author jiangwei
 * @date 2023/1/13 09:10
 * @see TypeRegistry
 * @since 1.1.4
 */
public interface JavaType<T> extends Serializable {

    /**
     * 获取Java类型对应 class 实例
     *
     * @return class 实例
     */
    Class<T> getJavaType();

    /**
     * 比较类型是否属于当前Java类型
     *
     * @param otherJavaType otherJavaType
     * @return ture ye or nor
     */
    boolean equalsTo(Class<?> otherJavaType);

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
