package cc.allio.uno.data.orm.sql.dml;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.function.MethodReferenceColumn;
import cc.allio.uno.data.orm.dialect.func.Func;
import cc.allio.uno.data.orm.sql.*;
import cc.allio.uno.data.orm.sql.dml.local.OrderCondition;
import cc.allio.uno.data.orm.sql.word.Distinct;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * SQL Query Operator
 *
 * @author jiangwei
 * @date 2023/4/12 23:02
 * @since 1.1.4
 */
public interface SQLQueryOperator extends SQLPrepareOperator<SQLQueryOperator>, SQLTableOperator<SQLQueryOperator>, SQLWhereOperator<SQLQueryOperator> {

    // ============================== SELECT PART ==============================

    /**
     * 添加'SELECT'条件
     *
     * @param reference 方法引用
     * @return Select
     */
    default <R> SQLQueryOperator select(MethodReferenceColumn<R> reference) {
        return select(reference.getColumn());
    }

    /**
     * 添加'SELECT'条件
     *
     * @param reference 方法引用
     * @param alias     alias
     * @return Select
     */
    default <R> SQLQueryOperator select(MethodReferenceColumn<R> reference, String alias) {
        return select(reference.getColumn(), alias);
    }

    /**
     * select *
     *
     * @return Select
     */
    default SQLQueryOperator selectAll() {
        return select(StringPool.ASTERISK);
    }

    /**
     * 添加'SELECT'条件
     *
     * @param fieldName java variable name
     * @return Select
     */
    default SQLQueryOperator select(String fieldName) {
        return select(SQLName.of(fieldName));
    }

    /**
     * 添加'SELECT'条件
     *
     * @param sqlName sqlName
     * @return Select
     */
    SQLQueryOperator select(SQLName sqlName);

    /**
     * 添加'SELECT'条件
     *
     * @param fieldName java variable name
     * @param alias     alias
     * @return Select
     */
    default SQLQueryOperator select(String fieldName, String alias) {
        return select(SQLName.of(fieldName), alias);
    }

    /**
     * 添加'SELECT'条件
     *
     * @param sqlName sqlName
     * @param alias   alias
     * @return Select
     */
    SQLQueryOperator select(SQLName sqlName, String alias);


    /**
     * 批量添加'SELECT'条件
     *
     * @param fieldNames java variable name
     * @return Select
     */
    default SQLQueryOperator select(String[] fieldNames) {
        return select(Lists.newArrayList(fieldNames));
    }

    /**
     * 批量条件'SELECT'条件
     *
     * @param fieldNames java variable name
     * @return Select
     */
    default SQLQueryOperator select(Collection<String> fieldNames) {
        return selects(fieldNames.stream().map(SQLName::of).collect(Collectors.toList()));
    }

    /**
     * 批量条件'SELECT'条件
     *
     * @param sqlNames sqlNames
     * @return Select
     */
    SQLQueryOperator selects(Collection<SQLName> sqlNames);

    /**
     * 添加 distinct
     *
     * @return Select
     */
    SQLQueryOperator distinct();

