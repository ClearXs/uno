package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.core.api.Self;
import cc.allio.uno.core.function.lambda.MethodReferenceColumn;

/**
 * where condition, contains following operate:
 * <ol>
 *     <li>{@link #gt(DSLName, Object)}</li>
 *     <li>{@link #gte(DSLName, Object)}</li>
 *     <li>{@link #lt(DSLName, Object)}</li>
 *     <li>{@link #lte(DSLName, Object)}</li>
 *     <li>{@link #eq(DSLName, Object)}</li>
 *     <li>{@link #neq(DSLName, Object)}</li>
 *     <li>{@link #notNull(DSLName)}</li>
 *     <li>{@link #isNull(DSLName)}</li>
 *     <li>{@link #in(DSLName, Object[])}</li>
 *     <li>{@link #notIn(DSLName, Object[])}</li>
 *     <li>{@link #between(DSLName, Object, Object)}</li>
 *     <li>{@link #notBetween(DSLName, Object, Object)}</li>
 *     <li>{@link #like(DSLName, Object)}</li>
 *     <li>{@link #$like(DSLName, Object)}</li>
 *     <li>{@link #like$(DSLName, Object)}</li>
 *     <li>{@link #$like$(DSLName, Object)}</li>
 *     <li>{@link #notLike(DSLName, Object)}</li>
 *     <li>{@link #$notLike(DSLName, Object)}</li>
 *     <li>{@link #notLike$(DSLName, Object)}</li>
 *     <li>{@link #$notLike$(DSLName, Object)}</li>
 *     <li>{@link #or()}</li>
 *     <li>{@link #and()}</li>
 *     <li>{@link #nor()}</li>
 * </ol>
 *
 * @author j.x
 * @since 1.1.4
 */
public interface WhereOperator<T extends Self<T>> extends Self<T> {

    /**
     * > condition
     *
     * @param reference 方法引用
     * @param value     比较数据值
     * @return self
     */
    default <R> T gt(MethodReferenceColumn<R> reference, Object value) {
        return gt(reference.getColumn(), value);
    }

    /**
     * > condition
     *
     * @param fieldName java variable name
     * @param value     比较数据值
     * @return self
     */
    default T gt(String fieldName, Object value) {
        return gt(DSLName.of(fieldName), value);
    }

    /**
     * > condition
     *
     * @param dslName dslName
     * @param value   比较数据值
     * @return self
     */
    T gt(DSLName dslName, Object value);

    /**
     * >= condition
     *
     * @param reference 方法引用
     * @param value     比较数据值
     * @return self
     */
    default <R> T gte(MethodReferenceColumn<R> reference, Object value) {
        return gte(reference.getColumn(), value);
    }

    /**
     * >= condition
     *
     * @param fieldName java variable name
     * @param value     比较数据值
     * @return self
     */
    default T gte(String fieldName, Object value) {
        return gte(DSLName.of(fieldName), value);
    }

    /**
     * >= condition
     *
     * @param dslName dslName
     * @param value   比较数据值
     * @return self
     */
    T gte(DSLName dslName, Object value);

    /**
     * < condition
     *
     * @param reference 方法引用
     * @param value     比较数据值
     * @return self
     */
    default <R> T lt(MethodReferenceColumn<R> reference, Object value) {
        return lt(reference.getColumn(), value);
    }

    /**
     * < condition
     *
     * @param fieldName java variable name
     * @param value     比较数据值
     * @return self
     */
    default T lt(String fieldName, Object value) {
        return lt(DSLName.of(fieldName), value);
    }

    /**
     * < condition
     *
     * @param dslName dslName
     * @param value   比较数据值
     * @return self
     */
    T lt(DSLName dslName, Object value);

    /**
     * <= condition
     *
     * @param reference 方法引用
     * @param value     比较数据值
     * @return self
     */
    default <R> T lte(MethodReferenceColumn<R> reference, Object value) {
        return lte(reference.getColumn(), value);
    }

    /**
     * <= condition
     *
     * @param fieldName java variable name
     * @param value     比较数据值
     * @return self
     */
    default T lte(String fieldName, Object value) {
        return lte(DSLName.of(fieldName), value);
    }

    /**
     * <= condition
     *
     * @param dslName dslName
     * @param value   比较数据值
     * @return self
     */
    T lte(DSLName dslName, Object value);

    /**
     * = condition
     *
     * @param reference 方法引用
     * @param value     比较数据值
     * @return self
     */
    default <R> T eq(MethodReferenceColumn<R> reference, Object value) {
        return eq(reference.getColumn(), value);
    }

    /**
     * = condition
     *
     * @param fieldName java variable name
     * @param value     比较数据值
     * @return self
     */
    default T eq(String fieldName, Object value) {
        return eq(DSLName.of(fieldName), value);
    }

    /**
     * = condition
     *
     * @param dslName dslName
     * @param value   比较数据值
     * @return self
     */
    T eq(DSLName dslName, Object value);

    /**
     * != condition
     *
     * @param reference 方法引用
     * @param value     比较数据值
     * @return self
     */
    default <R> T neq(MethodReferenceColumn<R> reference, Object value) {
        return neq(reference.getColumn(), value);
    }

    /**
     * != condition
     *
     * @param fieldName java variable name
     * @param value     比较数据值
     * @return self
     */
    default T neq(String fieldName, Object value) {
        return neq(DSLName.of(fieldName), value);
    }

