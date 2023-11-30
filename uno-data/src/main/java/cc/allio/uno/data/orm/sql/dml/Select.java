package cc.allio.uno.data.orm.sql.dml;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dialect.func.Func;
import cc.allio.uno.data.orm.sql.word.Distinct;
import cc.allio.uno.data.orm.sql.ColumnStatement;
import cc.allio.uno.core.function.MethodReferenceColumn;

import java.util.Collection;

/**
 * SQL Select相关语法
 *
 * @author jiangwei
 * @date 2022/9/30 13:44
 * @see SQLQueryOperator
 * @since 1.1.0
 * @deprecated 1.1.4版本删除
 */
public interface Select<T extends Select<T>> extends ColumnStatement<T> {

    /**
     * 添加'SELECT'条件
     *
     * @param reference 方法引用
     * @return Select
     */
    default <R> T select(MethodReferenceColumn<R> reference) {
        return select(reference.getColumn());
    }

    /**
     * 添加'SELECT'条件
     *
     * @param reference 方法引用
     * @param alias     alias
     * @return Select
     */
    default <R> T select(MethodReferenceColumn<R> reference, String alias) {
        return select(reference.getColumn(), alias);
    }

    /**
     * select *
     *
     * @return Select
     */
    default T selectAll() {
        return select(StringPool.ASTERISK);
    }

    /**
     * 添加'SELECT'条件
     *
     * @param fieldName java variable name
     * @return Select
     */
    T select(String fieldName);

    /**
     * 添加'SELECT'条件
     *
     * @param fieldName java variable name
     * @param alias     alias
     * @return Select
     */
    T select(String fieldName, String alias);

    /**
     * 批量添加'SELECT'条件
     *
     * @param fieldNames java variable name
     * @return Select
     */
    T select(String[] fieldNames);

    /**
     * 批量条件'SELECT'条件
     *
     * @param fieldNames java variable name
     * @return Select
     */
    T select(Collection<String> fieldNames);

    /**
     * 添加 distinct
     *
     * @return Select
     */
    T distinct();

    /**
     * 添加 distinct on
     *
     * @param reference 方法引用
     * @param <R>       实体类型
     * @return Select
     */
    default <R> T distinctOn(MethodReferenceColumn<R> reference) {
        return distinctOn(reference.getColumn());
    }

    /**
     * 添加 distinct on
     *
     * @param reference 方法引用
     * @param alias     别名
     * @param <R>       实体类型
     * @return Select
     */
    default <R> T distinctOn(MethodReferenceColumn<R> reference, String alias) {
        return distinctOn(reference.getColumn(), alias);
    }

    /**
     * 添加 distinct on
     *
     * @param fieldName java variable name
     * @return Select
     */
    default T distinctOn(String fieldName) {
        return distinctOn(fieldName, fieldName);
    }

    /**
     * 添加 distinct on
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return Select
     */
    T distinctOn(String fieldName, String alias);

    /**
     * 添加 min(field) alias
     *
     * @param reference 方法引用
     * @param <R>       实体类型
     * @return Select
     */
    default <R> T min(MethodReferenceColumn<R> reference) {
        return min(reference.getColumn());
    }

    /**
     * 添加 min(field) alias
     *
     * @param reference 方法引用
     * @param alias     别名
     * @param <R>       实体类型
     * @return Select
     */
    default <R> T min(MethodReferenceColumn<R> reference, String alias) {
        return min(reference.getColumn(), alias);
    }

    /**
     * 添加 min(field) alias
     *
     * @param fieldName java variable name
     * @return Select
     */
    default T min(String fieldName) {
        return min(fieldName, null);
    }

    /**
     * 添加 min(field) alias
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return Select
     */
    default T min(String fieldName, String alias) {
        return aggregate(Func.MIN_FUNCTION.getName(), fieldName, alias, null);
    }

    /**
     * 添加 max(field) alias
     *
     * @param reference 方法引用
     * @param <R>       实体类型
     * @return Select
     */
    default <R> T max(MethodReferenceColumn<R> reference) {
        return max(reference.getColumn());
    }

    /**
     * 添加 max(field) alias
     *
     * @param reference 方法应用
     * @param alias     别名
     * @param <R>       实体类型
     * @return Select
     */
    default <R> T max(MethodReferenceColumn<R> reference, String alias) {
        return max(reference.getColumn(), alias);
    }

    /**
     * 添加 max(field) alias
     *
     * @param fieldName java variable name
     * @return Select
     */
    default T max(String fieldName) {
        return max(fieldName, fieldName);
    }

    /**
     * 添加 max(field) alias
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return Select
     */
    default T max(String fieldName, String alias) {
        return aggregate(Func.MAX_FUNCTION.getName(), fieldName, alias, null);
    }

    /**
     * 添加 avg(field) alias
     *
     * @param reference 方法引用
     * @param <R>       实体类型
     * @return Select
     */
    default <R> T avg(MethodReferenceColumn<R> reference) {
        return avg(reference.getColumn());
    }

    /**
     * 添加 avg(field) alias
     *
     * @param reference 方法应用
     * @param alias     别名
     * @param <R>       实体类型
     * @return Select
     */
    default <R> T avg(MethodReferenceColumn<R> reference, String alias) {
        return avg(reference.getColumn(), alias);
    }

    /**
     * 添加 avg(field) alias
     *
     * @param fieldName java variable name
     * @return Select
     */
    default T avg(String fieldName) {
        return avg(fieldName, fieldName);
    }

    /**
     * 添加 avg(field) alias
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return Select
     */
    default T avg(String fieldName, String alias) {
        return aggregate(Func.AVG_FUNCTION.getName(), fieldName, alias, null);
    }

    /**
     * 添加 count(field) alias
     *
     * @return Select
     */
    default T count() {
        return count(StringPool.ASTERISK, "count");
    }

    /**
     * 添加 count(field) alias
     *
     * @param reference 方法应用
     * @param <R>       实体类型
     * @return Select
     */
    default <R> T count(MethodReferenceColumn<R> reference) {
        return count(reference.getColumn(), null);
    }

    /**
     * 添加 count(field) alias
     *
     * @param reference 方法引用
     * @param alias     别名
     * @param <R>       实体类型
     * @return Select
     */
    default <R> T count(MethodReferenceColumn<R> reference, String alias) {
        return count(reference.getColumn(), alias);
    }

    /**
     * 添加 count(field) alias
     *
     * @param fieldName java variable name
     * @return Select
     */
    default T count(String fieldName) {
        return count(fieldName, null);
    }

    /**
     * 添加 count(field) alias
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return Select
     */
    default T count(String fieldName, String alias) {
        return aggregate(Func.COUNT_FUNCTION.getName(), fieldName, alias, null);
    }

    /**
     * Select 函数
     *
     * @param syntax    函数语法
     * @param fieldName java variable name
     * @param alias     别名
     * @param distinct  distinct
     * @return Select
     * @see Func
     */
    T aggregate(String syntax, String fieldName, String alias, Distinct distinct);

    @Override
    default int order() {
        return SELECT_ORDER;
    }
}
