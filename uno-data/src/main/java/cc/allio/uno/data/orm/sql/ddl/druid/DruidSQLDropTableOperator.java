package cc.allio.uno.data.orm.sql.ddl.druid;

import cc.allio.uno.data.orm.type.DBType;
import cc.allio.uno.data.orm.type.druid.DruidDbTypeAdapter;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.SQLDropTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import cc.allio.uno.data.orm.sql.Table;
import cc.allio.uno.data.orm.sql.ddl.SQLDropTableOperator;

/**
 * DruidSQLDropTableOperator
 *
 * @author jiangwei
 * @date 2023/4/16 13:02
 * @since 1.1.4
 */
public class DruidSQLDropTableOperator implements SQLDropTableOperator {
    private SQLDropTableStatement dropTableStatement;
    private final DbType druidDbType;

    public DruidSQLDropTableOperator(DBType dbType) {
        this.druidDbType = DruidDbTypeAdapter.getInstance().get(dbType);
        this.dropTableStatement = new SQLDropTableStatement();
        dropTableStatement.setDbType(druidDbType);
    }

    @Override
    public String getSQL() {
        return SQLUtils.toSQLString(dropTableStatement);
    }

    @Override
    public SQLDropTableOperator parse(String sql) {
        return null;
    }

    @Override
    public void reset() {
        // reset
        this.dropTableStatement = new SQLDropTableStatement();
        dropTableStatement.setDbType(druidDbType);
    }

    @Override
    public SQLDropTableOperator from(Table table) {
        dropTableStatement.addTableSource(new SQLExprTableSource(table.getName().format()));
        return self();
    }

    @Override
    public SQLDropTableOperator ifExist(Boolean ifExist) {
        dropTableStatement.setIfExists(ifExist);
        return self();
    }
}
