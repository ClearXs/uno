package cc.allio.uno.data.orm.dsl.sql.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.exception.DDLException;
import cc.allio.uno.data.orm.dsl.sql.SQLSupport;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DruidDataTypeAdapter;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import com.alibaba.druid.sql.ast.statement.*;
import cc.allio.uno.data.orm.dsl.ddl.AlterTableOperator;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * druid for modify xxxx structure
 *
 * @author j.x
 * @since 1.1.4
 */
@AutoService(AlterTableOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class SQLAlterTableOperator implements AlterTableOperator<SQLAlterTableOperator> {

    private SQLAlterTableStatement alterTableStatement;
    private DBType dbType;
    private DbType druidDbType;
    private Table from;

    private final List<ColumnDef> commentColumnDefList = Lists.newArrayList();

    public SQLAlterTableOperator() {
        this(DBType.getSystemDbType());
    }

    public SQLAlterTableOperator(DBType dbType) {
        this.dbType = dbType;
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.alterTableStatement = new SQLAlterTableStatement(druidDbType);
    }

    /**
     * {@link com.alibaba.druid.sql.visitor.SQLASTOutputVisitor#visit(SQLAlterTableAlterColumn)}
     */
    @Override
    public String getDSL() {
        if (from == null) {
            throw new DDLException("alter table rename requirement 'from table', you can through #from invoke support 'from table'");
        }
        String sql = SQLUtils.toSQLString(alterTableStatement);
        if (DBType.POSTGRESQL == dbType) {
            // 特殊处理，去除SQLASTOutputVisitor#visit(SQLAlterTableAlterColumn)的DATA TYPE，替换为TYPE
            return sql.replace("SET DATA TYPE", "TYPE");
        }
        return sql;
    }

    @Override
    public SQLAlterTableOperator parse(String dsl) {
        this.alterTableStatement = (SQLAlterTableStatement) SQLUtils.parseSingleStatement(dsl, druidDbType);
        return self();
    }

    @Override
    public SQLAlterTableOperator customize(UnaryOperator<SQLAlterTableOperator> operatorFunc) {
        return operatorFunc.apply(new SQLAlterTableOperator(dbType));
    }

    @Override
    public void reset() {
        this.alterTableStatement = new SQLAlterTableStatement(druidDbType);
        this.from = null;
    }

    @Override
    public void setDBType(DBType dbType) {
        this.dbType = dbType;
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.alterTableStatement.setDbType(this.druidDbType);
    }

    @Override
    public DBType getDBType() {
        return dbType;
    }

    @Override
    public SQLAlterTableOperator alertColumns(Collection<ColumnDef> columnDefs) {
        for (ColumnDef columnDef : columnDefs) {
            SQLColumnDefinition columnDefinition = new SQLColumnDefinition();
            String comment = columnDef.getComment();
            if (dbType == DBType.POSTGRESQL) {
                commentColumnDefList.add(columnDef);
            } else {
                columnDefinition.setComment(comment);
            }
            columnDefinition.setName(columnDef.getDslName().format());
            columnDefinition.setDbType(druidDbType);
            if (columnDef.getDataType() != null) {
                SQLAlterTableAlterColumn alterTableAlterColumn = new SQLAlterTableAlterColumn();
                SQLDataType druidDataType = DruidDataTypeAdapter.getInstance(dbType).adapt(columnDef.getDataType());
                alterTableAlterColumn.setDataType(druidDataType);
                alterTableAlterColumn.setColumn(columnDefinition);
                this.alterTableStatement.addItem(alterTableAlterColumn);
            }
            if (columnDef.isNonNull()) {
                SQLAlterTableAlterColumn alterTableAlterColumn = new SQLAlterTableAlterColumn();
                alterTableAlterColumn.setSetNotNull(true);
                alterTableAlterColumn.setColumn(columnDefinition);
                this.alterTableStatement.addItem(alterTableAlterColumn);
            }
            if (columnDef.getDefaultValue() != null && columnDef.getDataType() != null) {
                SQLValuableExpr defaultValueExpr = SQLSupport.newSQLValue(columnDef.getDataType(), columnDef.getDefaultValue());
                SQLAlterTableAlterColumn alterTableAlterColumn = new SQLAlterTableAlterColumn();
                alterTableAlterColumn.setSetDefault(defaultValueExpr);
                alterTableAlterColumn.setColumn(columnDefinition);
                this.alterTableStatement.addItem(alterTableAlterColumn);
            }
        }
        return self();
    }

    @Override
    public SQLAlterTableOperator addColumns(Collection<ColumnDef> columnDefs) {
        for (ColumnDef columnDef : columnDefs) {
            if (DBType.POSTGRESQL == dbType) {
                commentColumnDefList.add(columnDef);
            }
            SQLAlterTableAddColumn alterTableAddColumn = new SQLAlterTableAddColumn();
            SQLColumnDefinition columnDefinition = DDLSQLSupport.createColumnDefinition(columnDef, dbType);
            alterTableAddColumn.addColumn(columnDefinition);
            this.alterTableStatement.addItem(alterTableAddColumn);
        }
        return self();
    }

    @Override
    public SQLAlterTableOperator deleteColumns(Collection<DSLName> columns) {
        SQLAlterTableDropColumnItem dropColumnItem = new SQLAlterTableDropColumnItem();
        for (DSLName column : columns) {
            dropColumnItem.addColumn(new SQLIdentifierExpr(column.format()));
        }
        this.alterTableStatement.addItem(dropColumnItem);
        return self();
    }

    @Override
    public SQLAlterTableOperator rename(Table to) {
        SQLAlterTableRename alterTableRename = new SQLAlterTableRename();
        alterTableRename.setTo(new SQLIdentifierExpr(to.getName().format()));
        this.alterTableStatement.addItem(alterTableRename);
        return self();
    }

    @Override
    public SQLAlterTableOperator from(Table table) {
        this.from = table;
        SQLExprTableSource tableSource = DDLSQLSupport.createTableSource(table, dbType);
        this.alterTableStatement.setTableSource(tableSource);
        return self();
    }

    @Override
    public Table getTable() {
        return from;
    }

    @Override
    public List<Operator<?>> getPostOperatorList() {
        List<Operator<?>> commentOperatorList = Lists.newArrayList();
        for (ColumnDef columnDef : commentColumnDefList) {
            String columnCommentInfo = columnDef.getComment();
            if (StringUtils.isNotBlank(columnCommentInfo)) {
                SQLCommentStatement commentStatement = DDLSQLSupport.createCommentStatement(columnDef, from, dbType);
                String columnCommentSQL = SQLUtils.toSQLString(commentStatement, druidDbType);
                commentOperatorList.add(Operator.from(columnCommentSQL));
            }
        }
        return commentOperatorList;
    }
}
