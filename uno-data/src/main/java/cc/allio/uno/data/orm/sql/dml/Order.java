package cc.allio.uno.data.orm.sql.dml;

import cc.allio.uno.data.orm.sql.dml.local.OrderCondition;
import cc.allio.uno.data.orm.sql.ColumnStatement;
import cc.allio.uno.data.orm.sql.MethodReferenceColumn;

/**
 * SQL ORDER相关语法定义
 *
 * @deprecated 1.1.4版本删除
 */
@Deprecated
public interface Order<T extends Order<T>> extends ColumnStatement<T> {

    /**
     * 默认Order排序规则
     *
     * @param fieldName java variable name
     * @return Order对象
     */
    default T by(String fieldName) {
        return byDesc(fieldName);
    }

    /**
     * 默认Order排序规则
     *
     * @param reference 方法引用
     * @return Order对象
     */
    default <R> T by(MethodReferenceColumn<R> reference) {
        return by(reference.getColumn());
    }

    /**
     * 添加ORDER ASC
     *
     * @param reference 方法引用
     * @return Order对象
     */
    default <R> T byAsc(MethodReferenceColumn<R> reference) {
        return byAsc(reference.getColumn());
    }

    /**
     * 添加ORDER ASC
     *
     * @param fieldName java variable name
     * @return Order对象
     */
    T byAsc(String fieldName);

    /**
     * 添加ORDER DESC
     *
     * @param reference 方法引用
     * @return Order对象
     */
    default <R> T byDesc(MethodReferenceColumn<R> reference) {
        return byDesc(reference.getColumn());
    }

    /**
     * 添加ORDER DESC
     *
     * @param fieldName java variable name
     * @return Order对象
     */
    T byDesc(String fieldName);

    /**
     * 添加Order语句
     *
     * @param reference 方法引用
     * @param order     排序
     * @return Order对象
     */
    default T orderBy(MethodReferenceColumn<?> reference, String order) {
        return orderBy(reference.getColumn(), order);
    }

    /**
     * 添加Order语句
     *
     * @param fieldName java variable name
     * @param order     排序
     * @return Order对象
     */
    T orderBy(String fieldName, String order);

    /**
     * 添加Order语句
     *
     * @param reference      方法引用
     * @param orderCondition 排序条件
     * @return Order对象
     */
    default <R> T orderBy(MethodReferenceColumn<R> reference, OrderCondition orderCondition) {
        return orderBy(reference.getColumn(), orderCondition);
    }

    /**
     * 添加Order语句
     *
     * @param fieldName      java variable name
     * @param orderCondition 排序条件
     * @return Order对象
     */
    T orderBy(String fieldName, OrderCondition orderCondition);
}
