package cc.allio.uno.data.orm.dsl.influxdb.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.DropTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;

import java.util.function.UnaryOperator;

/**
 * influxdb drop bucket(table) operator
 *
 * @author j.x
 * @since 1.1.8
 */
@AutoService(DropTableOperator.class)
@Operator.Group(OperatorKey.INFLUXDB_LITERAL)
public class InfluxdbDropBucketOperator implements DropTableOperator<InfluxdbDropBucketOperator> {

    private Table fromBucket;

    @Override
    public InfluxdbDropBucketOperator ifExist(Boolean ifExist) {
        // nothing to do
        return self();
    }

    @Override
    public String getDSL() {
        return StringPool.EMPTY;
    }

    @Override
    public InfluxdbDropBucketOperator parse(String dsl) {
        // nothing to do
        return self();
    }

    @Override
    public InfluxdbDropBucketOperator customize(UnaryOperator<InfluxdbDropBucketOperator> operatorFunc) {
        return operatorFunc.apply(new InfluxdbDropBucketOperator());
    }

    @Override
    public void reset() {
        // nothing to do
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
    public InfluxdbDropBucketOperator from(Table table) {
        this.fromBucket = table;
        return self();
    }

    @Override
    public Table getTable() {
        return fromBucket;
    }
}
