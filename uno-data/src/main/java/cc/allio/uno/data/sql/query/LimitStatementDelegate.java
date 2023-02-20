package cc.allio.uno.data.sql.query;

import cc.allio.uno.data.sql.FromStatement;
import cc.allio.uno.data.sql.SQLException;
import cc.allio.uno.data.sql.Statement;
import cc.allio.uno.data.sql.expression.Expression;
import cc.allio.uno.data.sql.From;

import java.util.Collection;

/**
 * limit statement delegate
 *
 * @author jiangwei
 * @date 2023/1/11 19:19
 * @since 1.1.4
 */
public class LimitStatementDelegate extends AbstractStatementDelegate implements LimitDelegate {

    public LimitStatementDelegate(Select<SelectStatement> select, From<FromStatement> from, Where<WhereStatement> where, Group<GroupStatement> group, Order<OrderStatement> order, Limit<LimitStatement> limit) {
        super(select, from, where, group, order, limit);
    }

    public LimitStatementDelegate(Select<SelectStatement> select, From<FromStatement> from, Where<WhereStatement> where, Group<GroupStatement> group, Order<OrderStatement> order, Limit<LimitStatement> limit, Statement<?> parent) {
        super(select, from, where, group, order, limit, parent);
    }

    @Override
    public String getSQL() throws SQLException {
        return getLimit().getSQL();
    }

    @Override
    public Collection<Expression> getExpressions() {
        return getLimit().getExpressions();
    }

    @Override
    public void syntaxCheck() throws SQLException {
        getLimit().syntaxCheck();
    }

    @Override
    public int order() {
        return getLimit().order();
    }

    @Override
    public LimitDelegate limit(Integer number) {
        getLimit().limit(number);
        return self();
    }

    @Override
    public LimitDelegate offset(Integer number) {
        getLimit().offset(number);
        return self();
    }

}
