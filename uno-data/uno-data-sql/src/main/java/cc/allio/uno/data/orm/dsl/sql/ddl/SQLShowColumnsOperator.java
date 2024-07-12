package cc.allio.uno.data.orm.dsl.sql.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.dsl.opeartorgroup.OperatorGroup;
import cc.allio.uno.data.orm.dsl.sql.SQLSupport;
import cc.allio.uno.data.orm.dsl.sql.dml.SQLQueryOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.alibaba.druid.DbType;
import cc.allio.uno.data.orm.dsl.ddl.ShowColumnsOperator;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * 表结构
 *
 * @author j.x
 * @date 2023/6/8 19:20
 * @since 1.1.4
 */
@AutoService(ShowColumnsOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class SQLShowColumnsOperator extends PrepareOperatorImpl<SQLShowColumnsOperator> implements ShowColumnsOperator<SQLShowColumnsOperator> {

    private DBType dbType;
    private DbType druidDbType;
    private Table table;
    private Database database;
    private final SQLQueryOperator queryOperator;

    public SQLShowColumnsOperator() {
        this(DBType.getSystemDbType());
    }

    public SQLShowColumnsOperator(DBType dbType) {
        this.dbType = dbType;
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.queryOperator = OperatorGroup.getOperator(SQLQueryOperator.class, OperatorKey.SQL, dbType);
    }

    @Override
    public String getDSL() {
        return SQLSupport.on(this)
                .onDb(druidDbType)
                .then(queryOperator::getDSL)
                .execute();
    }

    @Override
    public SQLShowColumnsOperator parse(String dsl) {
        throw SQLSupport.on(this).onNonsupport("parse").<DSLException>execute();
    }

    @Override
    public SQLShowColumnsOperator customize(UnaryOperator<SQLShowColumnsOperator> operatorFunc) {
        return operatorFunc.apply(new SQLShowColumnsOperator(dbType));
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
    public void setDBType(DBType dbType) {
        this.dbType = dbType;
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.queryOperator.setDBType(dbType);
    }

    @Override
    public DBType getDBType() {
        return dbType;
    }

    @Override
    public SQLShowColumnsOperator from(Table table) {
        Object result = SQLSupport.on(this)
                .onDb(druidDbType)
                .then(queryOperator::reset)
                .then(() -> {
                    queryOperator.selectAll()
                            .select(DSLName.of(TABLE_CATALOG_FIELD, DSLName.PLAIN_FEATURE))
                            .select(DSLName.of(TABLE_SCHEMA_FILED, DSLName.PLAIN_FEATURE))
                            .select(DSLName.of(TABLE_NAME_FILED, DSLName.PLAIN_FEATURE))
                            .select(DSLName.of(COLUMN_NAME_FIELD, DSLName.PLAIN_FEATURE))
                            .select(DSLName.of(ORDINAL_POSITION_FIELD, DSLName.PLAIN_FEATURE))
                            .select(DSLName.of(COLUMN_DEFAULT_FIELD, DSLName.PLAIN_FEATURE))
                            .select(DSLName.of(IS_NULLABLE_FIELD, DSLName.PLAIN_FEATURE))
                            .select(DSLName.of(DATA_TYPE_FIELD, DSLName.PLAIN_FEATURE))
                            .select(DSLName.of(CHARACTER_MAXIMUM_LENGTH_FIELD, DSLName.PLAIN_FEATURE))
                            .select(DSLName.of(CHARACTER_OCTET_LENGTH_FIELD, DSLName.PLAIN_FEATURE))
                            .select(DSLName.of(NUMERIC_PRECISION_FIELD, DSLName.PLAIN_FEATURE))
                            .select(DSLName.of(NUMERIC_SCALE_FIELD, DSLName.PLAIN_FEATURE))
                            .select(DSLName.of(DATETIME_PRECISION_FIELD, DSLName.PLAIN_FEATURE));
                    switch (druidDbType) {
                        case mysql: {
                            Table systemTable = Table.of(DSLName.of("INFORMATION_SCHEMA.COLUMNS", DSLName.PLAIN_FEATURE)).setSchema(null);
                            queryOperator.from(systemTable)
                                    .eq(DSLName.of(TABLE_NAME_FILED, DSLName.PLAIN_FEATURE), table.getName().format());
                            if (database != null) {
                                queryOperator.eq(DSLName.of(TABLE_SCHEMA_FILED, DSLName.PLAIN_FEATURE), database.getName());
                            }
                            break;
                        }
                        case h2:
                        case postgresql: {
                            Table systemTable = Table.of(DSLName.of("INFORMATION_SCHEMA.COLUMNS", DSLName.PLAIN_FEATURE)).setSchema(null);
                            queryOperator
                                    .from(systemTable)
                                    .eq(DSLName.of("TABLE_NAME", DSLName.PLAIN_FEATURE), table.getName().format());
                            break;
                        }
                        case db2: {
                            Table systemTable = Table.of(DSLName.of("SYSCAT.COLUMNS", DSLName.PLAIN_FEATURE)).setSchema(null);
                            queryOperator
                                    .from(systemTable)
                                    .eq(DSLName.of("TABNAME", DSLName.PLAIN_FEATURE), table.getName().format());
                        }
                    }
                })
                .execute();
        if (result instanceof DSLException) {
            throw ((DSLException) result);
        }
        this.table = table;
        return self();
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public QueryOperator<?> toQueryOperator() {
        return SQLSupport.on(this)
                .onDb(druidDbType)
                .then(() -> queryOperator)
                .execute();
    }

    @Override
    public SQLShowColumnsOperator database(Database database) {
        this.database = database;
        return self();
    }
}
