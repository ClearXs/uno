package cc.allio.uno.data.orm.dsl.sql.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.sql.SQLSupport;
import cc.allio.uno.data.orm.dsl.sql.UnoSQLExprTableSource;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.SQLDropTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import cc.allio.uno.data.orm.dsl.ddl.DropTableOperator;

import java.util.function.UnaryOperator;

/**
 * DruidSQLDropTableOperator
 *
 * @author j.x
 * @since 1.1.4
 */
@AutoService(DropTableOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class SQLDropTableOperator implements DropTableOperator<SQLDropTableOperator> {

    private DBType dbType;
    private DbType druidDbType;
    private Table table;
    private SQLDropTableStatement dropTableStatement;

    public SQLDropTableOperator() {
        this(DBType.getSystemDbType());
    }

    public SQLDropTableOperator(DBType dbType) {
        this.dbType = dbType;
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.dropTableStatement = new SQLDropTableStatement();
        this.dropTableStatement.setDbType(druidDbType);
    }

    @Override
    public String getDSL() {
        return SQLUtils.toSQLString(dropTableStatement);
    }

    @Override
    public SQLDropTableOperator parse(String dsl) {
        return null;
    }

    @Override
    public SQLDropTableOperator customize(UnaryOperator<SQLDropTableOperator> operatorFunc) {
        return operatorFunc.apply(new SQLDropTableOperator(dbType));
    }

    @Override
    public void reset() {
        // eachReset
        this.dropTableStatement = new SQLDropTableStatement();
        this.dropTableStatement.setDbType(druidDbType);
    }

    @Override
    public void setDBType(DBType dbType) {
        this.dbType = dbType;
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.dropTableStatement.setDbType(this.druidDbType);
    }

    @Override
    public DBType getDBType() {
        return dbType;
    }

    @Override
    public SQLDropTableOperator from(Table table) {
        SQLExprTableSource tableSource = new UnoSQLExprTableSource(druidDbType);
        tableSource.setExpr(table.getName().getName());
        tableSource.setSchema(table.getSchema());
        tableSource.setCatalog(table.getCatalog());
        this.table = table;
        dropTableStatement.addTableSource(tableSource);
        return self();
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public SQLDropTableOperator ifExist(Boolean ifExist) {
        dropTableStatement.setIfExists(ifExist);
        return self();
    }
}
