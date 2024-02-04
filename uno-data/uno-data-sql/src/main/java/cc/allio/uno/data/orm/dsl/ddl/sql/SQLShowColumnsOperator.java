package cc.allio.uno.data.orm.dsl.ddl.sql;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLShowColumnsStatement;
import cc.allio.uno.data.orm.dsl.ddl.ShowColumnsOperator;

import java.util.List;

/**
 * 表结构
 *
 * @author jiangwei
 * @date 2023/6/8 19:20
 * @since 1.1.4
 */
@AutoService(ShowColumnsOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class SQLShowColumnsOperator extends PrepareOperatorImpl<ShowColumnsOperator> implements ShowColumnsOperator {

    private final DbType druidDbType;
    private final QueryOperator queryOperator;
    private final SQLShowColumnsStatement showColumnsStatement;
    private Table table;

    public SQLShowColumnsOperator() {
        this(DBType.getSystemDbType());
    }

    public SQLShowColumnsOperator(DBType dbType) {
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.queryOperator = OperatorGroup.getOperator(QueryOperator.class, OperatorKey.SQL, dbType);
        this.showColumnsStatement = new SQLShowColumnsStatement();
    }

    @Override
    public String getDSL() {
        return SQLSupport.on(this)
                .onDb(druidDbType)
                .then(() -> {
                    if (druidDbType == DbType.mysql) {
                        return SQLUtils.toSQLString(showColumnsStatement);
                    }
                    return queryOperator.getDSL();
                })
                .execute();
    }

    @Override
    public ShowColumnsOperator parse(String dsl) {
        throw SQLSupport.on(this).onNonsupport("parse").<DSLException>execute();
    }

    @Override
    public String getPrepareDSL() {
        throw SQLSupport.on(this).onNonsupport("getPrepareDSL").<DSLException>execute();
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        throw SQLSupport.on(this).onNonsupport("getPrepareValues").<DSLException>execute();
    }

    @Override
    public void reset() {
        super.reset();
        queryOperator.reset();
    }

    @Override
    public ShowColumnsOperator from(Table table) {
        Object result = SQLSupport.on(this)
                .onDb(druidDbType)
                .then(() -> {
                    switch (druidDbType) {
                        case DbType.mysql -> {
                            SQLPropertyExpr sqlPropertyExpr = new SQLPropertyExpr();
                            sqlPropertyExpr.setName(table.getName().format());
                            showColumnsStatement.setTable(sqlPropertyExpr);
                        }
                        case DbType.h2, DbType.postgresql -> {
                            Table systemTable = Table.of(DSLName.of("INFORMATION_SCHEMA.COLUMNS", DSLName.PLAIN_FEATURE)).setSchema(null);
                            queryOperator.select(DSLName.of("COLUMN_NAME", DSLName.PLAIN_FEATURE), "field")
                                    .select(DSLName.of("DATA_TYPE", DSLName.PLAIN_FEATURE), "type")
                                    .select(DSLName.of("IS_NULLABLE", DSLName.PLAIN_FEATURE), "nulls")
                                    .select(DSLName.of("COLUMN_DEFAULT", DSLName.PLAIN_FEATURE), "CDEFAULT")
                                    .from(systemTable)
                                    .eq(DSLName.of("TABLE_NAME", DSLName.PLAIN_FEATURE), table.getName().format());
                        }
                        case DbType.db2 -> {
                            Table systemTable = Table.of(DSLName.of("SYSCAT.COLUMNS", DSLName.PLAIN_FEATURE)).setSchema(null);
                            queryOperator.select(DSLName.of("COLNAME", DSLName.PLAIN_FEATURE), "field")
                                    .select(DSLName.of("TYPENAME", DSLName.PLAIN_FEATURE), "type")
                                    .select(DSLName.of("NULLS", DSLName.PLAIN_FEATURE), "nulls")
                                    .select(DSLName.of("DEFAULT", DSLName.PLAIN_FEATURE), "CDEFAULT")
                                    .from(systemTable)
                                    .eq(DSLName.of("TABNAME", DSLName.PLAIN_FEATURE), table.getName().format());
                        }
                    }
                })
                .execute();
        if (result instanceof DSLException err) {
            throw err;
        }
        this.table = table;
        return self();
    }

    @Override
    public Table getTables() {
        return table;
    }

    @Override
    public QueryOperator toQueryOperator() {
        return SQLSupport.on(this)
                .onDb(druidDbType)
                .then(() -> {
                    if (druidDbType == DbType.mysql) {
                        queryOperator.reset();
                        return queryOperator.parse(SQLUtils.toSQLString(showColumnsStatement));
                    }
                    return queryOperator;
                })
                .execute();
    }
}
