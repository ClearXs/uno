package cc.allio.uno.data.orm.dsl.influxdb.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.ddl.AlterTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import lombok.Getter;

import java.util.Collection;

/**
 * influxdb alter bucket(table) operator
 *
 * @author j.x
 * @date 2024/4/1 16:27
 * @since 1.1.8
 */
@Getter
@AutoService(AlterTableOperator.class)
@Operator.Group(OperatorKey.INFLUXDB_LITERAL)
public class InfluxdbAlterBucketOperator implements AlterTableOperator {

    private Table fromBucket;
    private Table toBucket;
    private Long retention;

    @Override
    public String getDSL() {
        throw Exceptions.unOperate("getDSL");
    }

    @Override
    public AlterTableOperator parse(String dsl) {
        throw Exceptions.unOperate("parse");
    }

    @Override
    public void reset() {
        this.fromBucket = null;
        this.toBucket = null;
        this.retention = null;
    }

    @Override
    public void setDBType(DBType dbType) {
        // nothing to do
    }

    @Override
    public DBType getDBType() {
        return DBType.INFLUXDB;
    }

    @Override
    public AlterTableOperator from(Table table) {
        this.fromBucket = toBucket;
        return self();
    }

    @Override
    public Table getTable() {
        return fromBucket;
    }

    @Override
    public AlterTableOperator alertColumns(Collection<ColumnDef> columnDefs) {
        throw Exceptions.unOperate("alertColumns");
    }

    @Override
    public AlterTableOperator addColumns(Collection<ColumnDef> columnDefs) {
        throw Exceptions.unOperate("addColumns");
    }

    @Override
    public AlterTableOperator deleteColumns(Collection<DSLName> columns) {
        throw Exceptions.unOperate("deleteColumns");
    }

    @Override
    public AlterTableOperator rename(Table to) {
        this.toBucket = to;
        return self();
    }

    /**
     * retain bucket time
     *
     * @param retention the long type retention
     * @return return {@link InfluxdbAlterBucketOperator}
     */
    public InfluxdbAlterBucketOperator retention(Long retention) {
        this.retention = retention;
        return this;
    }
}
