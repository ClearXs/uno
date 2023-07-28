package cc.allio.uno.data.orm.sql.dml;

import cc.allio.uno.data.orm.sql.ColumnStatement;
import cc.allio.uno.data.orm.sql.MethodReferenceColumn;

/**
 * SQL Where相关语法
 *
 * @author jiangwei
 * @date 2022/9/30 13:46
 * @since 1.1.0
 * @deprecated 1.1.4版本删除
 */
@Deprecated
public interface Where<T extends Where<T>> extends ColumnStatement<T> {

    /**
     * > condition
     *
     * @param reference 方法引用
     * @param value     比较数据值
     * @return where instance
     */
    default <R> T gt(MethodReferenceColumn<R> reference, Object value) {
        return gt(reference.getColumn(), value);
    }

    /**
     * > condition
     *
     * @param fieldName java variable name
     * @param value     比较数据值
     * @return where instance
     */
    T gt(String fieldName, Object value);

    /**
     * >= condition
     *
     * @param reference 方法引用
     * @param value     比较数据值
     * @return where instance
     */
    default <R> T gte(MethodReferenceColumn<R> reference, Object value) {
        return gte(reference.getColumn(), value);
    }

    /**
     * >= condition
     *
     * @param fieldName java variable name
     * @param value     比较数据值
     * @return where instance
     */
    T gte(String fieldName, Object value);

    /**
     * < condition
     *
     * @param reference 方法引用
     * @param value     比较数据值
     * @return where instance
     */
    default <R> T lt(MethodReferenceColumn<R> reference, Object value) {
        return lt(reference.getColumn(), value);
    }

    /**
     * < condition
     *
     * @param fieldName java variable name
     * @param value     比较数据值
     * @return where instance
     */
    T lt(String fieldName, Object value);

    /**
     * <= condition
     *
     * @param reference 方法引用
     * @param value     比较数据值
     * @return where instance
     */
    default <R> T lte(MethodReferenceColumn<R> reference, Object value) {
        return lte(reference.getColumn(), value);
    }

    /**
     * <= condition
     *
     * @param fieldName java variable name
     * @param value     比较数据值
     * @return where instance
     */
    T lte(String fieldName, Object value);

    /**
     * = condition
     *
     * @param reference 方法引用
     * @param value     比较数据值
     * @return where instance
     */
    default <R> T eq(MethodReferenceColumn<R> reference, Object value) {
        return eq(reference.getColumn(), value);
    }

    /**
     * = condition
     *
     * @param fieldName java variable name
     * @param value     比较数据值
     * @return where instance
     */
    T eq(String fieldName, Object value);

    /**
     * is not null condition
     *
     * @param reference 方法引用
     * @return where instance
     */
    default <R> T notNull(MethodReferenceColumn<R> reference) {
        return notNull(reference.getColumn());
    }

    /**
     * is not null condition
     *
     * @param fieldName java variable name
     * @return where instance
     */
    T notNull(String fieldName);

    /**
     * is null condition
     *
     * @param reference 方法引用
     * @return where
     */
    default <R> T isNull(MethodReferenceColumn<R> reference) {
        return isNull(reference.getColumn());
    }

    /**
     * is null condition
     *
     * @param fieldName java variable name
     * @return where
     */
    T isNull(String fieldName);

    /**
     * 'in'条件
     *
     * @param reference 方法引用
     * @param values    数值数据
     * @return where instance
     */
    default <R> T in(MethodReferenceColumn<R> reference, Object... values) {
        return in(reference.getColumn(), values);
    }

    /**
     * 'in'条件
     *
     * @param fieldName java variable name
     * @param values    数值数据
     * @return where instance
     */
    T in(String fieldName, Object... values);

    /**
     * between condition
     *
     * @param reference 方法引用
     * @param withValue between起始值
     * @param endValue  between结束值
     * @return where instance
     */
    default <R> T between(MethodReferenceColumn<R> reference, Object withValue, Object endValue) {
        return between(reference.getColumn(), withValue, endValue);
    }

    /**
     * between condition
     *
     * @param fieldName java variable name
     * @param withValue between起始值
     * @param endValue  between结束值
     * @return where instance
     */
    T between(String fieldName, Object withValue, Object endValue);

    /**
     * not between condition
     *
     * @param reference 方法引用
     * @param withValue between起始值
     * @param endValue  between结束值
     * @return where instance
     */
    default <R> T notBetween(MethodReferenceColumn<R> reference, Object withValue, Object endValue) {
        return notBetween(reference.getColumn(), withValue, endValue);
    }

    /**
     * not between condition
     *
     * @param fieldName java variable name
     * @param withValue between起始值
     * @param endValue  between结束值
     * @return where instance
     */
    T notBetween(String fieldName, Object withValue, Object endValue);

    /**
     * '%field'
     *
     * @param reference 方法引用
     * @param value     like值
     * @return where instance
     */
    default <R> T $like(MethodReferenceColumn<R> reference, Object value) {
        return $like(reference.getColumn(), value);
    }

    /**
     * '%field'
     *
     * @param fieldName java variable name
     * @param value     like值
     * @return where instance
     */
    T $like(String fieldName, Object value);

    /**
     * 'field%'
     *
     * @param reference 方法引用
     * @param value     like值
     * @return where instance
     */
    default <R> T like$(MethodReferenceColumn<R> reference, Object value) {
        return like$(reference.getColumn(), value);
    }

    /**
     * 'field%'
     *
     * @param fieldName java variable name
     * @param value     like值
     * @return where instance
     */
    T like$(String fieldName, Object value);

    /**
     * '%like%'
     *
     * @param reference 方法引用
     * @param value     like值
     * @return where instance
     */
    default <R> T $like$(MethodReferenceColumn<R> reference, Object value) {
        return $like$(reference.getColumn(), value);
    }

    /**
     * '%like%'
     *
     * @param fieldName java variable name
     * @param value     like值
     * @return where instance
     */
    T $like$(String fieldName, Object value);

    /**
     * 逻辑'and'控制
     *
     * @return where instance
     */
    T or();

    /**
     * 逻辑'or'控制
     *
     * @return where instance
     */
    T and();
}
