package cc.allio.uno.data.orm.dsl.ddl.sql;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.ddl.ShowTablesOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DruidDbTypeAdapter;
import com.alibaba.druid.DbType;

@AutoService(ShowTablesOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class SQLShowTablesOperator extends PrepareOperatorImpl<ShowTablesOperator> implements ShowTablesOperator {

    private final DbType druidDbType;
    private QueryOperator queryOperator;
    private String schema;
    private Database database;

    public SQLShowTablesOperator() {
        this(DBType.getSystemDbType());
    }

    public SQLShowTablesOperator(DBType dbType) {
        this.druidDbType = DruidDbTypeAdapter.getInstance().adapt(dbType);
        this.queryOperator = OperatorGroup.getOperator(QueryOperator.class, OperatorKey.SQL, dbType);
        this.schema = "PUBLIC";
    }

    @Override
    public String getDSL() {
        trigger();
        return queryOperator.getDSL();
    }

    @Override
    public ShowTablesOperator parse(String dsl) {
        this.queryOperator = queryOperator.parse(dsl);
        return self();
    }

    @Override
    public String getPrepareDSL() {
        throw SQLSupport.nonsupportOperate(this, "getPrepareDSL");
    }

    @Override
    public QueryOperator toQueryOperator() {
        trigger();
        return queryOperator;
    }

    @Override
    public ShowTablesOperator database(Database database) {
        this.database = database;
        return self();
    }

    @Override
    public ShowTablesOperator schema(String schema) {
        this.schema = schema;
        return self();
    }

    private void trigger() {
        Table table = Table.of(DSLName.of("INFORMATION_SCHEMA.TABLES", DSLName.PLAIN_FEATURE)).setSchema(null);
        Object result = SQLSupport.on(this)
                .onDb(druidDbType)
                .then(() ->
                        switch (druidDbType) {
                            case DbType.mysql ->
                                    queryOperator.select(DSLName.of("TABLE_CATALOG", DSLName.PLAIN_FEATURE), ShowTablesOperator.CATALOG_FILED)
                                            .select(DSLName.of("TABLE_SCHEMA", DSLName.PLAIN_FEATURE), ShowTablesOperator.SCHEMA_FILED)
                                            .select(DSLName.of("TABLE_NAME", DSLName.PLAIN_FEATURE), ShowTablesOperator.NAME_FILED)
                                            .select(DSLName.of("TABLE_TYPE", DSLName.PLAIN_FEATURE), ShowTablesOperator.TYPE_FILED)
                                            .from(table)
                                            .eq(DSLName.of("TABLE_SCHEMA", DSLName.PLAIN_FEATURE), database.getName().format())
                                            .and()
                                            .eq(DSLName.of("TABLE_TYPE", DSLName.PLAIN_FEATURE), "BASE TABLE");
                            case DbType.h2, DbType.postgresql ->
                                    queryOperator.select(DSLName.of("TABLE_CATALOG", DSLName.PLAIN_FEATURE), ShowTablesOperator.CATALOG_FILED)
                                            .select(DSLName.of("TABLE_SCHEMA", DSLName.PLAIN_FEATURE), ShowTablesOperator.SCHEMA_FILED)
                                            .select(DSLName.of("TABLE_NAME", DSLName.PLAIN_FEATURE), ShowTablesOperator.NAME_FILED)
                                            .select(DSLName.of("TABLE_TYPE", DSLName.PLAIN_FEATURE), ShowTablesOperator.TYPE_FILED)
                                            .from(table)
                                            .eq(DSLName.of("TABLE_SCHEMA", DSLName.PLAIN_FEATURE), schema).
                                            and()
                                            .eq(DSLName.of("TABLE_TYPE", DSLName.PLAIN_FEATURE), "BASE TABLE");
                            default -> queryOperator;
                        })
                .execute();
        if (result instanceof DSLException err) {
            throw err;
        }
    }
}
