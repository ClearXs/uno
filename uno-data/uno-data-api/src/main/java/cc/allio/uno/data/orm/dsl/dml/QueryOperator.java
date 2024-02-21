package cc.allio.uno.data.orm.dsl.dml;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.function.lambda.MethodReferenceColumn;
import cc.allio.uno.data.orm.dsl.Func;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.word.Distinct;
import com.google.common.collect.Lists;

import java.util.Collection;

/**
 * SQL Query Operator
 *
 * @author jiangwei
 * @date 2023/4/12 23:02
 * @since 1.1.4
 */
public interface QueryOperator extends PrepareOperator<QueryOperator>, TableOperator<QueryOperator>, WhereOperator<QueryOperator> {

    // ============================== SELECT PART ==============================

    /**
     * 添加'SELECT'条件
     *
     * @param reference 方法引用
     * @return Select
     */
    default <R> QueryOperator select(MethodReferenceColumn<R> reference) {
        return select(reference.getColumn());
    }

    /**
     * 添加'SELECT'条件
     *
     * @param reference 方法引用
     * @param alias     alias
     * @return Select
     */
    default <R> QueryOperator select(MethodReferenceColumn<R> reference, String alias) {
        return select(reference.getColumn(), alias);
    }

    /**
     * select *
     *
     * @return Select
     */
    default QueryOperator selectAll() {
        return select(StringPool.ASTERISK);
    }

    /**
     * 添加'SELECT'条件
     *
     * @param fieldName java variable name
     * @return Select
     */
    default QueryOperator select(String fieldName) {
        return select(DSLName.of(fieldName));
    }

    /**
     * 添加'SELECT'条件
     *
     * @param sqlName sqlName
     * @return Select
     */
    QueryOperator select(DSLName sqlName);

    /**
     * 添加'SELECT'条件
     *
     * @param fieldName java variable name
     * @param alias     alias
     * @return Select
     */
    default QueryOperator select(String fieldName, String alias) {
        return select(DSLName.of(fieldName), alias);
    }

    /**
     * 添加'SELECT'条件
     *
     * @param sqlName sqlName
     * @param alias   alias
     * @return Select
     */
    QueryOperator select(DSLName sqlName, String alias);


    /**
     * 批量添加'SELECT'条件
     *
     * @param fieldNames java variable name
     * @return Select
     */
    default QueryOperator select(String[] fieldNames) {
        return select(Lists.newArrayList(fieldNames));
    }

    /**
     * 批量条件'SELECT'条件
     *
     * @param fieldNames java variable name
     * @return Select
     */
    default QueryOperator select(Collection<String> fieldNames) {
        return selects(fieldNames.stream().map(DSLName::of).toList());
    }

    /**
     * 批量条件'SELECT'条件
     *
     * @param sqlNames sqlNames
     * @return Select
     */
    QueryOperator selects(Collection<DSLName> sqlNames);

    /**
     * 添加 distinct
     *
     * @return Select
     */
    QueryOperator distinct();

