package cc.allio.uno.data.orm.sql.dml.druid;

import cc.allio.uno.data.orm.sql.dml.local.expression.Expression;
import cc.allio.uno.data.orm.sql.JoinType;
import cc.allio.uno.data.orm.sql.SQLException;
import cc.allio.uno.data.orm.sql.dml.JoinFrom;

import java.util.Collection;

public class DruidJoinFrom implements JoinFrom<DruidJoinFrom> {

    private final DruidSelectFrom parent;
    private final JoinType joinType;

    public DruidJoinFrom(DruidSelectFrom parent, JoinType joinType) {
        this.parent = parent;
        this.joinType = joinType;
    }

    @Override
    public DruidJoinFrom from(Class<?> tableEntity) throws SQLException {
        return null;
    }

    @Override
    public DruidJoinFrom from(String table) throws SQLException {
        return null;
    }

    @Override
    public DruidJoinFrom from(String table, String alias) {
        return null;
    }

    @Override
    public String getSQL() throws SQLException {
        return null;
    }

    @Override
    public String getCondition() {
        return null;
    }

    @Override
    public Collection<Expression> getExpressions() {
        return null;
    }

    @Override
    public void syntaxCheck() throws SQLException {

    }

    @Override
    public int order() {
        return 0;
    }
}
