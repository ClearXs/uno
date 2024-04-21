package cc.allio.uno.data.orm.dsl.influxdb.dml;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.DeleteOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * influx delete measurement operator.
 *
 * @author j.x
 * @date 2024/4/13 20:14
 * @see <a href="https://docs.influxdata.com/influxdb/v2/reference/cli/influx/delete/">api</a>
 * @since 1.1.8
 */
@Getter
@AutoService(DeleteOperator.class)
@Operator.Group(OperatorKey.INFLUXDB_LITERAL)
public class InfluxdbDeleteOperator extends InfluxdbSQLWhereOperatorImpl<InfluxdbDeleteOperator> implements DeleteOperator<InfluxdbDeleteOperator> {

    private Table measurement;
    private Date startTime;
    private Date stopTime;
    private String bucket;

    private static final String MEASUREMENT_NAME = "_measurement";

    public InfluxdbDeleteOperator() {
        super();
    }

    @Override
    public String getPrepareDSL() {
        return getDSL();
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        return List.of();
    }

    @Override
    public String getDSL() {
        return sqlQueryOperator.getWhereDSL();
    }

    @Override
    public InfluxdbDeleteOperator parse(String dsl) {
        return self();
    }

    @Override
    public InfluxdbDeleteOperator customize(UnaryOperator<InfluxdbDeleteOperator> operatorFunc) {
        return operatorFunc.apply(new InfluxdbDeleteOperator());
    }

    @Override
    public void reset() {
        this.measurement = null;
        this.startTime = null;
        this.stopTime = null;
        this.bucket = null;
        sqlQueryOperator.reset();
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
    public InfluxdbDeleteOperator from(Table table) {
        this.measurement = table;
        // set predicate like as "_measurement="xxx""
        eq(MEASUREMENT_NAME, table.getName().format());
        return self();
    }

    @Override
    public Table getTable() {
        return measurement;
    }


    /**
     * range to delete data of start time
     *
     * @param startTime the start time of {@link Date}
     * @return self
     */
    public InfluxdbDeleteOperator start(Date startTime) {
        this.startTime = startTime;
        return self();
    }

    /**
     * range to delete data of stop time
     *
     * @param stopTime the stop time of {@link Date}
     * @return self
     */
    public InfluxdbDeleteOperator stop(Date stopTime) {
        this.stopTime = stopTime;
        return self();
    }

    /**
     * set to bucket
     *
     * @param bucket the bucket
     * @return self
     */
    public InfluxdbDeleteOperator bucket(String bucket) {
        this.bucket = bucket;
        return self();
    }
}
