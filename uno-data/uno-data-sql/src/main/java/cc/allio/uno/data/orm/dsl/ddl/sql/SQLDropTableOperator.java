package cc.allio.uno.data.orm.dsl.ddl.sql;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.UnoSQLExprTableSource;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DruidDbTypeAdapter;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.SQLDropTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.DropTableOperator;

/**
 * DruidSQLDropTableOperator
 *
 * @author jiangwei
 * @date 2023/4/16 13:02
 * @since 1.1.4
 */
@AutoService(DropTableOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class SQLDropTableOperator implements DropTableOperator {
    private final DbType druidDbType;
    private SQLDropTableStatement dropTableStatement;
    private Table table;

    public SQLDropTableOperator() {
        this(DBType.getSystemDbType());
    }

    public SQLDropTableOperator(DBType dbType) {
        this.druidDbType = DruidDbTypeAdapter.getInstance().adapt(dbType);
        this.dropTableStatement = new SQLDropTableStatement();
        dropTableStatement.setDbType(druidDbType);
    }

    @Override
    public String getDSL() {
        return SQLUtils.toSQLString(dropTableStatement);
    }

    @Override
    public DropTableOperator parse(String dsl) {
        return null;
    }

    @Override
    public void reset() {
        // eachReset
        this.dropTableStatement = new SQLDropTableStatement();
        dropTableStatement.setDbType(druidDbType);
    }

    @Override
    public DropTableOperator from(Table table) {
        SQLExprTableSource tableSource = new UnoSQLExprTableSource(druidDbType);
        tableSource.setExpr(table.getName().getName());
        tableSource.setSchema(table.getSchema());
        tableSource.setCatalog(table.getCatalog());
        this.table = table;
        dropTableStatement.addTableSource(tableSource);
        return self();
    }

    @Override
    public Table getTables() {
        return table;
    }

    @Override
    public DropTableOperator ifExist(Boolean ifExist) {
        dropTableStatement.setIfExists(ifExist);
        return self();
    }
}
