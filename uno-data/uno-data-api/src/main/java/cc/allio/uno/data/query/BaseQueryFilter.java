package cc.allio.uno.data.query;

import cc.allio.uno.data.orm.dsl.Func;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.word.Distinct;
import cc.allio.uno.data.orm.dsl.type.DBType;

import java.util.Collection;
import java.util.List;

/**
 * 基于{@link QueryOperator}实现的{@link QueryFilter}
 *
 * @author j.x
 * @date 2023/4/17 18:21
 * @since 1.1.4
 */
public class BaseQueryFilter implements QueryFilter, QueryOperator {

    private QueryWrapper queryWrapper;
    private final QueryOperator queryOperator;

    public BaseQueryFilter(DBType dbType, OperatorKey operatorMetadataKey) {
        queryOperator = OperatorGroup.getOperator(QueryOperator.class, operatorMetadataKey, dbType);
    }

    @Override
    public String getDSL() {
        return queryOperator.getDSL();
    }

    @Override
    public QueryOperator parse(String dsl) {
        return queryOperator.parse(dsl);
    }

    @Override
    public void reset() {
        queryOperator.reset();
    }

    @Override
    public void setDBType(DBType dbType) {

    }

    @Override
    public DBType getDBType() {
        return null;
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
    public QueryOperator from(Table table) {
        return queryOperator.from(table);
    }

    @Override
    public Table getTable() {
        return queryOperator.getTable();
    }

    @Override
    public QueryOperator gt(DSLName sqlName, Object value) {
        return queryOperator.gt(sqlName, value);
    }

    @Override
    public QueryOperator gte(DSLName sqlName, Object value) {
        return queryOperator.gte(sqlName, value);
    }

    @Override
    public QueryOperator lt(DSLName sqlName, Object value) {
        return queryOperator.lt(sqlName, value);
    }

    @Override
    public QueryOperator lte(DSLName sqlName, Object value) {
        return queryOperator.lte(sqlName, value);
    }

    @Override
    public QueryOperator eq(DSLName sqlName, Object value) {
        return queryOperator.eq(sqlName, value);
    }

    @Override
    public QueryOperator neq(DSLName sqlName, Object value) {
        return queryOperator.neq(sqlName, value);
    }

    @Override
    public QueryOperator notNull(DSLName sqlName) {
        return queryOperator.notNull(sqlName);
    }

    @Override
    public QueryOperator isNull(DSLName sqlName) {
        return queryOperator.isNull(sqlName);
    }

    @Override
    public QueryOperator in(DSLName sqlName, Object... values) {
        return queryOperator.in(sqlName, values);
    }

    @Override
    public QueryOperator notIn(DSLName sqlName, Object... values) {
        return queryOperator.notIn(sqlName, values);
    }

    @Override
    public QueryOperator between(DSLName sqlName, Object withValue, Object endValue) {
        return queryOperator.between(sqlName, withValue, endValue);
    }

    @Override
    public QueryOperator notBetween(DSLName sqlName, Object withValue, Object endValue) {
        return queryOperator.notBetween(sqlName, withValue, endValue);
    }

    @Override
    public QueryOperator like(DSLName sqlName, Object value) {
        return queryOperator.like(sqlName, value);
    }

    @Override
    public QueryOperator $like(DSLName sqlName, Object value) {
        return queryOperator.$like(sqlName, value);
    }

    @Override
    public QueryOperator like$(DSLName sqlName, Object value) {
        return queryOperator.like$(sqlName, value);
    }

    @Override
    public QueryOperator $like$(DSLName sqlName, Object value) {
        return queryOperator.$like$(sqlName, value);
    }

    @Override
    public QueryOperator notLike(DSLName sqlName, Object value) {
        return queryOperator.notLike(sqlName, value);
    }

    @Override
    public QueryOperator $notLike(DSLName sqlName, Object value) {
        return queryOperator.$notLike(sqlName, value);
    }

    @Override
    public QueryOperator notLike$(DSLName sqlName, Object value) {
        return queryOperator.notLike$(sqlName, value);
    }

    @Override
    public QueryOperator $notLike$(DSLName sqlName, Object value) {
        return queryOperator.$notLike$(sqlName, value);
    }

    @Override
    public QueryOperator or() {
        return queryOperator.or();
    }

    @Override
    public QueryOperator and() {
        return queryOperator.and();
    }

    @Override
    public QueryOperator nor() {
        return queryOperator.nor();
    }

    @Override
    public QueryOperator select(DSLName dslName) {
        return queryOperator.select(dslName);
    }

    @Override
    public QueryOperator select(DSLName dslName, String alias) {
        return queryOperator.select(dslName, alias);
    }

    @Override
    public QueryOperator selects(Collection<DSLName> dslNames) {
        return queryOperator.selects(dslNames);
    }

    @Override
    public List<String> obtainSelectColumns() {
        return queryOperator.obtainSelectColumns();
    }

    @Override
    public QueryOperator distinct() {
        return queryOperator.distinct();
    }

    @Override
    public QueryOperator distinctOn(DSLName sqlName, String alias) {
        return queryOperator.distinctOn(sqlName, alias);
    }

    @Override
    public QueryOperator aggregate(Func syntax, DSLName sqlName, String alias, Distinct distinct) {
        return queryOperator.aggregate(syntax, sqlName, alias, distinct);
    }

    @Override
    public QueryOperator from(QueryOperator fromTable, String alias) {
        return queryOperator.from(fromTable, alias);
    }

    @Override
    public QueryOperator join(Table left, JoinType joinType, Table right, BinaryCondition condition) {
        return queryOperator.join(left, joinType, right, condition);
    }

    @Override
    public QueryOperator orderBy(DSLName sqlName, OrderCondition orderCondition) {
        return queryOperator.orderBy(sqlName, orderCondition);
    }

    @Override
    public QueryOperator limit(Long limit, Long offset) {
        return queryOperator.limit(limit, offset);
    }

    @Override
    public QueryOperator groupByOnes(Collection<DSLName> fieldNames) {
        return queryOperator.groupByOnes(fieldNames);
    }

    @Override
    public QueryOperator tree(QueryOperator baseQuery, QueryOperator subQuery) {
        return queryOperator.tree(baseQuery, subQuery);
    }

    @Override
    public QueryOperator self() {
        return queryOperator;
    }
}
