package cc.allio.uno.data.orm.dsl.sql.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.StringUtils;
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
import com.google.common.collect.Lists;

import java.util.List;
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
    private String comment;
    private final List<ColumnDef> commentColumnDefList = Lists.newArrayList();

    public SQLCreateTableOperator() {
        this(DBType.getSystemDbType());
    }

    public SQLCreateTableOperator(DBType dbType) {
        this.dbType = dbType;
        this.druidType = SQLSupport.translateDb(dbType);
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
        comment(table.getComment());
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
        if (DBType.POSTGRESQL == dbType) {
            commentColumnDefList.add(columnDef);
        }
        SQLColumnDefinition columnDefinition = DDLSQLSupport.createColumnDefinition(columnDef, dbType);
        createTableStatement.addColumn(columnDefinition);
        return self();
    }

    @Override
    public SQLCreateTableOperator comment(String comment) {
        if (DBType.POSTGRESQL == dbType) {
            this.comment = comment;
        }

        createTableStatement.setComment(new SQLIdentifierExpr(comment));
        return self();
    }

    @Override
    public List<Operator<?>> getPostOperatorList() {
        List<Operator<?>> commentOperatorList = Lists.newArrayList();
        if (StringUtils.isNotBlank(comment)) {
            SQLCommentStatement tableComment = DDLSQLSupport.createTableCommentStatement(comment, table, dbType);
            String tableCommentSQL = SQLUtils.toSQLString(tableComment, druidType);
            commentOperatorList.add(Operator.from(tableCommentSQL));
        }

        for (ColumnDef columnDef : commentColumnDefList) {
            String columnCommentInfo = columnDef.getComment();
            if (StringUtils.isNotBlank(columnCommentInfo)) {
                SQLCommentStatement commentStatement = DDLSQLSupport.createCommentStatement(columnDef, table, dbType);
                String columnCommentSQL = SQLUtils.toSQLString(commentStatement, druidType);
                commentOperatorList.add(Operator.from(columnCommentSQL));
            }
        }
        return commentOperatorList;
    }
}
