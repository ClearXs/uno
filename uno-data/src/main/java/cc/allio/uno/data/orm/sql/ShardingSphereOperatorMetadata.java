package cc.allio.uno.data.orm.sql;

import cc.allio.uno.data.orm.sql.ddl.SQLCreateTableOperator;
import cc.allio.uno.data.orm.sql.ddl.SQLDropTableOperator;
import cc.allio.uno.data.orm.sql.ddl.SQLExistTableOperator;
import cc.allio.uno.data.orm.sql.ddl.SQLShowColumnsOperator;
import cc.allio.uno.data.orm.sql.dml.SQLDeleteOperator;
import cc.allio.uno.data.orm.sql.dml.SQLInsertOperator;
import cc.allio.uno.data.orm.sql.dml.SQLQueryOperator;
import cc.allio.uno.data.orm.sql.dml.SQLUpdateOperator;
import cc.allio.uno.data.orm.type.DBType;

/**
 * sharding-sphere
 *
 * @author jiangwei
 * @date 2023/4/16 13:08
 * @since 1.1.4
 */
public class ShardingSphereOperatorMetadata implements OperatorMetadata {
    @Override
    public SQLQueryOperator query(DBType dbType) {
        return null;
    }

    @Override
    public SQLInsertOperator insert(DBType dbType) {
        return null;
    }

    @Override
    public SQLUpdateOperator update(DBType dbType) {
        return null;
    }

    @Override
    public SQLDeleteOperator delete(DBType dbType) {
        return null;
    }

    @Override
    public SQLCreateTableOperator createTable(DBType dbType) {
        return null;
    }

    @Override
    public SQLDropTableOperator dropTable(DBType dbType) {
        return null;
    }

    @Override
    public SQLExistTableOperator existTable(DBType dbType) {
        return null;
    }

    @Override
    public SQLShowColumnsOperator showColumns(DBType dbType) {
        return null;
    }

    @Override
    public OperatorMetadataKey getKey() {
        return DRUID_OPERATOR_KEY;
    }
}
