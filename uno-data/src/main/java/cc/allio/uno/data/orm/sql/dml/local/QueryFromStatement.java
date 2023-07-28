package cc.allio.uno.data.orm.sql.dml.local;

import cc.allio.uno.data.orm.sql.*;
import cc.allio.uno.data.orm.sql.dml.*;
import cc.allio.uno.data.orm.sql.dml.local.expression.Expression;

import java.util.Collection;

/**
 * from statement delegate
 *
 * @author jiangwei
 * @date 2023/1/11 13:27
 * @since 1.1.4
 */
public class QueryFromStatement extends AbstractStatementDelegate implements SelectFrom<QueryFromStatement> {

    public QueryFromStatement(Select<SelectStatement> select, From<FromStatement> from, Where<WhereStatement> where, Group<GroupStatement> group, Order<OrderStatement> order, Limit<LimitStatement> limit) {
        super(select, from, where, group, order, limit);
    }

    public QueryFromStatement(Select<SelectStatement> select, From<FromStatement> from, Where<WhereStatement> where, Group<GroupStatement> group, Order<OrderStatement> order, Limit<LimitStatement> limit, Statement<?> parent) {
        super(select, from, where, group, order, limit, parent);
    }

    @Override
    public QueryFromStatement from(Class<?> tableEntity) throws SQLException {
        getFrom().from(tableEntity);
        return self();
    }

    @Override
    public QueryFromStatement from(String table) throws SQLException {
        getFrom().from(table);
        return self();
    }

    @Override
    public QueryFromStatement from(String table, String alias) {
        return null;
    }

    @Override
    public SubQueryFrom subQuery() {
        return null;
    }

    @Override
    public JoinFrom join(JoinType joinType) {
        return null;
    }

    @Override
    public String getSQL() throws SQLException {
        if (getParent() != null) {
            return getParent().getSQL();
        }
        return getFrom().getSQL();
    }

    @Override
    public String getCondition() {
        return null;
    }

    @Override
    public Collection<Expression> getExpressions() {
        if (getParent() != null) {
            return getParent().getExpressions();
        }
        return getFrom().getExpressions();
    }

    @Override
    public void syntaxCheck() throws SQLException {
        getFrom().syntaxCheck();
    }

    @Override
    public int order() {
        return getFrom().order();
    }
}
