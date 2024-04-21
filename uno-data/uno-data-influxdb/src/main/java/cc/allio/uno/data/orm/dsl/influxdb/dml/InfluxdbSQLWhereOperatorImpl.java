package cc.allio.uno.data.orm.dsl.influxdb.dml;

import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.opeartorgroup.OperatorGroup;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.WhereOperator;
import cc.allio.uno.data.orm.dsl.sql.dml.SQLQueryOperator;

/**
 * use SQL as for InfluxQL
 *
 * @author j.x
 * @date 2024/4/14 15:14
 * @since 1.1.8
 */
public class InfluxdbSQLWhereOperatorImpl<T extends WhereOperator<T>> implements WhereOperator<T> {

    protected SQLQueryOperator sqlQueryOperator;

    public InfluxdbSQLWhereOperatorImpl() {
        this.sqlQueryOperator = OperatorGroup.getQueryOperator(SQLQueryOperator.class, OperatorKey.SQL);
    }

    @Override
    public T gt(DSLName dslName, Object value) {
        sqlQueryOperator.gt(dslName, value);
        return self();
    }

    @Override
    public T gte(DSLName dslName, Object value) {
        sqlQueryOperator.gte(dslName, value);
        return self();
    }

    @Override
    public T lt(DSLName dslName, Object value) {
        sqlQueryOperator.lt(dslName, value);
        return self();
    }

    @Override
    public T lte(DSLName dslName, Object value) {
        sqlQueryOperator.lte(dslName, value);
        return self();
    }

    @Override
    public T eq(DSLName dslName, Object value) {
        sqlQueryOperator.eq(dslName, value);
        return self();
    }

    @Override
    public T neq(DSLName dslName, Object value) {
        sqlQueryOperator.neq(dslName, value);
        return self();
    }

    @Override
    public T notNull(DSLName dslName) {
        sqlQueryOperator.notNull(dslName);
        return self();
    }

    @Override
    public T isNull(DSLName dslName) {
        sqlQueryOperator.isNull(dslName);
        return self();
    }

    @Override
    public <V> T in(DSLName dslName, V... values) {
        sqlQueryOperator.in(dslName, values);
        return self();
    }

    @Override
    public <V> T notIn(DSLName dslName, V... values) {
        sqlQueryOperator.notIn(dslName, values);
        return self();
    }

    @Override
    public T between(DSLName dslName, Object withValue, Object endValue) {
        sqlQueryOperator.between(dslName, withValue, endValue);
        return self();
    }

    @Override
    public T notBetween(DSLName dslName, Object withValue, Object endValue) {
        sqlQueryOperator.notBetween(dslName, withValue, endValue);
        return self();
    }

    @Override
    public T like(DSLName dslName, Object value) {
        sqlQueryOperator.lte(dslName, value);
        return self();
    }

    @Override
    public T $like(DSLName dslName, Object value) {
        sqlQueryOperator.$like(dslName, value);
        return self();
    }

    @Override
    public T like$(DSLName dslName, Object value) {
        sqlQueryOperator.like$(dslName, value);
        return self();
    }

    @Override
    public T $like$(DSLName dslName, Object value) {
        sqlQueryOperator.$like$(dslName, value);
        return self();
    }

    @Override
    public T notLike(DSLName dslName, Object value) {
        sqlQueryOperator.notLike(dslName, value);
        return self();
    }

    @Override
    public T $notLike(DSLName dslName, Object value) {
        sqlQueryOperator.$notLike(dslName, value);
        return self();
    }

    @Override
    public T notLike$(DSLName dslName, Object value) {
        sqlQueryOperator.notLike$(dslName, value);
        return self();
    }

    @Override
    public T $notLike$(DSLName dslName, Object value) {
        sqlQueryOperator.$notLike$(dslName, value);
        return self();
    }

    @Override
    public T or() {
        sqlQueryOperator.or();
        return self();
    }

    @Override
    public T and() {
        sqlQueryOperator.and();
        return self();
    }

    @Override
    public T nor() {
        sqlQueryOperator.nor();
        return self();
    }
}
