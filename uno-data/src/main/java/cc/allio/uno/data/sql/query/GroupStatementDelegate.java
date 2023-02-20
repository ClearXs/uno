package cc.allio.uno.data.sql.query;

import cc.allio.uno.data.sql.expression.Expression;
import cc.allio.uno.data.sql.*;

import java.util.Collection;

/**
 * groupSQL语句代码
 *
 * @author jiangwei
 * @date 2022/9/30 14:13
 * @since 1.1.0
 */
public class GroupStatementDelegate extends AbstractStatementDelegate implements GroupDelegate {

    public GroupStatementDelegate(Select<SelectStatement> select, From<FromStatement> from, Where<WhereStatement> where, Group<GroupStatement> group, Order<OrderStatement> order, Limit<LimitStatement> limit) {
        super(select, from, where, group, order, limit);
    }

    public GroupStatementDelegate(Select<SelectStatement> select, From<FromStatement> from, Where<WhereStatement> where, Group<GroupStatement> group, Order<OrderStatement> order, Limit<LimitStatement> limit, Statement<?> parent) {
        super(select, from, where, group, order, limit, parent);
    }

    @Override
    public GroupDelegate byOne(String fieldName) {
        getGroup().byOne(fieldName);
        return self();
    }

    @Override
    public GroupDelegate byOnes(String... fieldNames) {
        getGroup().byOnes(fieldNames);
        return self();
    }

    @Override
    public GroupDelegate byOnes(Collection<String> fieldNames) {
        getGroup().byOnes(fieldNames);
        return self();
    }

    @Override
    public String getSQL() throws SQLException {
        if (getParent() != null) {
            return getParent().getSQL();
        }
        return getGroup().getSQL();
    }

    @Override
    public Collection<Expression> getExpressions() {
        if (getParent() != null) {
            return getParent().getExpressions();
        }
        return getGroup().getExpressions();
    }

    @Override
    public void syntaxCheck() throws SQLException {
        getGroup().syntaxCheck();
    }

    @Override
    public int order() {
        return getGroup().order();
    }

    @Override
    public Collection<RuntimeColumn> getColumns() {
        return getGroup().getColumns();
    }
}
