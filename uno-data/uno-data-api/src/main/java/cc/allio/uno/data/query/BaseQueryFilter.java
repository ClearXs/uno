package cc.allio.uno.data.query;

import cc.allio.uno.data.orm.dsl.Func;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.opeartorgroup.OperatorGroup;
import cc.allio.uno.data.orm.dsl.word.Distinct;
import cc.allio.uno.data.orm.dsl.type.DBType;

import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * 基于{@link QueryOperator}实现的{@link QueryFilter}
 *
 * @author j.x
 * @date 2023/4/17 18:21
 * @since 1.1.4
 */
public class BaseQueryFilter implements QueryFilter, QueryOperator<BaseQueryFilter> {

    private QueryWrapper queryWrapper;
    private final QueryOperator<?> queryOperator;
    private final DBType dbType;
    private final OperatorKey operatorMetadataKey;

    public BaseQueryFilter(DBType dbType, OperatorKey operatorMetadataKey) {
        this.dbType = dbType;
        this.operatorMetadataKey = operatorMetadataKey;
        this.queryOperator = OperatorGroup.getOperator(QueryOperator.class, operatorMetadataKey, dbType);
    }

    @Override
    public String getDSL() {
        return queryOperator.getDSL();
    }

    @Override
    public BaseQueryFilter parse(String dsl) {
        queryOperator.parse(dsl);
        return self();
    }

    @Override
    public BaseQueryFilter customize(UnaryOperator<BaseQueryFilter> operatorFunc) {
        // TODO will be implementation #customize
        return self();
    }

    @Override
    public void reset() {
        queryOperator.reset();
    }

    @Override
    public void setDBType(DBType dbType) {
        queryOperator.setDBType(dbType);
    }

    @Override
    public DBType getDBType() {
        return queryOperator.getDBType();
    }

    @Override
    public void setQueryWrapper(QueryWrapper queryWrapper) {
        this.queryWrapper = queryWrapper;
    }

    @Override
    public QueryWrapper getQueryWrapper() {
        return queryWrapper;
    }

    @Override
    public String getPrepareDSL() {
        return queryOperator.getPrepareDSL();
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        return queryOperator.getPrepareValues();
    }

    @Override
    public BaseQueryFilter from(Table table) {
        queryOperator.from(table);
        return self();
    }

    @Override
    public Table getTable() {
        return queryOperator.getTable();
    }

    @Override
    public BaseQueryFilter gt(DSLName sqlName, Object value) {
        queryOperator.gt(sqlName, value);
        return self();
    }

    @Override
    public BaseQueryFilter gte(DSLName sqlName, Object value) {
        queryOperator.gte(sqlName, value);
        return self();
    }

    @Override
    public BaseQueryFilter lt(DSLName sqlName, Object value) {
        queryOperator.lt(sqlName, value);
        return self();
    }

    @Override
    public BaseQueryFilter lte(DSLName sqlName, Object value) {
        queryOperator.lte(sqlName, value);
        return self();
    }

    @Override
    public BaseQueryFilter eq(DSLName sqlName, Object value) {
        queryOperator.eq(sqlName, value);
        return self();
    }

    @Override
    public BaseQueryFilter neq(DSLName sqlName, Object value) {
        queryOperator.neq(sqlName, value);
        return self();
    }

    @Override
    public BaseQueryFilter notNull(DSLName sqlName) {
        queryOperator.notNull(sqlName);
        return self();
    }

    @Override
    public BaseQueryFilter isNull(DSLName sqlName) {
        queryOperator.isNull(sqlName);
        return self();
    }

    @Override
    public BaseQueryFilter in(DSLName sqlName, Object... values) {
        queryOperator.in(sqlName, values);
        return self();
    }

    @Override
    public BaseQueryFilter notIn(DSLName sqlName, Object... values) {
        queryOperator.notIn(sqlName, values);
        return self();
    }

