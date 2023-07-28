package cc.allio.uno.data.query.mybatis;

import cc.allio.uno.data.orm.dialect.func.Func;
import cc.allio.uno.data.orm.sql.*;
import cc.allio.uno.data.orm.sql.dml.SQLQueryOperator;
import cc.allio.uno.data.orm.sql.dml.local.OrderCondition;
import cc.allio.uno.data.orm.sql.word.Distinct;
import cc.allio.uno.data.orm.type.DBType;

import java.util.Collection;
import java.util.List;

/**
 * 基于{@link SQLQueryOperator}实现的{@link QueryFilter}
 *
 * @author jiangwei
 * @date 2023/4/17 18:21
 * @since 1.1.4
 */
public class BaseQueryFilter implements QueryFilter, SQLQueryOperator {

    private QueryWrapper queryWrapper;
    private final SQLQueryOperator queryOperator;

    public BaseQueryFilter(DBType dbType, OperatorMetadata.OperatorMetadataKey operatorMetadataKey) {
        queryOperator = SQLOperatorFactory.getSQLOperator(SQLQueryOperator.class, operatorMetadataKey, dbType);
    }

    @Override
    public String getSQL() {
        return queryOperator.getSQL();
    }

    @Override
    public SQLQueryOperator parse(String sql) {
        return queryOperator.parse(sql);
    }

    @Override
    public void reset() {
        queryOperator.reset();
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
    public String getPrepareSQL() {
        return queryOperator.getPrepareSQL();
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        return queryOperator.getPrepareValues();
    }

    @Override
    public SQLQueryOperator from(Table table) {
        return queryOperator.from(table);
    }

    @Override
    public SQLQueryOperator gt(SQLName sqlName, Object value) {
        return queryOperator.gt(sqlName, value);
    }

    @Override
    public SQLQueryOperator gte(SQLName sqlName, Object value) {
        return queryOperator.gte(sqlName, value);
    }

    @Override
    public SQLQueryOperator lt(SQLName sqlName, Object value) {
        return queryOperator.lt(sqlName, value);
    }

    @Override
    public SQLQueryOperator lte(SQLName sqlName, Object value) {
        return queryOperator.lte(sqlName, value);
    }

    @Override
    public SQLQueryOperator eq(SQLName sqlName, Object value) {
        return queryOperator.eq(sqlName, value);
    }

    @Override
    public SQLQueryOperator notNull(SQLName sqlName) {
        return queryOperator.notNull(sqlName);
    }

    @Override
    public SQLQueryOperator isNull(SQLName sqlName) {
        return queryOperator.isNull(sqlName);
    }

    @Override
    public SQLQueryOperator in(SQLName sqlName, Object... values) {
        return queryOperator.in(sqlName, values);
    }

    @Override
    public SQLQueryOperator between(SQLName sqlName, Object withValue, Object endValue) {
        return queryOperator.between(sqlName, withValue, endValue);
    }

    @Override
    public SQLQueryOperator notBetween(SQLName sqlName, Object withValue, Object endValue) {
        return queryOperator.notBetween(sqlName, withValue, endValue);
    }

    @Override
    public SQLQueryOperator like(SQLName sqlName, Object value) {
        return queryOperator.like(sqlName, value);
    }

    @Override
    public SQLQueryOperator $like(SQLName sqlName, Object value) {
        return queryOperator.$like(sqlName, value);
    }

    @Override
    public SQLQueryOperator like$(SQLName sqlName, Object value) {
        return queryOperator.like$(sqlName, value);
    }

    @Override
    public SQLQueryOperator $like$(SQLName sqlName, Object value) {
        return queryOperator.$like$(sqlName, value);
    }

    @Override
    public SQLQueryOperator or() {
        return queryOperator.or();
    }

    @Override
    public SQLQueryOperator and() {
        return queryOperator.and();
    }

    @Override
    public SQLQueryOperator select(SQLName sqlName) {
        return queryOperator.select(sqlName);
    }

    @Override
    public SQLQueryOperator select(SQLName sqlName, String alias) {
        return queryOperator.select(sqlName, alias);
    }

    @Override
    public SQLQueryOperator selects(Collection<SQLName> sqlNames) {
        return queryOperator.selects(sqlNames);
    }

    @Override
    public SQLQueryOperator distinct() {
        return queryOperator.distinct();
    }

    @Override
    public SQLQueryOperator distinctOn(SQLName sqlName, String alias) {
        return queryOperator.distinctOn(sqlName, alias);
    }

    @Override
    public SQLQueryOperator aggregate(Func syntax, SQLName sqlName, String alias, Distinct distinct) {
        return queryOperator.aggregate(syntax, sqlName, alias, distinct);
    }

    @Override
    public SQLQueryOperator from(SQLQueryOperator fromTable, String alias) {
        return queryOperator.from(fromTable, alias);
    }

    @Override
    public SQLQueryOperator join(Table left, JoinType joinType, Table right, SQLBinaryCondition condition) {
        return queryOperator.join(left, joinType, right, condition);
    }

    @Override
    public SQLQueryOperator orderBy(SQLName sqlName, OrderCondition orderCondition) {
        return queryOperator.orderBy(sqlName, orderCondition);
    }

    @Override
    public SQLQueryOperator limit(Long limit, Long offset) {
        return queryOperator.limit(limit, offset);
    }

    @Override
    public SQLQueryOperator groupByOnes(Collection<SQLName> fieldNames) {
        return queryOperator.groupByOnes(fieldNames);
    }

    @Override
    public SQLQueryOperator self() {
        return queryOperator;
    }
}
