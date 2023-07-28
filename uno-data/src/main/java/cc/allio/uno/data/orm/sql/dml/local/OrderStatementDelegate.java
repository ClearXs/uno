package cc.allio.uno.data.orm.sql.dml.local;

import cc.allio.uno.data.orm.sql.*;
import cc.allio.uno.data.orm.sql.dml.*;
import cc.allio.uno.data.orm.sql.dml.local.expression.Expression;

import java.util.Collection;

/**
 * orderSQL语句代理
 *
 * @author jiangwei
 * @date 2022/9/30 14:11
 * @since 1.1.0
 */
public class OrderStatementDelegate extends AbstractStatementDelegate implements OrderDelegate {

    public OrderStatementDelegate(Select<SelectStatement> select, From<FromStatement> from, Where<WhereStatement> where, Group<GroupStatement> group, Order<OrderStatement> order, Limit<LimitStatement> limit) {
        super(select, from, where, group, order, limit);
    }

    public OrderStatementDelegate(Select<SelectStatement> select, From<FromStatement> from, Where<WhereStatement> where, Group<GroupStatement> group, Order<OrderStatement> order, Limit<LimitStatement> limit, Statement<?> parent) {
        super(select, from, where, group, order, limit, parent);
    }

    @Override
    public OrderDelegate byAsc(String fieldName) {
        getOrder().byAsc(fieldName);
        return self();
    }

    @Override
    public OrderDelegate byDesc(String fieldName) {
        getOrder().byDesc(fieldName);
        return self();
    }

    @Override
    public OrderDelegate orderBy(String fieldName, String order) {
        getOrder().orderBy(fieldName, order);
        return self();
    }

    @Override
    public OrderDelegate orderBy(String fieldName, OrderCondition orderCondition) {
        getOrder().orderBy(fieldName, orderCondition);
        return self();
    }

    @Override
    public String getSQL() throws SQLException {
        if (getParent() != null) {
            return getParent().getSQL();
        }
        return getOrder().getSQL();
    }

    @Override
    public String getCondition() {
        return getOrder().getCondition();
    }

    @Override
    public Collection<Expression> getExpressions() {
        if (getParent() != null) {
            return getParent().getExpressions();
        }
        return getOrder().getExpressions();
    }

    @Override
    public void syntaxCheck() throws SQLException {
        getOrder().syntaxCheck();
    }

    @Override
    public int order() {
        return getOrder().order();
    }

    @Override
    public Collection<RuntimeColumn> getColumns() {
        return getOrder().getColumns();
    }
}
