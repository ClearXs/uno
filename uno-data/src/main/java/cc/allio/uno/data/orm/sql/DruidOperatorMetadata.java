package cc.allio.uno.data.orm.sql;

import cc.allio.uno.data.orm.sql.ddl.SQLCreateTableOperator;
import cc.allio.uno.data.orm.sql.ddl.SQLDropTableOperator;
import cc.allio.uno.data.orm.sql.ddl.SQLExistTableOperator;
import cc.allio.uno.data.orm.sql.ddl.SQLShowColumnsOperator;
import cc.allio.uno.data.orm.sql.ddl.druid.DruidSQLCreateOperator;
import cc.allio.uno.data.orm.sql.ddl.druid.DruidSQLDropTableOperator;
import cc.allio.uno.data.orm.sql.ddl.druid.DruidSQLExistTableOperator;
import cc.allio.uno.data.orm.sql.ddl.druid.DruidSQLShowColumnsOperator;
import cc.allio.uno.data.orm.sql.dml.SQLDeleteOperator;
import cc.allio.uno.data.orm.sql.dml.SQLInsertOperator;
import cc.allio.uno.data.orm.sql.dml.SQLQueryOperator;
import cc.allio.uno.data.orm.sql.dml.SQLUpdateOperator;
import cc.allio.uno.data.orm.sql.dml.druid.DruidSQLDeleteQueryOperator;
import cc.allio.uno.data.orm.sql.dml.druid.DruidSQLInsertOperator;
import cc.allio.uno.data.orm.sql.dml.druid.DruidSQLQueryOperator;
import cc.allio.uno.data.orm.sql.dml.druid.DruidSQLUpdateOperator;
import cc.allio.uno.data.orm.type.DBType;

/**
 * Druid Operator Metadata
 *
 * @author jiangwei
 * @date 2023/4/13 18:55
 * @since 1.1.4
 */
public class DruidOperatorMetadata implements OperatorMetadata {

    @Override
    public SQLQueryOperator query(DBType dbType) {
        return new DruidSQLQueryOperator(dbType);
    }

    @Override
    public SQLInsertOperator insert(DBType dbType) {
        return new DruidSQLInsertOperator(dbType);
    }

    @Override
    public SQLUpdateOperator update(DBType dbType) {
        return new DruidSQLUpdateOperator(dbType);
    }

    @Override
    public SQLDeleteOperator delete(DBType dbType) {
        return new DruidSQLDeleteQueryOperator(dbType);
    }

    @Override
    public SQLCreateTableOperator createTable(DBType dbType) {
        return new DruidSQLCreateOperator(dbType);
    }

    @Override
    public SQLDropTableOperator dropTable(DBType dbType) {
        return new DruidSQLDropTableOperator(dbType);
    }

    @Override
    public SQLExistTableOperator existTable(DBType dbType) {
        return new DruidSQLExistTableOperator(dbType);
    }

    @Override
    public SQLShowColumnsOperator showColumns(DBType dbType) {
        return new DruidSQLShowColumnsOperator(dbType);
    }

    @Override
    public OperatorMetadataKey getKey() {
        return DRUID_OPERATOR_KEY;
    }
}
