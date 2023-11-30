package cc.allio.uno.data.orm.sql;

import cc.allio.uno.core.function.MethodReferenceColumn;

/**
 * where eq1=eq2...
 *
 * @author jiangwei
 * @date 2023/4/16 18:14
 * @since 1.1.4
 */
public interface SQLWhereOperator<T extends Operator<T>> extends Operator<T> {

    /**
     * > condition
     *
     * @param reference 方法引用
     * @param value     比较数据值
     * @return SQLWhereOperator
     */
    default <R> T gt(MethodReferenceColumn<R> reference, Object value) {
        return gt(reference.getColumn(), value);
    }

    /**
     * > condition
     *
     * @param fieldName java variable name
     * @param value     比较数据值
     * @return SQLWhereOperator
     */
    default T gt(String fieldName, Object value) {
        return gt(SQLName.of(fieldName), value);
    }

    /**
     * > condition
     *
     * @param sqlName sqlName
     * @param value   比较数据值
     * @return SQLWhereOperator
     */
    T gt(SQLName sqlName, Object value);

    /**
     * >= condition
     *
     * @param reference 方法引用
     * @param value     比较数据值
     * @return SQLWhereOperator
     */
    default <R> T gte(MethodReferenceColumn<R> reference, Object value) {
        return gte(reference.getColumn(), value);
    }

    /**
     * >= condition
     *
     * @param fieldName java variable name
     * @param value     比较数据值
     * @return SQLWhereOperator
     */
    default T gte(String fieldName, Object value) {
        return gte(SQLName.of(fieldName), value);
    }

    /**
     * >= condition
     *
     * @param sqlName sqlName
     * @param value   比较数据值
     * @return SQLWhereOperator
     */
    T gte(SQLName sqlName, Object value);

    /**
     * < condition
     *
     * @param reference 方法引用
     * @param value     比较数据值
     * @return SQLWhereOperator
     */
    default <R> T lt(MethodReferenceColumn<R> reference, Object value) {
        return lt(reference.getColumn(), value);
    }

    /**
     * < condition
     *
     * @param fieldName java variable name
     * @param value     比较数据值
     * @return SQLWhereOperator
     */
    default T lt(String fieldName, Object value) {
        return lt(SQLName.of(fieldName), value);
    }

    /**
     * < condition
     *
     * @param sqlName sqlName
     * @param value   比较数据值
     * @return SQLWhereOperator
     */
    T lt(SQLName sqlName, Object value);

    /**
     * <= condition
     *
     * @param reference 方法引用
     * @param value     比较数据值
     * @return SQLWhereOperator
     */
    default <R> T lte(MethodReferenceColumn<R> reference, Object value) {
        return lte(reference.getColumn(), value);
    }

    /**
     * <= condition
     *
     * @param fieldName java variable name
     * @param value     比较数据值
     * @return SQLWhereOperator
     */
    default T lte(String fieldName, Object value) {
        return lte(SQLName.of(fieldName), value);
    }

    /**
     * <= condition
     *
     * @param sqlName sqlName
     * @param value   比较数据值
     * @return SQLWhereOperator
     */
    T lte(SQLName sqlName, Object value);

    /**
     * = condition
     *
     * @param reference 方法引用
     * @param value     比较数据值
     * @return SQLWhereOperator
     */
    default <R> T eq(MethodReferenceColumn<R> reference, Object value) {
        return eq(reference.getColumn(), value);
    }

    /**
     * = condition
     *
     * @param fieldName java variable name
     * @param value     比较数据值
     * @return SQLWhereOperator
     */
    default T eq(String fieldName, Object value) {
        return eq(SQLName.of(fieldName), value);
    }

    /**
     * = condition
     *
     * @param sqlName sqlName
     * @param value   比较数据值
     * @return SQLWhereOperator
     */
    T eq(SQLName sqlName, Object value);

    /**
     * is not null condition
     *
     * @param reference 方法引用
     * @return SQLWhereOperator
     */
    default <R> T notNull(MethodReferenceColumn<R> reference) {
        return notNull(reference.getColumn());
    }

    /**
     * is not null condition
     *
     * @param fieldName java variable name
     * @return SQLWhereOperator
     */
    default T notNull(String fieldName) {
        return notNull(SQLName.of(fieldName));
    }

    /**
     * is not null condition
     *
     * @param sqlName sqlName
     * @return SQLWhereOperator
     */
    T notNull(SQLName sqlName);

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
    default T isNull(String fieldName) {
        return isNull(SQLName.of(fieldName));
    }

    /**
     * is null condition
     *
     * @param sqlName sqlName
     * @return where
     */
    T isNull(SQLName sqlName);

    /**
     * 'in'条件
     *
     * @param reference 方法引用
     * @param values    数值数据
     * @return SQLWhereOperator
     */
    default <R> T in(MethodReferenceColumn<R> reference, Object... values) {
        return in(reference.getColumn(), values);
    }

