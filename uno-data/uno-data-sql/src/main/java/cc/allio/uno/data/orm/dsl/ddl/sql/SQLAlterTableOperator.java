package cc.allio.uno.data.orm.dsl.ddl.sql;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DruidDbTypeAdapter;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.AlterTableOperator;

/**
 * druid for modify table structure
 *
 * @author jiangwei
 * @date 2023/6/8 19:55
 * @since 1.1.4
 */
@AutoService(AlterTableOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class SQLAlterTableOperator implements AlterTableOperator {

    private final SQLAlterTableStatement alterTableStatement;
    private final DbType druidDbType;

    public SQLAlterTableOperator() {
        this(DBType.getSystemDbType());
    }

    public SQLAlterTableOperator(DBType dbType) {
        this.druidDbType = DruidDbTypeAdapter.getInstance().adapt(dbType);
        this.alterTableStatement = new SQLAlterTableStatement(druidDbType);
    }

    @Override
    public String getDSL() {
        return null;
    }

    @Override
    public AlterTableOperator parse(String dsl) {
        return null;
    }

    @Override
    public void reset() {

    }

    @Override
    public AlterTableOperator from(Table table) {
        return null;
    }

    @Override
    public Table getTable() {
        return null;
    }
}
