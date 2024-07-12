package cc.allio.uno.data.orm.dsl.sql.dml;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.sql.SQLSupport;
import cc.allio.uno.data.orm.dsl.sql.UnoSQLExprTableSource;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.visitor.ParameterizedOutputVisitorUtils;
import cc.allio.uno.data.orm.dsl.dml.DeleteOperator;
import reactor.util.function.Tuple2;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * DruidSQLDeleteQueryOperator
 *
 * @author j.x
 * @date 2023/4/16 18:43
 * @since 1.1.4
 */
@AutoService(DeleteOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class SQLDeleteOperator extends SQLWhereOperatorImpl<SQLDeleteOperator> implements DeleteOperator<SQLDeleteOperator> {

    private DBType dbType;
    private DbType druidDbType;
    private Table table;
    private SQLDeleteStatement deleteStatement;

    public SQLDeleteOperator() {
        this(DBType.getSystemDbType());
    }

    public SQLDeleteOperator(DBType dbType) {
        super();
        this.dbType = dbType;
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.deleteStatement = new SQLDeleteStatement();
        deleteStatement.setDbType(druidDbType);
    }

    @Override
    public String getDSL() {
        return ParameterizedOutputVisitorUtils.restore(
                getPrepareDSL(),
                druidDbType,
                getPrepareValues().stream().map(PrepareValue::getValue).collect(Collectors.toList()));

    }

    @Override
    public SQLDeleteOperator parse(String dsl) {
        this.deleteStatement = (SQLDeleteStatement) SQLUtils.parseSingleStatement(dsl, druidDbType);
        SQLExpr where = this.deleteStatement.getWhere();
        if (SQLSupport.isBinaryExpr(where)) {
            this.deleteStatement.setWhere(null);
            SQLSupport.binaryExprTraversal(
                    (SQLBinaryOpExpr) where,
                    (newExpr, mode, prepareValues) -> {
                        switchMode(mode);
                        appendAndSetWhere(newExpr);
                        for (Tuple2<String, Object> prepareValue : prepareValues) {
                            addPrepareValue(prepareValue.getT1(), prepareValue.getT2());
                        }
                    });
        }
        return self();
    }

    @Override
    public SQLDeleteOperator customize(UnaryOperator<SQLDeleteOperator> operatorFunc) {
        return operatorFunc.apply(new SQLDeleteOperator(dbType));
    }

    @Override
    public void reset() {
        super.reset();
        this.deleteStatement = new SQLDeleteStatement();
        deleteStatement.setDbType(druidDbType);
    }

    @Override
    public void setDBType(DBType dbType) {
        this.dbType = dbType;
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.deleteStatement.setDbType(this.druidDbType);
    }

    @Override
    public DBType getDBType() {
        return dbType;
    }

    @Override
    public String getPrepareDSL() {
        return SQLUtils.toSQLString(deleteStatement);
    }

    @Override
    public SQLDeleteOperator from(Table table) {
        SQLExprTableSource tableSource = new UnoSQLExprTableSource(druidDbType);
        tableSource.setExpr(new SQLIdentifierExpr(table.getName().format()));
        tableSource.setCatalog(table.getCatalog());
        tableSource.setSchema(table.getSchema());
        this.table = table;
        deleteStatement.setTableSource(tableSource);
        return self();
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    protected DbType getDruidType() {
        return druidDbType;
    }

    @Override
    protected SQLObject getSQLObject() {
        return deleteStatement;
    }

    @Override
    protected Consumer<SQLExpr> getSetWhere() {
        return where -> deleteStatement.setWhere(where);
    }
}