    /**
     * 'in'条件
     *
     * @param fieldName java variable name
     * @param values    数值数据
     * @return SQLWhereOperator
     */
    default T in(String fieldName, Object... values) {
        return in(SQLName.of(fieldName), values);
    }

    /**
     * 'in'条件
     *
     * @param sqlName sqlName
     * @param values  数值数据
     * @return SQLWhereOperator
     */
    T in(SQLName sqlName, Object... values);

    /**
     * between condition
     *
     * @param reference 方法引用
     * @param withValue between起始值
     * @param endValue  between结束值
     * @return SQLWhereOperator
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
     * @return SQLWhereOperator
     */
    default T between(String fieldName, Object withValue, Object endValue) {
        return between(SQLName.of(fieldName), withValue, endValue);
    }

    /**
     * between condition
     *
     * @param sqlName   sqlName
     * @param withValue between起始值
     * @param endValue  between结束值
     * @return SQLWhereOperator
     */
    T between(SQLName sqlName, Object withValue, Object endValue);

    /**
     * not between condition
     *
     * @param reference 方法引用
     * @param withValue between起始值
     * @param endValue  between结束值
     * @return SQLWhereOperator
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
     * @return SQLWhereOperator
     */
    default T notBetween(String fieldName, Object withValue, Object endValue) {
        return notBetween(SQLName.of(fieldName), withValue, endValue);
    }

    /**
     * not between condition
     *
     * @param sqlName   sqlName
     * @param withValue between起始值
     * @param endValue  between结束值
     * @return SQLWhereOperator
     */
    T notBetween(SQLName sqlName, Object withValue, Object endValue);

    /**
     * 'field'
     *
     * @param reference 方法引用
     * @param value     like值
     * @return SQLWhereOperator
     */
    default <R> T like(MethodReferenceColumn<R> reference, Object value) {
        return like(reference.getColumn(), value);
    }

    /**
     * '%field'
     *
     * @param reference 方法引用
     * @param value     like值
     * @return SQLWhereOperator
     */
    default <R> T $like(MethodReferenceColumn<R> reference, Object value) {
        return $like(reference.getColumn(), value);
    }

    /**
     * 'field'
     *
     * @param fieldName java variable name
     * @param value     like值
     * @return SQLWhereOperator
     */
    default T like(String fieldName, Object value) {
        return like(SQLName.of(fieldName), value);
    }

    /**
     * 'field'
     *
     * @param sqlName sqlName
     * @param value   like值
     * @return SQLWhereOperator
     */
    T like(SQLName sqlName, Object value);

    /**
     * '%field'
     *
     * @param fieldName java variable name
     * @param value     like值
     * @return SQLWhereOperator
     */
    default T $like(String fieldName, Object value) {
        return $like(SQLName.of(fieldName), value);
    }

    /**
     * '%field'
     *
     * @param sqlName sqlName
     * @param value   like值
     * @return SQLWhereOperator
     */
    T $like(SQLName sqlName, Object value);

    /**
     * 'field%'
     *
     * @param reference 方法引用
     * @param value     like值
     * @return SQLWhereOperator
     */
    default <R> T like$(MethodReferenceColumn<R> reference, Object value) {
        return like$(reference.getColumn(), value);
    }

    /**
     * 'field%'
     *
     * @param fieldName java variable name
     * @param value     like值
     * @return SQLWhereOperator
     */
    default T like$(String fieldName, Object value) {
        return like$(SQLName.of(fieldName), value);
    }

    /**
     * 'field%'
     *
     * @param sqlName sqlName
     * @param value   like值
     * @return SQLWhereOperator
     */
    T like$(SQLName sqlName, Object value);


    /**
     * '%like%'
     *
     * @param reference 方法引用
     * @param value     like值
     * @return SQLWhereOperator
     */
    default <R> T $like$(MethodReferenceColumn<R> reference, Object value) {
        return $like$(reference.getColumn(), value);
    }

    /**
     * '%like%'
     *
     * @param fieldName java variable name
     * @param value     like值
     * @return SQLWhereOperator
     */
    default T $like$(String fieldName, Object value) {
        return $like$(SQLName.of(fieldName), value);
    }

    /**
     * '%like%'
     *
     * @param sqlName sqlName
     * @param value   like值
     * @return SQLWhereOperator
     */
    T $like$(SQLName sqlName, Object value);

    /**
     * 逻辑'or'控制。当使用后，新加的条件语句将会与之前的做为or条件，直到用{@link #and()}
     *
     * @return SQLWhereOperator
     */
    T or();

    /**
     * 逻辑'and'控制。当使用，新加的条件语句将会与之前的作and条件，直到用{@link #or()}
     *
     * @return SQLWhereOperator
     */
    T and();
}
