package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.core.api.Self;
import cc.allio.uno.core.function.lambda.MethodReferenceColumn;

/**
 * where eq1=eq2...
 *
 * @author jiangwei
 * @date 2023/4/16 18:14
 * @since 1.1.4
 */
public interface WhereOperator<T extends Self<T>> extends Self<T> {

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
        return gt(DSLName.of(fieldName), value);
    }

    /**
     * > condition
     *
     * @param sqlName sqlName
     * @param value   比较数据值
     * @return SQLWhereOperator
     */
    T gt(DSLName sqlName, Object value);

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
        return gte(DSLName.of(fieldName), value);
    }

    /**
     * >= condition
     *
     * @param sqlName sqlName
     * @param value   比较数据值
     * @return SQLWhereOperator
     */
    T gte(DSLName sqlName, Object value);

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
        return lt(DSLName.of(fieldName), value);
    }

    /**
     * < condition
     *
     * @param sqlName sqlName
     * @param value   比较数据值
     * @return SQLWhereOperator
     */
    T lt(DSLName sqlName, Object value);

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
        return lte(DSLName.of(fieldName), value);
    }

    /**
     * <= condition
     *
     * @param sqlName sqlName
     * @param value   比较数据值
     * @return SQLWhereOperator
     */
    T lte(DSLName sqlName, Object value);

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
        return eq(DSLName.of(fieldName), value);
    }

    /**
     * = condition
     *
     * @param sqlName sqlName
     * @param value   比较数据值
     * @return SQLWhereOperator
     */
    T eq(DSLName sqlName, Object value);

    /**
     * != condition
     *
     * @param reference 方法引用
     * @param value     比较数据值
     * @return SQLWhereOperator
     */
    default <R> T neq(MethodReferenceColumn<R> reference, Object value) {
        return neq(reference.getColumn(), value);
    }

    /**
     * != condition
     *
     * @param fieldName java variable name
     * @param value     比较数据值
     * @return SQLWhereOperator
     */
    default T neq(String fieldName, Object value) {
        return neq(DSLName.of(fieldName), value);
    }

    /**
     * != condition
     *
     * @param sqlName sqlName
     * @param value   比较数据值
     * @return SQLWhereOperator
     */
    T neq(DSLName sqlName, Object value);

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
        return notNull(DSLName.of(fieldName));
    }

    /**
     * is not null condition
     *
     * @param sqlName sqlName
     * @return SQLWhereOperator
     */
    T notNull(DSLName sqlName);

    /**
     * @see #isNull(DSLName)
     */
    default <R> T isNull(MethodReferenceColumn<R> reference) {
        return isNull(reference.getColumn());
    }

    /**
     * @see #isNull(DSLName)
     */
    default T isNull(String fieldName) {
        return isNull(DSLName.of(fieldName));
    }

    /**
     * is null condition
     *
     * @param sqlName sqlName
     * @return where
     */
    T isNull(DSLName sqlName);

    /**
     * @see #in(DSLName, Object...)
     */
    default <R, V> T in(MethodReferenceColumn<R> reference, V... values) {
        return in(reference.getColumn(), values);
    }

    /**
     * @see #in(DSLName, Object...)
     */
    default <V> T in(String fieldName, V... values) {
        return in(DSLName.of(fieldName), values);
    }

    /**
     * 'in'条件
     *
     * @param sqlName sqlName
     * @param values  数值数据
     * @return SQLWhereOperator
     */
    <V> T in(DSLName sqlName, V... values);

    /**
     * @see #notIn(DSLName, Object...)
     */
    default <R, V> T notIn(MethodReferenceColumn<R> reference, V... values) {
        return notIn(reference.getColumn(), values);
    }

    /**
     * @see #notIn(DSLName, Object...)
     */
    default <V> T notIn(String fieldName, V... values) {
        return notIn(DSLName.of(fieldName), values);
    }

    /**
     * 'not in'条件
     *
     * @param sqlName sqlName
     * @param values  数值数据
     * @return SQLWhereOperator
     */
    <V> T notIn(DSLName sqlName, V... values);

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
        return between(DSLName.of(fieldName), withValue, endValue);
    }

    /**
     * between condition
     *
     * @param sqlName   sqlName
     * @param withValue between起始值
     * @param endValue  between结束值
     * @return SQLWhereOperator
     */
    T between(DSLName sqlName, Object withValue, Object endValue);

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
        return notBetween(DSLName.of(fieldName), withValue, endValue);
    }

    /**
     * not between condition
     *
     * @param sqlName   sqlName
     * @param withValue between起始值
     * @param endValue  between结束值
     * @return SQLWhereOperator
     */
    T notBetween(DSLName sqlName, Object withValue, Object endValue);

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
        return like(DSLName.of(fieldName), value);
    }

    /**
     * 'field'
     *
     * @param sqlName sqlName
     * @param value   like值
     * @return SQLWhereOperator
     */
    T like(DSLName sqlName, Object value);

    /**
     * '%field'
     *
     * @param fieldName java variable name
     * @param value     like值
     * @return SQLWhereOperator
     */
    default T $like(String fieldName, Object value) {
        return $like(DSLName.of(fieldName), value);
    }

    /**
     * '%field'
     *
     * @param sqlName sqlName
     * @param value   like值
     * @return SQLWhereOperator
     */
    T $like(DSLName sqlName, Object value);

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
        return like$(DSLName.of(fieldName), value);
    }

    /**
     * 'field%'
     *
     * @param sqlName sqlName
     * @param value   like值
     * @return SQLWhereOperator
     */
    T like$(DSLName sqlName, Object value);


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
        return $like$(DSLName.of(fieldName), value);
    }

    /**
     * '%like%'
     *
     * @param sqlName sqlName
     * @param value   like值
     * @return SQLWhereOperator
     */
    T $like$(DSLName sqlName, Object value);

    /**
     * @see #notLike(DSLName, Object)
     */
    default <R> T notLike(MethodReferenceColumn<R> reference, Object value) {
        return notLike(reference.getColumn(), value);
    }

    /**
     * @see #notLike(DSLName, Object)
     */
    default T notLike(String fieldName, Object value) {
        return notLike(DSLName.of(fieldName), value);
    }

    /**
     * 'not field'
     *
     * @param sqlName sqlName
     * @param value   like值
     * @return SQLWhereOperator
     */
    T notLike(DSLName sqlName, Object value);

    /**
     * @see #$notLike(DSLName, Object)
     */
    default <R> T $notLike(MethodReferenceColumn<R> reference, Object value) {
        return $notLike(reference.getColumn(), value);
    }

    /**
     * @see #$notLike(DSLName, Object)
     */
    default T $notLike(String fieldName, Object value) {
        return $notLike(DSLName.of(fieldName), value);
    }

    /**
     * 'not %field'
     *
     * @param sqlName sqlName
     * @param value   like值
     * @return SQLWhereOperator
     */
    T $notLike(DSLName sqlName, Object value);

    /**
     * @see #notLike$(DSLName, Object)
     */
    default <R> T notLike$(MethodReferenceColumn<R> reference, Object value) {
        return notLike$(reference.getColumn(), value);
    }

    /**
     * @see #notLike$(DSLName, Object)
     */
    default T notLike$(String fieldName, Object value) {
        return notLike$(DSLName.of(fieldName), value);
    }

    /**
     * 'not field%'
     *
     * @param sqlName sqlName
     * @param value   like值
     * @return SQLWhereOperator
     */
    T notLike$(DSLName sqlName, Object value);

    /**
     * @see #$notLike$(DSLName, Object)
     */
    default <R> T $notLike$(MethodReferenceColumn<R> reference, Object value) {
        return $notLike$(reference.getColumn(), value);
    }

    /**
     * @see #$notLike$(DSLName, Object)
     */
    default T $notLike$(String fieldName, Object value) {
        return $notLike$(DSLName.of(fieldName), value);
    }

    /**
     * '%not like%'
     *
     * @param sqlName sqlName
     * @param value   like值
     * @return SQLWhereOperator
     */
    T $notLike$(DSLName sqlName, Object value);

    /**
     * logic predicate 'or'
     *
     * @return SQLWhereOperator
     */
    T or();

    /**
     * logic predicate 'and'
     *
     * @return SQLWhereOperator
     */
    T and();
}
