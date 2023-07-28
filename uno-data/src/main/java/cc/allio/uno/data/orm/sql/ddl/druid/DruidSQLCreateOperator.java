package cc.allio.uno.data.orm.sql.ddl.druid;

import cc.allio.uno.data.orm.type.DBType;
import cc.allio.uno.data.orm.type.DataType;
import cc.allio.uno.data.orm.type.druid.DruidDbTypeAdapter;
import cc.allio.uno.data.orm.type.druid.DruidTypeAdapter;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.sql.Table;
import cc.allio.uno.data.orm.sql.SQLColumnDef;
import cc.allio.uno.data.orm.sql.ddl.SQLCreateTableOperator;

import java.util.List;

/**
 * 基于Druid create operator
 *
 * @author jiangwei
 * @date 2023/4/12 19:45
 * @since 1.1.4
 */
public class DruidSQLCreateOperator implements SQLCreateTableOperator {

    private SQLCreateTableStatement createTableStatement;
    private final DbType druidType;

    public DruidSQLCreateOperator(DBType dbType) {
        this.druidType = DruidDbTypeAdapter.getInstance().get(dbType);
        SQLCreateTableStatement druidCreateTableStatement;
        switch (druidType) {
            case mysql:
                druidCreateTableStatement = new MySqlCreateTableStatement();
                break;
            case oracle:
                druidCreateTableStatement = new OracleCreateTableStatement();
                break;
            default:
                druidCreateTableStatement = new SQLCreateTableStatement();
        }
        this.createTableStatement = druidCreateTableStatement;
    }

    @Override
    public String getSQL() {
        return SQLUtils.toSQLString(createTableStatement);
    }

    @Override
    public SQLCreateTableOperator parse(String sql) {
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, druidType);
        if (CollectionUtils.isNotEmpty(sqlStatements)) {
            this.createTableStatement = (SQLCreateTableStatement) sqlStatements.get(0);
        }
        return self();
    }

    @Override
    public void reset() {
        // reset
        this.createTableStatement = new SQLCreateTableStatement(druidType);
    }

    @Override
    public SQLCreateTableOperator from(String table) {
        createTableStatement.setTableName(table);
        return self();
    }

    @Override
    public SQLCreateTableOperator from(Table table) {
        createTableStatement.setTableName(table.getName().format());
        return self();
    }

    @Override
    public SQLCreateTableOperator column(SQLColumnDef columnDef) {
        SQLColumnDefinition sqlColumnDefinition = new SQLColumnDefinition();
        sqlColumnDefinition.setComment(columnDef.getComment());
        sqlColumnDefinition.setName(columnDef.getSqlName().format());
        sqlColumnDefinition.setDbType(druidType);
        DataType dataType = columnDef.getDataType();
        SQLDataType druidType = DruidTypeAdapter.getInstance().get(dataType);
        if (druidType != null) {
            druidType.setDbType(this.druidType);
            sqlColumnDefinition.setDataType(druidType);
        }
        if (columnDef.isPk()) {
            SQLColumnPrimaryKey sqlPrimaryKey = new SQLColumnPrimaryKey();
            sqlColumnDefinition.addConstraint(sqlPrimaryKey);
        }
        if (columnDef.isFk()) {
            SQLColumnReference sqlColumnReference = new SQLColumnReference();
            sqlColumnDefinition.addConstraint(sqlColumnReference);
        }
        if (columnDef.isNull()) {
            SQLNullConstraint sqlNullConstraint = new SQLNullConstraint();
            sqlColumnDefinition.addConstraint(sqlNullConstraint);
        }
        if (columnDef.isNonNull()) {
            SQLNotNullConstraint sqlNotNullConstraint = new SQLNotNullConstraint();
            sqlColumnDefinition.addConstraint(sqlNotNullConstraint);
        }
        if (columnDef.isUnique()) {
            SQLColumnUniqueKey sqlColumnUniqueKey = new SQLColumnUniqueKey();
            sqlColumnDefinition.addConstraint(sqlColumnUniqueKey);
        }
        createTableStatement.addColumn(sqlColumnDefinition);
        return self();
    }

    @Override
    public SQLCreateTableOperator schemaName(String schemaName) {
        createTableStatement.setSchema(schemaName);
        return self();
    }

    @Override
    public SQLCreateTableOperator comment(String comment) {
        return self();
    }
}
