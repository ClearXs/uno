package cc.allio.uno.data.orm.dsl.sql.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.sql.SQLSupport;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DruidDbTypeAdapter;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import cc.allio.uno.data.orm.dsl.ddl.CreateTableOperator;

import java.util.function.UnaryOperator;

/**
 * 基于Druid registry operator
 *
 * @author j.x
 * @date 2023/4/12 19:45
 * @since 1.1.4
 */
@AutoService(CreateTableOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class SQLCreateTableOperator implements CreateTableOperator<SQLCreateTableOperator> {

    private DBType dbType;
    private DbType druidType;
    private Table table;
    private SQLCreateTableStatement createTableStatement;

    public SQLCreateTableOperator() {
        this(DBType.getSystemDbType());
    }

    public SQLCreateTableOperator(DBType dbType) {
        this.dbType = dbType;
        this.druidType = SQLSupport.translateDb(dbType);
        if (druidType == DbType.mysql) {
            this.createTableStatement = new MySqlCreateTableStatement();
        } else if (druidType == DbType.oracle) {
            this.createTableStatement = new OracleCreateTableStatement();
        } else {
            this.createTableStatement = new SQLCreateTableStatement();
        }
    }

    @Override
    public String getDSL() {
        return SQLUtils.toSQLString(createTableStatement);
    }

    @Override
    public SQLCreateTableOperator parse(String dsl) {
        this.createTableStatement = (SQLCreateTableStatement) SQLUtils.parseSingleStatement(dsl, druidType);
        return self();
    }

    @Override
    public SQLCreateTableOperator customize(UnaryOperator<SQLCreateTableOperator> operatorFunc) {
        return operatorFunc.apply(new SQLCreateTableOperator(dbType));
    }

    @Override
    public void reset() {
        // eachReset
        this.createTableStatement = new SQLCreateTableStatement(druidType);
    }

    @Override
    public void setDBType(DBType dbType) {
        this.dbType = dbType;
        this.druidType = DruidDbTypeAdapter.getInstance().adapt(dbType);
        this.createTableStatement.setDbType(this.druidType);
    }

    @Override
    public DBType getDBType() {
        return dbType;
    }

    @Override
    public SQLCreateTableOperator from(String table) {
        return from(Table.of(table));
    }

    @Override
    public SQLCreateTableOperator from(Table table) {
        this.table = table;
        SQLExprTableSource tableSource = DDLSQLSupport.createTableSource(table, dbType);
        createTableStatement.setTableSource(tableSource);
        return self();
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public SQLCreateTableOperator column(ColumnDef columnDef) {
        SQLColumnDefinition columnDefinition = DDLSQLSupport.createColumnDefinition(columnDef, dbType);
        createTableStatement.addColumn(columnDefinition);
        return self();
    }

    @Override
    public SQLCreateTableOperator comment(String comment) {
        createTableStatement.setComment(new SQLIdentifierExpr(comment));
        return self();
    }
}
