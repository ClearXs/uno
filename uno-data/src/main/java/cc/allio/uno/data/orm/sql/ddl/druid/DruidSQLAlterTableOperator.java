package cc.allio.uno.data.orm.sql.ddl.druid;

import cc.allio.uno.data.orm.type.DBType;
import cc.allio.uno.data.orm.type.druid.DruidDbTypeAdapter;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import cc.allio.uno.data.orm.sql.Table;
import cc.allio.uno.data.orm.sql.ddl.SQLAlterTableOperator;

/**
 * druid for modify table structure
 *
 * @author jiangwei
 * @date 2023/6/8 19:55
 * @since 1.1.4
 */
public class DruidSQLAlterTableOperator implements SQLAlterTableOperator {

    private final SQLAlterTableStatement alterTableStatement;
    private final DbType druidDbType;

    public DruidSQLAlterTableOperator(DBType dbType) {
        this.druidDbType = DruidDbTypeAdapter.getInstance().get(dbType);
        this.alterTableStatement = new SQLAlterTableStatement(druidDbType);

    }

    @Override
    public String getSQL() {
        return null;
    }

    @Override
    public SQLAlterTableOperator parse(String sql) {
        return null;
    }

    @Override
    public void reset() {

    }

    @Override
    public SQLAlterTableOperator from(Table table) {
        return null;
    }
}
