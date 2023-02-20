package cc.allio.uno.data.sql.query;

import cc.allio.uno.data.sql.FromStatement;
import cc.allio.uno.data.sql.SQLException;
import cc.allio.uno.data.sql.Statement;
import cc.allio.uno.data.sql.expression.Expression;
import cc.allio.uno.data.sql.From;

import java.util.Collection;

/**
 * from statement delegate
 *
 * @author jiangwei
 * @date 2023/1/11 13:27
 * @since 1.1.4
 */
public class QueryFromStatement extends AbstractStatementDelegate implements QueryFrom {

    public QueryFromStatement(Select<SelectStatement> select, From<FromStatement> from, Where<WhereStatement> where, Group<GroupStatement> group, Order<OrderStatement> order, Limit<LimitStatement> limit) {
        super(select, from, where, group, order, limit);
    }

    public QueryFromStatement(Select<SelectStatement> select, From<FromStatement> from, Where<WhereStatement> where, Group<GroupStatement> group, Order<OrderStatement> order, Limit<LimitStatement> limit, Statement<?> parent) {
        super(select, from, where, group, order, limit, parent);
    }

    @Override
    public QueryFrom from(Class<?> tableEntity) throws SQLException {
        getFrom().from(tableEntity);
        return self();
    }

    @Override
    public QueryFrom from(String table) throws SQLException {
        getFrom().from(table);
        return self();
    }

    @Override
    public String getSQL() throws SQLException {
        if (getParent() != null) {
            return getParent().getSQL();
        }
        return getFrom().getSQL();
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
