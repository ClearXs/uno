package cc.allio.uno.data.orm.dsl.dml;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.function.lambda.MethodReferenceColumn;
import cc.allio.uno.data.orm.dsl.Func;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.helper.PojoWrapper;
import cc.allio.uno.data.orm.dsl.opeartorgroup.OperatorGroup;
import cc.allio.uno.data.orm.dsl.word.Distinct;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * Query Operator
 *
 * @author j.x
 * @date 2023/4/12 23:02
 * @see OperatorGroup
 * @since 1.1.4
 */
public interface QueryOperator<T extends QueryOperator<T>> extends PrepareOperator<T>, TableOperator<T>, WhereOperator<T> {

    // ============================== SELECT PART ==============================

    /**
     * the select field
     *
     * @param reference 方法引用
     * @return self
     */
    default <R> T select(MethodReferenceColumn<R> reference) {
        return select(reference.getColumn());
    }

    /**
     * the select field
     *
     * @param reference 方法引用
     * @param alias     alias
     * @return self
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
     * the select field
     *
     * @param fieldName java variable name
     * @return self
     */
    default T select(String fieldName) {
        return select(DSLName.of(fieldName));
    }

    /**
     * the select field
     *
     * @param dslName dslName
     * @return self
     */
    T select(DSLName dslName);

    /**
     * the select field
     *
     * @param fieldName java variable name
     * @param alias     alias
     * @return self
     */
    default T select(String fieldName, String alias) {
        return select(DSLName.of(fieldName), alias);
    }

    /**
     * the select field
     *
     * @param dslName dslName
     * @param alias   alias
     * @return self
     */
    T select(DSLName dslName, String alias);


    /**
     * the select field
     *
     * @param fieldNames java variable name
     * @return self
     */
    default T select(String[] fieldNames) {
        return select(Lists.newArrayList(fieldNames));
    }

    /**
     * the select field
     *
     * @param fieldNames java variable name
     * @return self
     */
    default T select(Collection<String> fieldNames) {
        return selects(fieldNames.stream().map(DSLName::of).toList());
    }

    /**
     * the select field
     *
     * @param entityType the entity type
     * @return self
     */
    default <P> T select(Class<P> entityType) {
        Collection<DSLName> columns = PojoWrapper.findColumns(entityType);
        return selects(columns);
    }

    /**
     * 批量条件'SELECT'条件
     *
     * @param dslNames dslNames
     * @return self
     */
    T selects(Collection<DSLName> dslNames);

    /**
     * obtain select columns
     */
    List<String> obtainSelectColumns();

    /**
     * 添加 distinct
     *
     * @return self
     */
    T distinct();

    /**
     * 添加 distinct on
     *
     * @param reference 方法引用
     * @param <R>       实体类型
     * @return self
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
     * @return self
     */
    default <R> T distinctOn(MethodReferenceColumn<R> reference, String alias) {
        return distinctOn(reference.getColumn(), alias);
    }

    /**
     * 添加 distinct on
     *
     * @param fieldName java variable name
     * @return self
     */
    default T distinctOn(String fieldName) {
        return distinctOn(fieldName, fieldName);
    }

    /**
     * 添加 distinct on
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return self
     */
    default T distinctOn(String fieldName, String alias) {
        return distinctOn(DSLName.of(fieldName), alias);
    }

    /**
     * 添加 distinct on
     *
     * @param dslName dslName
     * @param alias   别名
     * @return self
     */
    T distinctOn(DSLName dslName, String alias);

    /**
     * 添加 min(field) alias
     *
     * @param reference 方法引用
     * @param <R>       实体类型
     * @return self
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
     * @return self
     */
    default <R> T min(MethodReferenceColumn<R> reference, String alias) {
        return min(reference.getColumn(), alias);
    }

    /**
     * 添加 min(field) alias
     *
     * @param fieldName java variable name
     * @return self
     */
    default T min(String fieldName) {
        return min(fieldName, null);
    }

    /**
     * 添加 min(field) alias
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return self
     */
    default T min(String fieldName, String alias) {
        return aggregate(Func.MIN_FUNCTION.getName(), fieldName, alias, null);
    }

    /**
     * 添加 max(field) alias
     *
     * @param reference 方法引用
     * @param <R>       实体类型
     * @return self
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
     * @return self
     */
    default <R> T max(MethodReferenceColumn<R> reference, String alias) {
        return max(reference.getColumn(), alias);
    }

    /**
     * 添加 max(field) alias
     *
     * @param fieldName java variable name
     * @return self
     */
    default T max(String fieldName) {
        return max(fieldName, fieldName);
    }

    /**
     * 添加 max(field) alias
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return self
     */
    default T max(String fieldName, String alias) {
        return aggregate(Func.MAX_FUNCTION.getName(), fieldName, alias, null);
    }

    /**
     * 添加 avg(field) alias
     *
     * @param reference 方法引用
     * @param <R>       实体类型
     * @return self
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
     * @return self
     */
    default <R> T avg(MethodReferenceColumn<R> reference, String alias) {
        return avg(reference.getColumn(), alias);
    }

