package cc.allio.uno.data.sql.query;

import cc.allio.uno.data.sql.expression.Expression;
import cc.allio.uno.data.sql.*;

import java.util.Collection;

/**
 * whereSQL语句代理
 *
 * @author jiangwei
 * @date 2022/9/30 14:03
 * @see Where
 * @since 1.1.0
 */
public class WhereStatementDelegate extends AbstractStatementDelegate implements WhereDelegate {

    public WhereStatementDelegate(Select<SelectStatement> select, From<FromStatement> from, Where<WhereStatement> where, Group<GroupStatement> group, Order<OrderStatement> order, Limit<LimitStatement> limit) {
        super(select, from, where, group, order, limit);
    }

    public WhereStatementDelegate(Select<SelectStatement> select, From<FromStatement> from, Where<WhereStatement> where, Group<GroupStatement> group, Order<OrderStatement> order, Limit<LimitStatement> limit, Statement<?> parent) {
        super(select, from, where, group, order, limit, parent);
    }

    @Override
    public WhereDelegate gt(String fieldName, Object value) {
        getWhere().gt(fieldName, value);
        return self();
    }

    @Override
    public WhereDelegate gte(String fieldName, Object value) {
        getWhere().gte(fieldName, value);
        return self();
    }

    @Override
    public WhereDelegate lt(String fieldName, Object value) {
        getWhere().lt(fieldName, value);
        return self();
    }

    @Override
    public WhereDelegate lte(String fieldName, Object value) {
        getWhere().lte(fieldName, value);
        return self();
    }

    @Override
    public WhereDelegate eq(String fieldName, Object value) {
        getWhere().eq(fieldName, value);
        return self();
    }

    @Override
    public WhereDelegate notNull(String fieldName) {
        getWhere().notNull(fieldName);
        return self();
    }

    @Override
    public WhereDelegate isNull(String fieldName) {
        getWhere().isNull(fieldName);
        return self();
    }

    @Override
    public WhereDelegate in(String fieldName, Object[] values) {
        getWhere().in(fieldName, values);
        return self();
    }

    @Override
    public WhereDelegate between(String fieldName, Object withValue, Object endValue) {
        getWhere().between(fieldName, withValue, endValue);
        return self();
    }

    @Override
    public WhereDelegate notBetween(String fieldName, Object withValue, Object endValue) {
        getWhere().notBetween(fieldName, withValue, endValue);
        return self();
    }

    @Override
    public WhereDelegate $like(String fieldName, Object value) {
        getWhere().$like(fieldName, value);
        return self();
    }

    @Override
    public WhereDelegate like$(String fieldName, Object value) {
        getWhere().like$(fieldName, value);
        return self();
    }

    @Override
    public WhereDelegate $like$(String fieldName, Object value) {
        getWhere().$like$(fieldName, value);
        return self();
    }

    @Override
    public WhereDelegate or() {
        getWhere().or();
        return self();
    }

    @Override
    public WhereDelegate and() {
        getWhere().and();
        return self();
    }

    @Override
    public String getSQL() throws SQLException {
        if (getParent() != null) {
            return getParent().getSQL();
        }
        return getWhere().getSQL();
    }

    @Override
    public Collection<Expression> getExpressions() {
        if (getParent() != null) {
            return getParent().getExpressions();
        }
        return getWhere().getExpressions();
    }

    @Override
    public void syntaxCheck() throws SQLException {
        getWhere().syntaxCheck();
    }

    @Override
    public int order() {
        return getWhere().order();
    }

    @Override
    public Collection<RuntimeColumn> getColumns() {
        return getWhere().getColumns();
    }
}
