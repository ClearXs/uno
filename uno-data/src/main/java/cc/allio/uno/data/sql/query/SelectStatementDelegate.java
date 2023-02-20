package cc.allio.uno.data.sql.query;

import cc.allio.uno.data.sql.expression.Expression;
import cc.allio.uno.data.sql.*;
import cc.allio.uno.data.sql.word.Distinct;

import java.util.Collection;

/**
 * select语句代理
 *
 * @author jiangwei
 * @date 2022/9/30 14:07
 * @see Select
 * @since 1.1.0
 */
public class SelectStatementDelegate extends AbstractStatementDelegate implements SelectDelegate {

    public SelectStatementDelegate(Select<SelectStatement> select, From<FromStatement> from, Where<WhereStatement> where, Group<GroupStatement> group, Order<OrderStatement> order, Limit<LimitStatement> limit) {
        super(select, from, where, group, order, limit);
    }

    public SelectStatementDelegate(Select<SelectStatement> select, From<FromStatement> from, Where<WhereStatement> where, Group<GroupStatement> group, Order<OrderStatement> order, Limit<LimitStatement> limit, Statement<?> parent) {
        super(select, from, where, group, order, limit, parent);
    }

    @Override
    public SelectDelegate select(String fieldName) {
        getSelect().select(fieldName);
        return self();
    }

    @Override
    public SelectDelegate select(String fieldName, String alias) {
        getSelect().select(fieldName, alias);
        return self();
    }

    @Override
    public SelectDelegate select(String[] fieldNames) {
        getSelect().select(fieldNames);
        return self();
    }

    @Override
    public SelectDelegate select(Collection<String> fieldNames) {
        getSelect().select(fieldNames);
        return self();
    }

    @Override
    public SelectDelegate distinct() {
        getSelect().distinct();
        return self();
    }

    @Override
    public SelectDelegate distinctOn(String fieldName, String alias) {
        getSelect().distinctOn(fieldName, alias);
        return self();
    }

    @Override
    public SelectDelegate function(String syntax, String fieldName, String alias, Distinct distinct) {
        getSelect().function(syntax, fieldName, alias, distinct);
        return self();
    }

    @Override
    public String getSQL() throws SQLException {
        if (getParent() != null) {
            return getParent().getSQL();
        }
        return getSelect().getSQL();
    }

    @Override
    public Collection<Expression> getExpressions() {
        if (getParent() != null) {
            return getParent().getExpressions();
        }
        return getSelect().getExpressions();
    }

    @Override
    public void syntaxCheck() throws SQLException {
        getSelect().syntaxCheck();
    }

    @Override
    public int order() {
        return getSelect().order();
    }

    @Override
    public Collection<RuntimeColumn> getColumns() {
        return getSelect().getColumns();
    }
}
