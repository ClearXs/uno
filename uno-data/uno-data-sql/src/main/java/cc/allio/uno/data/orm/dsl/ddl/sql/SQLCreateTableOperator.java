package cc.allio.uno.data.orm.dsl.ddl.sql;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DataType;
import cc.allio.uno.data.orm.dsl.type.DruidDbTypeAdapter;
import cc.allio.uno.data.orm.dsl.type.DruidDataTypeAdapter;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import cc.allio.uno.data.orm.dsl.ddl.CreateTableOperator;

import java.util.Objects;

/**
 * 基于Druid registry operator
 *
 * @author jiangwei
 * @date 2023/4/12 19:45
 * @since 1.1.4
 */
@AutoService(CreateTableOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class SQLCreateTableOperator implements CreateTableOperator {
    private final DBType dbType;
    private final DbType druidType;
    private SQLCreateTableStatement createTableStatement;
    private Table table;

    public SQLCreateTableOperator() {
        this(DBType.getSystemDbType());
    }

    public SQLCreateTableOperator(DBType dbType) {
        this.dbType = dbType;
        this.druidType = DruidDbTypeAdapter.getInstance().adapt(dbType);
        this.createTableStatement =
                switch (druidType) {
                    case DbType.mysql -> new MySqlCreateTableStatement();
                    case DbType.oracle -> new OracleCreateTableStatement();
                    default -> new SQLCreateTableStatement();
                };
    }

    @Override
    public String getDSL() {
        return SQLUtils.toSQLString(createTableStatement);
    }

    @Override
    public CreateTableOperator parse(String dsl) {
        this.createTableStatement = (SQLCreateTableStatement) SQLUtils.parseSingleStatement(dsl, druidType);
        return self();
    }

    @Override
    public void reset() {
        // eachReset
        this.createTableStatement = new SQLCreateTableStatement(druidType);
    }

    @Override
    public CreateTableOperator from(String table) {
        return from(Table.of(table));
    }

    @Override
    public CreateTableOperator from(Table table) {
        this.table = table;
        SQLExprTableSource tableSource = new UnoSQLExprTableSource(druidType);
        tableSource.setExpr(table.getName().getName());
        tableSource.setSchema(table.getSchema());
        tableSource.setCatalog(table.getCatalog());
        createTableStatement.setTableSource(tableSource);
        return self();
    }

    @Override
    public Table getTables() {
        return table;
    }

    @Override
    public CreateTableOperator column(ColumnDef columnDef) {
        SQLColumnDefinition sqlColumnDefinition = new SQLColumnDefinition();
        sqlColumnDefinition.setComment(columnDef.getComment());
        sqlColumnDefinition.setName(columnDef.getDslName().format());
        sqlColumnDefinition.setDbType(druidType);
        DataType dataType = columnDef.getDataType();
        SQLDataType druidType = DruidDataTypeAdapter.getInstance(dbType).adapt(dataType);
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
        if (columnDef.getDefaultValue() != null) {
            SQLValuableExpr defaultValueExpr = SQLSupport.newSQLValue(dataType, columnDef.getDefaultValue());
            sqlColumnDefinition.setDefaultExpr(defaultValueExpr);
        }
        createTableStatement.addColumn(sqlColumnDefinition);
        return self();
    }

    @Override
    public CreateTableOperator comment(String comment) {
        createTableStatement.setComment(new SQLIdentifierExpr(comment));
        return self();
    }
}