    /**
     * 添加 distinct on
     *
     * @param reference 方法引用
     * @param <R>       实体类型
     * @return Select
     */
    default <R> SQLQueryOperator distinctOn(MethodReferenceColumn<R> reference) {
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
    default <R> SQLQueryOperator distinctOn(MethodReferenceColumn<R> reference, String alias) {
        return distinctOn(reference.getColumn(), alias);
    }

    /**
     * 添加 distinct on
     *
     * @param fieldName java variable name
     * @return Select
     */
    default SQLQueryOperator distinctOn(String fieldName) {
        return distinctOn(fieldName, fieldName);
    }

    /**
     * 添加 distinct on
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return Select
     */
    default SQLQueryOperator distinctOn(String fieldName, String alias) {
        return distinctOn(SQLName.of(fieldName), alias);
    }

    /**
     * 添加 distinct on
     *
     * @param sqlName sqlName
     * @param alias   别名
     * @return Select
     */
    SQLQueryOperator distinctOn(SQLName sqlName, String alias);

    /**
     * 添加 min(field) alias
     *
     * @param reference 方法引用
     * @param <R>       实体类型
     * @return Select
     */
    default <R> SQLQueryOperator min(MethodReferenceColumn<R> reference) {
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
    default <R> SQLQueryOperator min(MethodReferenceColumn<R> reference, String alias) {
        return min(reference.getColumn(), alias);
    }

    /**
     * 添加 min(field) alias
     *
     * @param fieldName java variable name
     * @return Select
     */
    default SQLQueryOperator min(String fieldName) {
        return min(fieldName, null);
    }

    /**
     * 添加 min(field) alias
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return Select
     */
    default SQLQueryOperator min(String fieldName, String alias) {
        return aggregate(Func.MIN_FUNCTION.getName(), fieldName, alias, null);
    }

    /**
     * 添加 max(field) alias
     *
     * @param reference 方法引用
     * @param <R>       实体类型
     * @return Select
     */
    default <R> SQLQueryOperator max(MethodReferenceColumn<R> reference) {
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
    default <R> SQLQueryOperator max(MethodReferenceColumn<R> reference, String alias) {
        return max(reference.getColumn(), alias);
    }

    /**
     * 添加 max(field) alias
     *
     * @param fieldName java variable name
     * @return Select
     */
    default SQLQueryOperator max(String fieldName) {
        return max(fieldName, fieldName);
    }

    /**
     * 添加 max(field) alias
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return Select
     */
    default SQLQueryOperator max(String fieldName, String alias) {
        return aggregate(Func.MAX_FUNCTION.getName(), fieldName, alias, null);
    }

    /**
     * 添加 avg(field) alias
     *
     * @param reference 方法引用
     * @param <R>       实体类型
     * @return Select
     */
    default <R> SQLQueryOperator avg(MethodReferenceColumn<R> reference) {
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
    default <R> SQLQueryOperator avg(MethodReferenceColumn<R> reference, String alias) {
        return avg(reference.getColumn(), alias);
    }

    /**
     * 添加 avg(field) alias
     *
     * @param fieldName java variable name
     * @return Select
     */
    default SQLQueryOperator avg(String fieldName) {
        return avg(fieldName, fieldName);
    }

    /**
     * 添加 avg(field) alias
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return Select
     */
    default SQLQueryOperator avg(String fieldName, String alias) {
        return aggregate(Func.AVG_FUNCTION.getName(), fieldName, alias, null);
    }

    /**
     * 添加 count(field) alias
     *
     * @return Select
     */
    default SQLQueryOperator count() {
        return count(StringPool.ASTERISK, "count");
    }

    /**
     * 添加 count(field) alias
     *
     * @param reference 方法应用
     * @param <R>       实体类型
     * @return Select
     */
    default <R> SQLQueryOperator count(MethodReferenceColumn<R> reference) {
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
    default <R> SQLQueryOperator count(MethodReferenceColumn<R> reference, String alias) {
        return count(reference.getColumn(), alias);
    }

    /**
     * 添加 count(field) alias
     *
     * @param fieldName java variable name
     * @return Select
     */
    default SQLQueryOperator count(String fieldName) {
        return count(fieldName, null);
    }

    /**
     * 添加 count(field) alias
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return Select
     */
    default SQLQueryOperator count(String fieldName, String alias) {
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
    default SQLQueryOperator aggregate(String syntax, String fieldName, String alias, Distinct distinct) {
        Func func = Func.of(syntax);
        if (func != null) {
            return aggregate(func, fieldName, alias, distinct);
        }
        return self();
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
    default SQLQueryOperator aggregate(Func syntax, String fieldName, String alias, Distinct distinct) {
        return aggregate(syntax, SQLName.of(fieldName), alias, distinct);
    }

    /**
     * Select 函数
     *
     * @param syntax   函数语法
     * @param sqlName  sqlName
     * @param alias    别名
     * @param distinct distinct
     * @return Select
     * @see Func
     */
    SQLQueryOperator aggregate(Func syntax, SQLName sqlName, String alias, Distinct distinct);

    // ============================== FROM PART ==============================

    /**
     * FROM (SELECT xx FROM ...) alias 语句
     *
     * @param fromTable QueryOperator实例
     * @param alias     查询别名
     * @return QueryOperator
     */
    SQLQueryOperator from(SQLQueryOperator fromTable, String alias);

    /**
     * FROM t1 left join t2 on t1.xx = t2.xx
     *
     * @param left      left
     * @param right     right
     * @param condition 条件
     * @return QueryOperator
     */
    default SQLQueryOperator leftJoin(Table left, Table right, SQLBinaryCondition condition) {
        return join(left, JoinType.LEFT_OUTER_JOIN, right, condition);
    }

    /**
     * FROM t1 right join t2 on t1.xx = t2.xx
     *
     * @param left      left
     * @param right     right
     * @param condition 条件
     * @return QueryOperator
     */
    default SQLQueryOperator rightJoin(Table left, Table right, SQLBinaryCondition condition) {
        return join(left, JoinType.RIGHT_OUTER_JOIN, right, condition);
    }

    /**
     * FROM t1 left join t2 on t1.xx = t2.xx
     *
     * @param leftAlias leftAlias
     * @param rightName rightName
     * @param condition 条件
     * @return QueryOperator
     */
    default SQLQueryOperator leftJoinThen(String leftAlias, String rightName, SQLBinaryCondition condition) {
        return leftJoinThen(leftAlias, rightName, rightName, condition);
    }

    /**
     * FROM t1 left join t2 on t1.xx = t2.xx
     *
     * @param leftAlias  right
     * @param rightName  rightName
     * @param rightAlias rightAlias
     * @param condition  条件
     * @return QueryOperator
     */
    default SQLQueryOperator leftJoinThen(String leftAlias, String rightName, String rightAlias, SQLBinaryCondition condition) {
        return joinThen(leftAlias, JoinType.LEFT_OUTER_JOIN, Table.of(rightName, rightAlias), condition);
    }

    /**
     * FROM t1 right join t2 on t1.xx = t2.xx
     *
     * @param leftAlias right
     * @param rightName rightName
     * @param condition 条件
     * @return QueryOperator
     */
    default SQLQueryOperator rightJoinThen(String leftAlias, String rightName, SQLBinaryCondition condition) {
        return joinThen(leftAlias, JoinType.RIGHT_OUTER_JOIN, Table.of(rightName, rightName), condition);
    }

    /**
     * FROM t1 right join t2 on t1.xx = t2.xx
     *
     * @param leftAlias  right
     * @param rightName  rightName
     * @param rightAlias rightAlias
     * @param condition  条件
     * @return QueryOperator
     */
    default SQLQueryOperator rightJoinThen(String leftAlias, String rightName, String rightAlias, SQLBinaryCondition condition) {
        return joinThen(leftAlias, JoinType.RIGHT_OUTER_JOIN, Table.of(rightName, rightAlias), condition);
    }

    /**
     * FROM (t1 left join t2 on t1.xx = t2.xx) c1 left join t3 ON c1.xx = t3.xx
     *
     * @param joinType  连接
     * @param right     right
     * @param condition 条件
     * @return QueryOperator
     */
    default SQLQueryOperator joinThen(JoinType joinType, Table right, SQLBinaryCondition condition) {
        return joinThen("empty", joinType, right, condition);
    }

    /**
     * FROM (t1 left join t2 on t1.xx = t2.xx) c1 left join t3 ON c1.xx = t3.xx
     *
     * @param leftAlias 表别名
     * @param joinType  连接
     * @param right     right
     * @param condition 条件
     * @return QueryOperator
     */
    default SQLQueryOperator joinThen(String leftAlias, JoinType joinType, Table right, SQLBinaryCondition condition) {
        return join(Table.of(leftAlias, leftAlias), joinType, right, condition);
    }

    /**
     * FROM t1 left join t2 on t1.xx = t2.xx
     *
     * @param left      left
     * @param joinType  连接
     * @param right     right
     * @param condition 条件
     * @return QueryOperator
     */
    SQLQueryOperator join(Table left, JoinType joinType, Table right, SQLBinaryCondition condition);

    // ============================== ORDER PART ==============================

    /**
     * 默认Order排序规则
     *
     * @param fieldName java variable name
     * @return Order对象
     */
    default SQLQueryOperator by(String fieldName) {
        return byDesc(fieldName);
    }

    /**
     * 默认Order排序规则
     *
     * @param reference 方法引用
     * @return Order对象
     */
    default <R> SQLQueryOperator by(MethodReferenceColumn<R> reference) {
        return by(reference.getColumn());
    }

    /**
     * 添加ORDER ASC
     *
     * @param reference 方法引用
     * @return Order对象
     */
    default <R> SQLQueryOperator byAsc(MethodReferenceColumn<R> reference) {
        return byAsc(reference.getColumn());
    }

    /**
     * 添加ORDER ASC
     *
     * @param fieldName java variable name
     * @return Order对象
     */
    default SQLQueryOperator byAsc(String fieldName) {
        return orderBy(fieldName, OrderCondition.ASC);
    }

    /**
     * 添加ORDER DESC
     *
     * @param reference 方法引用
     * @return Order对象
     */
    default <R> SQLQueryOperator byDesc(MethodReferenceColumn<R> reference) {
        return byDesc(reference.getColumn());
    }

    /**
     * 添加ORDER DESC
     *
     * @param fieldName java variable name
     * @return Order对象
     */
    default SQLQueryOperator byDesc(String fieldName) {
        return orderBy(fieldName, OrderCondition.DESC);
    }

    /**
     * 添加Order语句
     *
     * @param reference 方法引用
     * @param order     排序
     * @return Order对象
     */
    default SQLQueryOperator orderBy(MethodReferenceColumn<?> reference, String order) {
        return orderBy(reference.getColumn(), order);
    }

    /**
     * 添加Order语句
     *
     * @param fieldName java variable name
     * @param order     排序
     * @return Order对象
     */
    default SQLQueryOperator orderBy(String fieldName, String order) {
        return orderBy(fieldName, OrderCondition.valueOf(order));
    }

    /**
     * 添加Order语句
     *
     * @param reference      方法引用
     * @param orderCondition 排序条件
     * @return Order对象
     */
    default <R> SQLQueryOperator orderBy(MethodReferenceColumn<R> reference, OrderCondition orderCondition) {
        return orderBy(reference.getColumn(), orderCondition);
    }

    /**
     * 添加Order语句
     *
     * @param fieldName      java variable name
     * @param orderCondition 排序条件
     * @return Order对象
     */
    default SQLQueryOperator orderBy(String fieldName, OrderCondition orderCondition) {
        return orderBy(SQLName.of(fieldName), orderCondition);
    }

    /**
     * 添加Order语句
     *
     * @param sqlName        sqlName
     * @param orderCondition 排序条件
     * @return Order对象
     */
    SQLQueryOperator orderBy(SQLName sqlName, OrderCondition orderCondition);

    // ============================== LIMIT PART ==============================

    /**
     * 分页
     *
     * @param current  当前页
     * @param pageSize 页大小
     * @return Limit
     */
    default SQLQueryOperator page(Long current, Long pageSize) {
        return limit((current - 1) * pageSize, pageSize);
    }

    /**
     * LIMIT { number | ALL }
     *
     * @param limit  起始行数
     * @param offset 偏移数
     * @return Limit
     */
    SQLQueryOperator limit(Long limit, Long offset);

    // ============================== WHERE PART ==============================


    /**
     * 由某一个字段进行分组
     *
     * @param reference 方法引用
     * @return Group对象
     */
    default <R> SQLQueryOperator groupByOne(MethodReferenceColumn<R> reference) {
        return groupByOnes(reference.getColumn());
    }

    // ============================== GROUP PART ==============================

    /**
     * 由某一个字段进行分组
     *
     * @param fieldName java variable name
     * @return Group对象
     */
    default SQLQueryOperator groupByOne(String fieldName) {
        return groupByOnes(fieldName);
    }

    /**
     * 由某一类字段进行分组
     *
     * @param fieldNames java variable name数组
     * @return Group对象
     */
    default SQLQueryOperator groupByOnes(String... fieldNames) {
        return groupByOne(Lists.newArrayList(fieldNames));
    }

    /**
     * 由某一类字段进行分组
     *
     * @param fieldNames java variable name集合
     * @return Group对象
     */
    default SQLQueryOperator groupByOne(Collection<String> fieldNames) {
        return groupByOnes(fieldNames.stream().map(SQLName::of).collect(Collectors.toList()));
    }

    /**
     * 由某一类字段进行分组
     *
     * @param fieldNames java variable name集合
     * @return Group对象
     */
    SQLQueryOperator groupByOnes(Collection<SQLName> fieldNames);

}
