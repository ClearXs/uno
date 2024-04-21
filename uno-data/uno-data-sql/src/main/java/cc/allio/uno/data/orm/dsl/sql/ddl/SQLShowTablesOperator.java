package cc.allio.uno.data.orm.dsl.sql.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.ddl.ShowTablesOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.dsl.opeartorgroup.OperatorGroup;
import cc.allio.uno.data.orm.dsl.sql.SQLSupport;
import cc.allio.uno.data.orm.dsl.sql.dml.SQLQueryOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.alibaba.druid.DbType;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.UnaryOperator;

@AutoService(ShowTablesOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class SQLShowTablesOperator extends PrepareOperatorImpl<SQLShowTablesOperator> implements ShowTablesOperator<SQLShowTablesOperator> {

    private DBType dbType;
    private DbType druidDbType;
    private String schema;
    private Database database;
    private List<Table> tables;
    private SQLQueryOperator queryOperator;

    public SQLShowTablesOperator() {
        this(DBType.getSystemDbType());
    }

    public SQLShowTablesOperator(DBType dbType) {
        this.dbType = dbType;
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.queryOperator = OperatorGroup.getOperator(SQLQueryOperator.class, OperatorKey.SQL, dbType);
        this.schema = "PUBLIC";
        this.tables = Lists.newArrayList();
    }

    @Override
    public String getDSL() {
        trigger();
        return queryOperator.getDSL();
    }

    @Override
    public SQLShowTablesOperator parse(String dsl) {
        this.queryOperator = queryOperator.parse(dsl);
        return self();
    }

    @Override
    public SQLShowTablesOperator customize(UnaryOperator<SQLShowTablesOperator> operatorFunc) {
        return operatorFunc.apply(new SQLShowTablesOperator(dbType));
    }

    @Override
    public void setDBType(DBType dbType) {
        this.dbType = dbType;
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.queryOperator.setDBType(this.dbType);
    }

    @Override
    public DBType getDBType() {
        return dbType;
    }

    @Override
    public String getPrepareDSL() {
        throw SQLSupport.nonsupportOperate(this, "getPrepareDSL");
    }

    @Override
    public QueryOperator<?> toQueryOperator() {
        trigger();
        return queryOperator;
    }

    @Override
    public SQLShowTablesOperator database(Database database) {
        this.database = database;
        return self();
    }

    @Override
    public SQLShowTablesOperator schema(String schema) {
        this.schema = schema;
        return self();
    }

    private void trigger() {
        Table formTable = Table.of(DSLName.of("INFORMATION_SCHEMA.TABLES", DSLName.PLAIN_FEATURE)).setSchema(null);
        Object result = SQLSupport.on(this)
                .onDb(druidDbType)
                .then(() -> {
                    switch (druidDbType) {
                        case DbType.mysql ->
                                queryOperator.select(DSLName.of(ShowTablesOperator.TABLE_CATALOG_FILED, DSLName.PLAIN_FEATURE))
                                        .select(DSLName.of(ShowTablesOperator.TABLE_SCHEMA_FILED, DSLName.PLAIN_FEATURE))
                                        .select(DSLName.of(ShowTablesOperator.TABLE_NAME_FILED, DSLName.PLAIN_FEATURE))
                                        .select(DSLName.of(ShowTablesOperator.TABLE_TYPE_FILED, DSLName.PLAIN_FEATURE))
                                        .from(formTable)
                                        .eq(DSLName.of(ShowTablesOperator.TABLE_SCHEMA_FILED, DSLName.PLAIN_FEATURE), database.getName().format())
                                        .and()
                                        .eq(DSLName.of(ShowTablesOperator.TABLE_TYPE_FILED, DSLName.PLAIN_FEATURE), "BASE TABLE");
                        case DbType.h2, DbType.postgresql -> {
                            queryOperator.select(DSLName.of(ShowTablesOperator.TABLE_CATALOG_FILED, DSLName.PLAIN_FEATURE))
                                    .select(DSLName.of(ShowTablesOperator.TABLE_SCHEMA_FILED, DSLName.PLAIN_FEATURE))
                                    .select(DSLName.of(ShowTablesOperator.TABLE_NAME_FILED, DSLName.PLAIN_FEATURE))
                                    .select(DSLName.of(ShowTablesOperator.TABLE_TYPE_FILED, DSLName.PLAIN_FEATURE))
                                    .from(formTable)
                                    .eq(DSLName.of(ShowTablesOperator.TABLE_TYPE_FILED, DSLName.PLAIN_FEATURE), "BASE TABLE");
                            if (DbType.h2 == druidDbType) {
                                queryOperator.eq(DSLName.of(ShowTablesOperator.TABLE_SCHEMA_FILED, DSLName.PLAIN_FEATURE), schema);
                            } else if (DbType.postgresql == druidDbType) {
                                queryOperator.eq(DSLName.of(ShowTablesOperator.TABLE_SCHEMA_FILED, DSLName.PLAIN_FEATURE), schema.toLowerCase());
                            }
                        }
                    }
                    if (CollectionUtils.isNotEmpty(tables)) {
                        if (tables.size() == 1) {
                            queryOperator.eq(DSLName.of(ShowTablesOperator.TABLE_NAME_FILED, DSLName.PLAIN_FEATURE), tables.get(0).getName().format());
                        } else {
                            List<String> tableNames = tables.stream().map(Table::getName).map(DSLName::format).toList();
                            queryOperator.in(DSLName.of(ShowTablesOperator.TABLE_NAME_FILED, DSLName.PLAIN_FEATURE), tableNames);
                        }
                    }
                })
                .execute();
        if (result instanceof DSLException err) {
            throw err;
        }
    }

    @Override
    public SQLShowTablesOperator from(Table table) {
        this.tables.add(table);
        return self();
    }

    @Override
    public Table getTable() {
        throw SQLSupport.nonsupportOperate(this, "getTable");
    }

}