    /**
     * != condition
     *
     * @param dslName dslName
     * @param value   比较数据值
     * @return self
     */
    T neq(DSLName dslName, Object value);

    /**
     * is not null condition
     *
     * @param reference 方法引用
     * @return self
     */
    default <R> T notNull(MethodReferenceColumn<R> reference) {
        return notNull(reference.getColumn());
    }

    /**
     * is not null condition
     *
     * @param fieldName java variable name
     * @return self
     */
    default T notNull(String fieldName) {
        return notNull(DSLName.of(fieldName));
    }

    /**
     * is not null condition
     *
     * @param dslName dslName
     * @return self
     */
    T notNull(DSLName dslName);

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
     * @param dslName dslName
     * @return where
     */
    T isNull(DSLName dslName);

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
     * @param dslName dslName
     * @param values  数值数据
     * @return self
     */
    <V> T in(DSLName dslName, V... values);

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
     * @param dslName dslName
     * @param values  数值数据
     * @return self
     */
    <V> T notIn(DSLName dslName, V... values);

    /**
     * between condition
     *
     * @param reference 方法引用
     * @param withValue between起始值
     * @param endValue  between结束值
     * @return self
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
     * @return self
     */
    default T between(String fieldName, Object withValue, Object endValue) {
        return between(DSLName.of(fieldName), withValue, endValue);
    }

    /**
     * between condition
     *
     * @param dslName   dslName
     * @param withValue between起始值
     * @param endValue  between结束值
     * @return self
     */
    T between(DSLName dslName, Object withValue, Object endValue);

    /**
     * not between condition
     *
     * @param reference 方法引用
     * @param withValue between起始值
     * @param endValue  between结束值
     * @return self
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
     * @return self
     */
    default T notBetween(String fieldName, Object withValue, Object endValue) {
        return notBetween(DSLName.of(fieldName), withValue, endValue);
    }

    /**
     * not between condition
     *
     * @param dslName   dslName
     * @param withValue between起始值
     * @param endValue  between结束值
     * @return self
     */
    T notBetween(DSLName dslName, Object withValue, Object endValue);

    /**
     * 'field'
     *
     * @param reference 方法引用
     * @param value     like值
     * @return self
     */
    default <R> T like(MethodReferenceColumn<R> reference, Object value) {
        return like(reference.getColumn(), value);
    }

    /**
     * '%field'
     *
     * @param reference 方法引用
     * @param value     like值
     * @return self
     */
    default <R> T $like(MethodReferenceColumn<R> reference, Object value) {
        return $like(reference.getColumn(), value);
    }

    /**
     * 'field'
     *
     * @param fieldName java variable name
     * @param value     like值
     * @return self
     */
    default T like(String fieldName, Object value) {
        return like(DSLName.of(fieldName), value);
    }

    /**
     * 'field'
     *
     * @param dslName dslName
     * @param value   like值
     * @return self
     */
    T like(DSLName dslName, Object value);

    /**
     * '%field'
     *
     * @param fieldName java variable name
     * @param value     like值
     * @return self
     */
    default T $like(String fieldName, Object value) {
        return $like(DSLName.of(fieldName), value);
    }

    /**
     * '%field'
     *
     * @param dslName dslName
     * @param value   like值
     * @return self
     */
    T $like(DSLName dslName, Object value);

    /**
     * 'field%'
     *
     * @param reference 方法引用
     * @param value     like值
     * @return self
     */
    default <R> T like$(MethodReferenceColumn<R> reference, Object value) {
        return like$(reference.getColumn(), value);
    }

    /**
     * 'field%'
     *
     * @param fieldName java variable name
     * @param value     like值
     * @return self
     */
    default T like$(String fieldName, Object value) {
        return like$(DSLName.of(fieldName), value);
    }

    /**
     * 'field%'
     *
     * @param dslName dslName
     * @param value   like值
     * @return self
     */
    T like$(DSLName dslName, Object value);


    /**
     * '%like%'
     *
     * @param reference 方法引用
     * @param value     like值
     * @return self
     */
    default <R> T $like$(MethodReferenceColumn<R> reference, Object value) {
        return $like$(reference.getColumn(), value);
    }

    /**
     * '%like%'
     *
     * @param fieldName java variable name
     * @param value     like值
     * @return self
     */
    default T $like$(String fieldName, Object value) {
        return $like$(DSLName.of(fieldName), value);
    }

    /**
     * '%like%'
     *
     * @param dslName dslName
     * @param value   like值
     * @return self
     */
    T $like$(DSLName dslName, Object value);

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
     * @param dslName dslName
     * @param value   like值
     * @return self
     */
    T notLike(DSLName dslName, Object value);

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
     * @param dslName dslName
     * @param value   like值
     * @return self
     */
    T $notLike(DSLName dslName, Object value);

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
     * @param dslName dslName
     * @param value   like值
     * @return self
     */
    T notLike$(DSLName dslName, Object value);

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
     * @param dslName dslName
     * @param value   like值
     * @return self
     */
    T $notLike$(DSLName dslName, Object value);

    /**
     * logical predicate 'or'
     *
     * @return self
     */
    T or();

    /**
     * logical predicate 'and'
     *
     * @return self
     */
    T and();

    /**
     * logical predicate 'nor'
     *
     * @return self
     */
    T nor();
}
