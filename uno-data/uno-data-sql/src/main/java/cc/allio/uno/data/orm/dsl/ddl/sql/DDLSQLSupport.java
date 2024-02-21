package cc.allio.uno.data.orm.dsl.ddl.sql;

import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DataType;
import cc.allio.uno.data.orm.dsl.type.DruidDataTypeAdapter;
import cc.allio.uno.data.orm.dsl.type.DruidDbTypeAdapter;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import com.alibaba.druid.sql.ast.statement.*;

/**
 * 与DDL SQL相关的操作
 *
 * @author jiangwei
 * @date 2024/2/8 13:46
 * @since 1.1.6
 */
public class DDLSQLSupport extends SQLSupport {

    public DDLSQLSupport(Operator<?> druidOperator) {
        super(druidOperator);
    }

    /**
     * 基于{@link Table}创建{@link SQLExprTableSource}实例
     *
     * @param table table
     * @return SQLExprTableSource
     */
    public static SQLExprTableSource createTableSource(Table table, DBType dbType) {
        var druidType = DruidDbTypeAdapter.getInstance().adapt(dbType);
        SQLExprTableSource tableSource = new UnoSQLExprTableSource(druidType);
        tableSource.setExpr(table.getName().getName());
        tableSource.setSchema(table.getSchema());
        tableSource.setCatalog(table.getCatalog());
        return tableSource;
    }

    /**
     * 基于{@link ColumnDef}创建{@link SQLColumnDefinition}实例
     *
     * @param columnDef columnDef
     * @param dbType    dbType
     * @return SQLColumnDefinition
     */
    public static SQLColumnDefinition createColumnDefinition(ColumnDef columnDef, DBType dbType) {
        var druidType = DruidDbTypeAdapter.getInstance().adapt(dbType);
        SQLColumnDefinition sqlColumnDefinition = new SQLColumnDefinition();
        sqlColumnDefinition.setComment(columnDef.getComment());
        sqlColumnDefinition.setName(columnDef.getDslName().format());
        sqlColumnDefinition.setDbType(druidType);
        DataType dataType = columnDef.getDataType();
        SQLDataType druidDataType = DruidDataTypeAdapter.getInstance(dbType).adapt(dataType);
        if (druidType != null) {
            druidDataType.setDbType(druidType);
            sqlColumnDefinition.setDataType(druidDataType);
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
        return sqlColumnDefinition;
    }
}
