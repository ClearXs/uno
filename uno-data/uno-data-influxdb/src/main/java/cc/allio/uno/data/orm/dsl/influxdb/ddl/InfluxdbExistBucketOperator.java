package cc.allio.uno.data.orm.dsl.influxdb.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.PrepareValue;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.ExistTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;

import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * influxdb exist bucket(table) operator
 *
 * @author j.x
 * @since 1.1.8
 */
@AutoService(ExistTableOperator.class)
@Operator.Group(OperatorKey.INFLUXDB_LITERAL)
public class InfluxdbExistBucketOperator implements ExistTableOperator<InfluxdbExistBucketOperator> {

    private Table fromBucket;

    @Override
    public String getPrepareDSL() {
        return StringPool.EMPTY;
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        return Collections.emptyList();
    }

    @Override
    public String getDSL() {
        return StringPool.EMPTY;
    }

    @Override
    public InfluxdbExistBucketOperator parse(String dsl) {
        // nothing to do
        return self();
    }

    @Override
    public InfluxdbExistBucketOperator customize(UnaryOperator<InfluxdbExistBucketOperator> operatorFunc) {
        return operatorFunc.apply(new InfluxdbExistBucketOperator());
    }

    @Override
    public void reset() {
        this.fromBucket = null;
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
    public InfluxdbExistBucketOperator from(Table table) {
        this.fromBucket = table;
        return self();
    }

    @Override
    public Table getTable() {
        return fromBucket;
    }
}