    /**
     * 添加 avg(field) alias
     *
     * @param fieldName java variable name
     * @return self
     */
    default T avg(String fieldName) {
        return avg(fieldName, fieldName);
    }

    /**
     * 添加 avg(field) alias
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return self
     */
    default T avg(String fieldName, String alias) {
        return aggregate(Func.AVG_FUNCTION.getName(), fieldName, alias, null);
    }

    /**
     * 添加 count(field) alias
     *
     * @return self
     */
    default T count() {
        return count(StringPool.ASTERISK, "count");
    }

    /**
     * 添加 count(field) alias
     *
     * @param reference 方法应用
     * @param <R>       实体类型
     * @return self
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
     * @return self
     */
    default <R> T count(MethodReferenceColumn<R> reference, String alias) {
        return count(reference.getColumn(), alias);
    }

    /**
     * 添加 count(field) alias
     *
     * @param fieldName java variable name
     * @return self
     */
    default T count(String fieldName) {
        return count(fieldName, null);
    }

    /**
     * 添加 count(field) alias
     *
     * @param fieldName java variable name
     * @param alias     别名
     * @return self
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
     * @return self
     * @see Func
     */
    default T aggregate(String syntax, String fieldName, String alias, Distinct distinct) {
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
     * @return self
     * @see Func
     */
    default T aggregate(Func syntax, String fieldName, String alias, Distinct distinct) {
        return aggregate(syntax, DSLName.of(fieldName), alias, distinct);
    }

    /**
     * Select 函数
     *
     * @param syntax   函数语法
     * @param dslName  dslName
     * @param alias    别名
     * @param distinct distinct
     * @return self
     * @see Func
     */
    T aggregate(Func syntax, DSLName dslName, String alias, Distinct distinct);

    // ============================== FROM PART ==============================

    /**
     * FROM (SELECT xx FROM ...) alias 语句
     *
     * @param fromTable QueryOperator实例
     * @param alias     查询别名
     * @return self
     */
    T from(QueryOperator<?> fromTable, String alias);

    /**
     * FROM t1 left join t2 on t1.xx = t2.xx
     *
     * @param left      left
     * @param right     right
     * @param condition 条件
     * @return self
     */
    default T leftJoin(Table left, Table right, BinaryCondition condition) {
        return join(left, JoinType.LEFT_OUTER_JOIN, right, condition);
    }

    /**
     * FROM t1 right join t2 on t1.xx = t2.xx
     *
     * @param left      left
     * @param right     right
     * @param condition 条件
     * @return self
     */
    default T rightJoin(Table left, Table right, BinaryCondition condition) {
        return join(left, JoinType.RIGHT_OUTER_JOIN, right, condition);
    }

    /**
     * FROM t1 left join t2 on t1.xx = t2.xx
     *
     * @param leftAlias leftAlias
     * @param rightName rightName
     * @param condition 条件
     * @return self
     */
    default T leftJoinThen(String leftAlias, String rightName, BinaryCondition condition) {
        return leftJoinThen(leftAlias, rightName, rightName, condition);
    }

    /**
     * FROM t1 left join t2 on t1.xx = t2.xx
     *
     * @param leftAlias  right
     * @param rightName  rightName
     * @param rightAlias rightAlias
     * @param condition  条件
     * @return self
     */
    default T leftJoinThen(String leftAlias, String rightName, String rightAlias, BinaryCondition condition) {
        return joinThen(leftAlias, JoinType.LEFT_OUTER_JOIN, Table.of(rightName, rightAlias), condition);
    }

    /**
     * FROM t1 right join t2 on t1.xx = t2.xx
     *
     * @param leftAlias right
     * @param rightName rightName
     * @param condition 条件
     * @return self
     */
    default T rightJoinThen(String leftAlias, String rightName, BinaryCondition condition) {
        return joinThen(leftAlias, JoinType.RIGHT_OUTER_JOIN, Table.of(rightName, rightName), condition);
    }

    /**
     * FROM t1 right join t2 on t1.xx = t2.xx
     *
     * @param leftAlias  right
     * @param rightName  rightName
     * @param rightAlias rightAlias
     * @param condition  条件
     * @return self
     */
    default T rightJoinThen(String leftAlias, String rightName, String rightAlias, BinaryCondition condition) {
        return joinThen(leftAlias, JoinType.RIGHT_OUTER_JOIN, Table.of(rightName, rightAlias), condition);
    }

    /**
     * FROM (t1 left join t2 on t1.xx = t2.xx) c1 left join t3 ON c1.xx = t3.xx
     *
     * @param joinType  连接
     * @param right     right
     * @param condition 条件
     * @return self
     */
    default T joinThen(JoinType joinType, Table right, BinaryCondition condition) {
        return joinThen("empty", joinType, right, condition);
    }

    /**
     * FROM (t1 left join t2 on t1.xx = t2.xx) c1 left join t3 ON c1.xx = t3.xx
     *
     * @param leftAlias 表别名
     * @param joinType  连接
     * @param right     right
     * @param condition 条件
     * @return self
     */
    default T joinThen(String leftAlias, JoinType joinType, Table right, BinaryCondition condition) {
        return join(Table.of(leftAlias, leftAlias), joinType, right, condition);
    }

    /**
     * FROM t1 left join t2 on t1.xx = t2.xx
     *
     * @param left      left
     * @param joinType  连接
     * @param right     right
     * @param condition 条件
     * @return self
     */
    T join(Table left, JoinType joinType, Table right, BinaryCondition condition);

    // ============================== ORDER PART ==============================

    /**
     * 默认Order排序规则
     *
     * @param fieldName java variable name
     * @return self
     */
    default T by(String fieldName) {
        return byDesc(fieldName);
    }

    /**
     * 默认Order排序规则
     *
     * @param reference 方法引用
     * @return self
     */
    default <R> T by(MethodReferenceColumn<R> reference) {
        return by(reference.getColumn());
    }

    /**
     * 添加ORDER ASC
     *
     * @param reference 方法引用
     * @return self
     */
    default <R> T byAsc(MethodReferenceColumn<R> reference) {
        return byAsc(reference.getColumn());
    }

    /**
     * 添加ORDER ASC
     *
     * @param fieldName java variable name
     * @return self
     */
    default T byAsc(String fieldName) {
        return orderBy(fieldName, OrderCondition.ASC);
    }

    /**
     * 添加ORDER DESC
     *
     * @param reference 方法引用
     * @return self
     */
    default <R> T byDesc(MethodReferenceColumn<R> reference) {
        return byDesc(reference.getColumn());
    }

    /**
     * 添加ORDER DESC
     *
     * @param fieldName java variable name
     * @return self
     */
    default T byDesc(String fieldName) {
        return orderBy(fieldName, OrderCondition.DESC);
    }

    /**
     * 添加Order语句
     *
     * @param reference 方法引用
     * @param order     排序
     * @return self
     */
    default T orderBy(MethodReferenceColumn<?> reference, String order) {
        return orderBy(reference.getColumn(), order);
    }

    /**
     * 添加Order语句
     *
     * @param fieldName java variable name
     * @param order     排序
     * @return self
     */
    default T orderBy(String fieldName, String order) {
        return orderBy(fieldName, OrderCondition.valueOf(order));
    }

    /**
     * 添加Order语句
     *
     * @param reference      方法引用
     * @param orderCondition 排序条件
     * @return self
     */
    default <R> T orderBy(MethodReferenceColumn<R> reference, OrderCondition orderCondition) {
        return orderBy(reference.getColumn(), orderCondition);
    }

    /**
     * 添加Order语句
     *
     * @param fieldName      java variable name
     * @param orderCondition 排序条件
     * @return self
     */
    default T orderBy(String fieldName, OrderCondition orderCondition) {
        return orderBy(DSLName.of(fieldName), orderCondition);
    }

    /**
     * 添加Order语句
     *
     * @param dslName        dslName
     * @param orderCondition 排序条件
     * @return self
     */
    T orderBy(DSLName dslName, OrderCondition orderCondition);

    // ============================== LIMIT PART ==============================

    /**
     * 分页
     *
     * @param current  当前页
     * @param pageSize 页大小
     * @return self
     */
    default T page(Long current, Long pageSize) {
        return limit(pageSize, (current - 1) * pageSize);
    }

    /**
     * LIMIT { number | ALL }
     *
     * @param limit  限制查询的数量
     * @param offset 偏移数
     * @return self
     */
    T limit(Long limit, Long offset);

    // ============================== WHERE PART ==============================

    /**
     * 由某一个字段进行分组
     *
     * @param reference 方法引用
     * @return self
     */
    default <R> T groupByOne(MethodReferenceColumn<R> reference) {
        return groupByOnes(reference.getColumn());
    }

    // ============================== GROUP PART ==============================

    /**
     * 由某一个字段进行分组
     *
     * @param fieldName java variable name
     * @return self
     */
    default T groupByOne(String fieldName) {
        return groupByOnes(fieldName);
    }

    /**
     * 由某一类字段进行分组
     *
     * @param fieldNames java variable name数组
     * @return self
     */
    default T groupByOnes(String... fieldNames) {
        return groupByOne(Lists.newArrayList(fieldNames));
    }

    /**
     * 由某一类字段进行分组
     *
     * @param fieldNames java variable name集合
     * @return self
     */
    default T groupByOne(Collection<String> fieldNames) {
        return groupByOnes(fieldNames.stream().map(DSLName::of).toList());
    }

    /**
     * 由某一类字段进行分组
     *
     * @param fieldNames java variable name集合
     * @return Group对象
     */
    T groupByOnes(Collection<DSLName> fieldNames);

    // ====================== advanced method ======================

    /**
     * build tree query operator
     * <p><b>the entity field muse contains id and parent_id</b></p>
     *
     * @param baseQuery the build tree query base query operator
     * @param subQuery  the build tree query for sub query operator
     * @return self
     */
    T tree(QueryOperator<?> baseQuery, QueryOperator<?> subQuery);
}