    /**
     * 添加 distinct on
     *
     * @param reference 方法引用
     * @param <R>       实体类型
     * @return Select
     */
    default <R> QueryOperator distinctOn(MethodReferenceColumn<R> reference) {
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
    default <R> QueryOperator distinctOn(MethodReferenceColumn<R> reference, String alias) {
        return distinctOn(reference.getColumn(), alias);
    }

    /**
     * 添加 distinct on
     *
     * @param fieldName java variable name
     * @return Select
     */
    default QueryOperator distinctOn(String fieldName) {
        return distinctOn(fieldName, fieldName);
    }

    /**
     * 添加 distinct on
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return Select
     */
    default QueryOperator distinctOn(String fieldName, String alias) {
        return distinctOn(DSLName.of(fieldName), alias);
    }

    /**
     * 添加 distinct on
     *
     * @param sqlName sqlName
     * @param alias   别名
     * @return Select
     */
    QueryOperator distinctOn(DSLName sqlName, String alias);

    /**
     * 添加 min(field) alias
     *
     * @param reference 方法引用
     * @param <R>       实体类型
     * @return Select
     */
    default <R> QueryOperator min(MethodReferenceColumn<R> reference) {
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
    default <R> QueryOperator min(MethodReferenceColumn<R> reference, String alias) {
        return min(reference.getColumn(), alias);
    }

    /**
     * 添加 min(field) alias
     *
     * @param fieldName java variable name
     * @return Select
     */
    default QueryOperator min(String fieldName) {
        return min(fieldName, null);
    }

    /**
     * 添加 min(field) alias
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return Select
     */
    default QueryOperator min(String fieldName, String alias) {
        return aggregate(Func.MIN_FUNCTION.getName(), fieldName, alias, null);
    }

    /**
     * 添加 max(field) alias
     *
     * @param reference 方法引用
     * @param <R>       实体类型
     * @return Select
     */
    default <R> QueryOperator max(MethodReferenceColumn<R> reference) {
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
    default <R> QueryOperator max(MethodReferenceColumn<R> reference, String alias) {
        return max(reference.getColumn(), alias);
    }

    /**
     * 添加 max(field) alias
     *
     * @param fieldName java variable name
     * @return Select
     */
    default QueryOperator max(String fieldName) {
        return max(fieldName, fieldName);
    }

    /**
     * 添加 max(field) alias
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return Select
     */
    default QueryOperator max(String fieldName, String alias) {
        return aggregate(Func.MAX_FUNCTION.getName(), fieldName, alias, null);
    }

    /**
     * 添加 avg(field) alias
     *
     * @param reference 方法引用
     * @param <R>       实体类型
     * @return Select
     */
    default <R> QueryOperator avg(MethodReferenceColumn<R> reference) {
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
    default <R> QueryOperator avg(MethodReferenceColumn<R> reference, String alias) {
        return avg(reference.getColumn(), alias);
    }

    /**
     * 添加 avg(field) alias
     *
     * @param fieldName java variable name
     * @return Select
     */
    default QueryOperator avg(String fieldName) {
        return avg(fieldName, fieldName);
    }

    /**
     * 添加 avg(field) alias
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return Select
     */
    default QueryOperator avg(String fieldName, String alias) {
        return aggregate(Func.AVG_FUNCTION.getName(), fieldName, alias, null);
    }

    /**
     * 添加 count(field) alias
     *
     * @return Select
     */
    default QueryOperator count() {
        return count(StringPool.ASTERISK, "count");
    }

    /**
     * 添加 count(field) alias
     *
     * @param reference 方法应用
     * @param <R>       实体类型
     * @return Select
     */
    default <R> QueryOperator count(MethodReferenceColumn<R> reference) {
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
    default <R> QueryOperator count(MethodReferenceColumn<R> reference, String alias) {
        return count(reference.getColumn(), alias);
    }

    /**
     * 添加 count(field) alias
     *
     * @param fieldName java variable name
     * @return Select
     */
    default QueryOperator count(String fieldName) {
        return count(fieldName, null);
    }

    /**
     * 添加 count(field) alias
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return Select
     */
    default QueryOperator count(String fieldName, String alias) {
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
    default QueryOperator aggregate(String syntax, String fieldName, String alias, Distinct distinct) {
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
    default QueryOperator aggregate(Func syntax, String fieldName, String alias, Distinct distinct) {
        return aggregate(syntax, DSLName.of(fieldName), alias, distinct);
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
    QueryOperator aggregate(Func syntax, DSLName sqlName, String alias, Distinct distinct);

    // ============================== FROM PART ==============================

    /**
     * FROM (SELECT xx FROM ...) alias 语句
     *
     * @param fromTable QueryOperator实例
     * @param alias     查询别名
     * @return QueryOperator
     */
    QueryOperator from(QueryOperator fromTable, String alias);

    /**
     * FROM t1 left join t2 on t1.xx = t2.xx
     *
     * @param left      left
     * @param right     right
     * @param condition 条件
     * @return QueryOperator
     */
    default QueryOperator leftJoin(Table left, Table right, BinaryCondition condition) {
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
    default QueryOperator rightJoin(Table left, Table right, BinaryCondition condition) {
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
    default QueryOperator leftJoinThen(String leftAlias, String rightName, BinaryCondition condition) {
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
    default QueryOperator leftJoinThen(String leftAlias, String rightName, String rightAlias, BinaryCondition condition) {
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
    default QueryOperator rightJoinThen(String leftAlias, String rightName, BinaryCondition condition) {
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
    default QueryOperator rightJoinThen(String leftAlias, String rightName, String rightAlias, BinaryCondition condition) {
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
    default QueryOperator joinThen(JoinType joinType, Table right, BinaryCondition condition) {
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
    default QueryOperator joinThen(String leftAlias, JoinType joinType, Table right, BinaryCondition condition) {
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
    QueryOperator join(Table left, JoinType joinType, Table right, BinaryCondition condition);

    // ============================== ORDER PART ==============================

    /**
     * 默认Order排序规则
     *
     * @param fieldName java variable name
     * @return Order对象
     */
    default QueryOperator by(String fieldName) {
        return byDesc(fieldName);
    }

    /**
     * 默认Order排序规则
     *
     * @param reference 方法引用
     * @return Order对象
     */
    default <R> QueryOperator by(MethodReferenceColumn<R> reference) {
        return by(reference.getColumn());
    }

    /**
     * 添加ORDER ASC
     *
     * @param reference 方法引用
     * @return Order对象
     */
    default <R> QueryOperator byAsc(MethodReferenceColumn<R> reference) {
        return byAsc(reference.getColumn());
    }

    /**
     * 添加ORDER ASC
     *
     * @param fieldName java variable name
     * @return Order对象
     */
    default QueryOperator byAsc(String fieldName) {
        return orderBy(fieldName, OrderCondition.ASC);
    }

    /**
     * 添加ORDER DESC
     *
     * @param reference 方法引用
     * @return Order对象
     */
    default <R> QueryOperator byDesc(MethodReferenceColumn<R> reference) {
        return byDesc(reference.getColumn());
    }

    /**
     * 添加ORDER DESC
     *
     * @param fieldName java variable name
     * @return Order对象
     */
    default QueryOperator byDesc(String fieldName) {
        return orderBy(fieldName, OrderCondition.DESC);
    }

    /**
     * 添加Order语句
     *
     * @param reference 方法引用
     * @param order     排序
     * @return Order对象
     */
    default QueryOperator orderBy(MethodReferenceColumn<?> reference, String order) {
        return orderBy(reference.getColumn(), order);
    }

    /**
     * 添加Order语句
     *
     * @param fieldName java variable name
     * @param order     排序
     * @return Order对象
     */
    default QueryOperator orderBy(String fieldName, String order) {
        return orderBy(fieldName, OrderCondition.valueOf(order));
    }

    /**
     * 添加Order语句
     *
     * @param reference      方法引用
     * @param orderCondition 排序条件
     * @return Order对象
     */
    default <R> QueryOperator orderBy(MethodReferenceColumn<R> reference, OrderCondition orderCondition) {
        return orderBy(reference.getColumn(), orderCondition);
    }

    /**
     * 添加Order语句
     *
     * @param fieldName      java variable name
     * @param orderCondition 排序条件
     * @return Order对象
     */
    default QueryOperator orderBy(String fieldName, OrderCondition orderCondition) {
        return orderBy(DSLName.of(fieldName), orderCondition);
    }

    /**
     * 添加Order语句
     *
     * @param sqlName        sqlName
     * @param orderCondition 排序条件
     * @return Order对象
     */
    QueryOperator orderBy(DSLName sqlName, OrderCondition orderCondition);

    // ============================== LIMIT PART ==============================

    /**
     * 分页
     *
     * @param current  当前页
     * @param pageSize 页大小
     * @return Limit
     */
    default QueryOperator page(Long current, Long pageSize) {
        return limit(pageSize, (current - 1) * pageSize);
    }

    /**
     * LIMIT { number | ALL }
     *
     * @param limit  限制查询的数量
     * @param offset 偏移数
     * @return Limit
     */
    QueryOperator limit(Long limit, Long offset);

    // ============================== WHERE PART ==============================


    /**
     * 由某一个字段进行分组
     *
     * @param reference 方法引用
     * @return Group对象
     */
    default <R> QueryOperator groupByOne(MethodReferenceColumn<R> reference) {
        return groupByOnes(reference.getColumn());
    }

    // ============================== GROUP PART ==============================

    /**
     * 由某一个字段进行分组
     *
     * @param fieldName java variable name
     * @return Group对象
     */
    default QueryOperator groupByOne(String fieldName) {
        return groupByOnes(fieldName);
    }

    /**
     * 由某一类字段进行分组
     *
     * @param fieldNames java variable name数组
     * @return Group对象
     */
    default QueryOperator groupByOnes(String... fieldNames) {
        return groupByOne(Lists.newArrayList(fieldNames));
    }

    /**
     * 由某一类字段进行分组
     *
     * @param fieldNames java variable name集合
     * @return Group对象
     */
    default QueryOperator groupByOne(Collection<String> fieldNames) {
        return groupByOnes(fieldNames.stream().map(DSLName::of).toList());
    }

    /**
     * 由某一类字段进行分组
     *
     * @param fieldNames java variable name集合
     * @return Group对象
     */
    QueryOperator groupByOnes(Collection<DSLName> fieldNames);
}