    @Override
    public BaseQueryFilter between(DSLName sqlName, Object withValue, Object endValue) {
        queryOperator.between(sqlName, withValue, endValue);
        return self();
    }

    @Override
    public BaseQueryFilter notBetween(DSLName sqlName, Object withValue, Object endValue) {
        queryOperator.notBetween(sqlName, withValue, endValue);
        return self();
    }

    @Override
    public BaseQueryFilter like(DSLName sqlName, Object value) {
        queryOperator.like(sqlName, value);
        return self();
    }

    @Override
    public BaseQueryFilter $like(DSLName sqlName, Object value) {
        queryOperator.$like(sqlName, value);
        return self();
    }

    @Override
    public BaseQueryFilter like$(DSLName sqlName, Object value) {
        queryOperator.like$(sqlName, value);
        return self();
    }

    @Override
    public BaseQueryFilter $like$(DSLName sqlName, Object value) {
        queryOperator.$like$(sqlName, value);
        return self();
    }

    @Override
    public BaseQueryFilter notLike(DSLName sqlName, Object value) {
        queryOperator.notLike(sqlName, value);
        return self();
    }

    @Override
    public BaseQueryFilter $notLike(DSLName sqlName, Object value) {
        queryOperator.$notLike(sqlName, value);
        return self();
    }

    @Override
    public BaseQueryFilter notLike$(DSLName sqlName, Object value) {
        queryOperator.notLike$(sqlName, value);
        return self();
    }

    @Override
    public BaseQueryFilter $notLike$(DSLName sqlName, Object value) {
        queryOperator.$notLike$(sqlName, value);
        return self();
    }

    @Override
    public BaseQueryFilter or() {
        queryOperator.or();
        return self();
    }

    @Override
    public BaseQueryFilter and() {
        queryOperator.and();
        return self();
    }

    @Override
    public BaseQueryFilter nor() {
        queryOperator.nor();
        return self();
    }

    @Override
    public BaseQueryFilter select(DSLName dslName) {
        queryOperator.select(dslName);
        return self();
    }

    @Override
    public BaseQueryFilter select(DSLName dslName, String alias) {
        queryOperator.select(dslName, alias);
        return self();
    }

    @Override
    public BaseQueryFilter selects(Collection<DSLName> dslNames) {
        queryOperator.selects(dslNames);
        return self();
    }

    @Override
    public List<String> obtainSelectColumns() {
        return queryOperator.obtainSelectColumns();
    }

    @Override
    public BaseQueryFilter distinct() {
        queryOperator.distinct();
        return self();
    }

    @Override
    public BaseQueryFilter distinctOn(DSLName dslName, String alias) {
        queryOperator.distinctOn(dslName, alias);
        return self();
    }

    @Override
    public BaseQueryFilter aggregate(Func syntax, DSLName dslName, String alias, Distinct distinct) {
        queryOperator.aggregate(syntax, dslName, alias, distinct);
        return self();
    }

    @Override
    public BaseQueryFilter from(QueryOperator<?> fromTable, String alias) {
        queryOperator.from(fromTable, alias);
        return self();
    }

    @Override
    public BaseQueryFilter join(Table left, JoinType joinType, Table right, BinaryCondition condition) {
        queryOperator.join(left, joinType, right, condition);
        return self();
    }

    @Override
    public BaseQueryFilter orderBy(DSLName dslName, OrderCondition orderCondition) {
        queryOperator.orderBy(dslName, orderCondition);
        return self();
    }

    @Override
    public BaseQueryFilter limit(Long limit, Long offset) {
        queryOperator.limit(limit, offset);
        return self();
    }

    @Override
    public BaseQueryFilter groupByOnes(Collection<DSLName> fieldNames) {
        queryOperator.groupByOnes(fieldNames);
        return self();
    }

    @Override
    public BaseQueryFilter tree(QueryOperator<?> baseQuery, QueryOperator<?> subQuery) {
        queryOperator.tree(baseQuery, subQuery);
        return self();
    }
}