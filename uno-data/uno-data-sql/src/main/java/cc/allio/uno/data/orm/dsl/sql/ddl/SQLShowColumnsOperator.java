package cc.allio.uno.data.orm.dsl.sql.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.dsl.opeartorgroup.Operators;
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

    private static final String PG_SHOW_COLUMN_SQL = "SELECT\n" +
            "\tTABLE_CATALOG,\n" +
            "\tTABLE_SCHEMA,\n" +
            "\tTABLE_NAME,\n" +
            "\tCOLUMN_NAME,\n" +
            "\td.DESCRIPTION AS COLUMN_COMMENT,\n" +
            "\tORDINAL_POSITION,\n" +
            "\tCOLUMN_DEFAULT,\n" +
            "\tIS_NULLABLE,\n" +
            "\tUDT_NAME AS DATA_TYPE,\n" +
            "\tCHARACTER_MAXIMUM_LENGTH,\n" +
            "\tCHARACTER_OCTET_LENGTH,\n" +
            "\tNUMERIC_PRECISION,\n" +
            "\tNUMERIC_SCALE,\n" +
            "\tDATETIME_PRECISION \n" +
            "FROM\n" +
            "\tINFORMATION_SCHEMA.COLUMNS col\n" +
            "\tLEFT JOIN PG_CLASS C ON C.relname = col.\n" +
            "\tTABLE_NAME LEFT JOIN PG_DESCRIPTION d ON d.objoid = C.oid \n" +
            "\tAND d.objsubid = col.ordinal_position ";

    public SQLShowColumnsOperator() {
        this(DBType.getSystemDbType());
    }

    public SQLShowColumnsOperator(DBType dbType) {
        this.dbType = dbType;
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.queryOperator = Operators.getOperator(SQLQueryOperator.class, OperatorKey.SQL, dbType);
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
                        case DbType.mysql -> {
                            Table systemTable = Table.of(DSLName.of("INFORMATION_SCHEMA.COLUMNS", DSLName.PLAIN_FEATURE)).setSchema(null);
                            queryOperator.from(systemTable)
                                    .eq(DSLName.of(TABLE_NAME_FILED, DSLName.PLAIN_FEATURE), table.getName().format());
                            if (database != null) {
                                queryOperator.eq(DSLName.of(TABLE_SCHEMA_FILED, DSLName.PLAIN_FEATURE), database.getName());
                            }
                        }
                        case DbType.h2 -> {
                            Table systemTable = Table.of(DSLName.of("INFORMATION_SCHEMA.COLUMNS", DSLName.PLAIN_FEATURE)).setSchema(null);
                            queryOperator
                                    .from(systemTable)
                                    .eq(DSLName.of("TABLE_NAME", DSLName.PLAIN_FEATURE), table.getName().format());
                        }
                        case postgresql -> {
                            queryOperator.customize(PG_SHOW_COLUMN_SQL)
                                    .eq(DSLName.of("TABLE_NAME", DSLName.PLAIN_FEATURE), table.getName().format());
                        }
                        case DbType.db2 -> {
                            Table systemTable = Table.of(DSLName.of("SYSCAT.COLUMNS", DSLName.PLAIN_FEATURE)).setSchema(null);
                            queryOperator
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
