package cc.allio.uno.data.orm.dsl.sql.ddl;

import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.sql.SQLSupport;
import cc.allio.uno.data.orm.dsl.sql.UnoSQLExprTableSource;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DataType;
import cc.allio.uno.data.orm.dsl.type.DruidDataTypeAdapter;
import cc.allio.uno.data.orm.dsl.type.DruidDbTypeAdapter;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import com.alibaba.druid.sql.ast.statement.*;

/**
 * 与DDL SQL相关的操作
 *
 * @author j.x
 * @since 1.1.7
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
        // exclude pg DB type
        if (dbType != DBType.POSTGRESQL) {
            sqlColumnDefinition.setComment(columnDef.getComment());
        }
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

    /**
     * of druid {@link SQLCommentStatement} instance
     *
     * @param comment the table comment
     * @param table the {@link Table} instance
     * @param dbType the {@link DBType}
     * @return the table {@link SQLCommentStatement} instance
     */
    public static SQLCommentStatement createTableCommentStatement(String comment, Table table, DBType dbType) {
        var druidType = DruidDbTypeAdapter.getInstance().adapt(dbType);
        SQLCommentStatement tableComment = new SQLCommentStatement();
        SQLExprTableSource tableSource = DDLSQLSupport.createTableSource(table, dbType);
        tableComment.setComment(new SQLCharExpr(comment));
        tableComment.setType(SQLCommentStatement.Type.TABLE);
        tableComment.setOn(tableSource);
        tableComment.setDbType(druidType);
        return tableComment;
    }

    /**
     * of druid {@link SQLCommentStatement} instance
     *
     * @param columnDef the {@link ColumnDef} instance
     * @param table     the {@link Table} instance
     * @param dbType    the {@link DbType}
     * @return the column {@link SQLCommentStatement} instance
     */
    public static SQLCommentStatement createCommentStatement(ColumnDef columnDef, Table table, DBType dbType) {
        var druidType = DruidDbTypeAdapter.getInstance().adapt(dbType);
        String columnCommentInfo = columnDef.getComment();
        SQLCommentStatement columnComment = new SQLCommentStatement();
        SQLExprTableSource tableSource = new SQLExprTableSource();
        SQLPropertyExpr columnExpr = new SQLPropertyExpr();

        // set comment from table
        SQLPropertyExpr tableExpr = new SQLPropertyExpr();
        tableExpr.setOwner(new SQLIdentifierExpr(table.getSchema()));
        tableExpr.setName(table.getName().format());

        // set comment column name
        columnExpr.setName(columnDef.getDslName().format());
        // set own
        columnExpr.setOwner(tableExpr);

        tableSource.setExpr(columnExpr);

        columnComment.setComment(new SQLCharExpr(columnCommentInfo));
        columnComment.setOn(tableSource);
        columnComment.setType(SQLCommentStatement.Type.COLUMN);
        columnComment.setDbType(druidType);

        return columnComment;
    }
}
