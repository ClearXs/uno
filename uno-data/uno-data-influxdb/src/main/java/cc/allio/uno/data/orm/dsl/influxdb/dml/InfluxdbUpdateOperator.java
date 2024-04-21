package cc.allio.uno.data.orm.dsl.influxdb.dml;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * influx db update operator (bridge to {@link InfluxdbInsertOperator})
 *
 * @author j.x
 * @date 2024/4/14 15:48
 * @since 1.1.8
 */
@AutoService(UpdateOperator.class)
@Operator.Group(OperatorKey.INFLUXDB_LITERAL)
public class InfluxdbUpdateOperator extends InfluxdbSQLWhereOperatorImpl<InfluxdbUpdateOperator> implements UpdateOperator<InfluxdbUpdateOperator> {

    private Table measurement;
    private InfluxdbInsertOperator insertOperator;

    public InfluxdbUpdateOperator() {
        insertOperator = new InfluxdbInsertOperator();
    }

    @Override
    public InfluxdbUpdateOperator updates(Map<DSLName, Object> values) {
        insertOperator.inserts(values);
        return self();
    }

    @Override
    public InfluxdbUpdateOperator strictFill(String f, Supplier<Object> v) {
        insertOperator.strictFill(f, v);
        return self();
    }

    /**
     * set to measurement tags
     *
     * @see #tags(Map)
     */
    public InfluxdbUpdateOperator tags(String f1, Object v1) {
        return tags(Map.of(DSLName.of(f1), getValueIfNull(v1)));
    }

    /**
     * set to measurement tags
     *
     * @see #tags(Map)
     */
    public InfluxdbUpdateOperator tags(String f1, Object v1, String f2, Object v2) {
        return tags(Map.of(
                DSLName.of(f1), getValueIfNull(v1),
                DSLName.of(f2), getValueIfNull(v2)));
    }

    /**
     * set to measurement tags
     *
     * @see #tags(Map)
     */
    public InfluxdbUpdateOperator tags(String f1, Object v1, String f2, Object v2, String f3, Object v3) {
        return tags(Map.of(
                DSLName.of(f1), getValueIfNull(v1),
                DSLName.of(f2), getValueIfNull(v2),
                DSLName.of(f3), getValueIfNull(v3)));
    }

    /**
     * set to measurement tags
     *
     * @param values the tag map value
     * @return self
     */
    public InfluxdbUpdateOperator tags(Map<DSLName, Object> values) {
        insertOperator.tags(values);
        return self();
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
        return StringPool.EMPTY;
    }

    @Override
    public InfluxdbUpdateOperator parse(String dsl) {
        // nothing to do
        return self();
    }

    @Override
    public InfluxdbUpdateOperator customize(UnaryOperator<InfluxdbUpdateOperator> operatorFunc) {
        return operatorFunc.apply(new InfluxdbUpdateOperator());
    }

    @Override
    public void reset() {
        this.measurement = null;
        this.sqlQueryOperator.reset();
        this.insertOperator.reset();
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
    public InfluxdbUpdateOperator from(Table table) {
        this.measurement = table;
        return self();
    }

    @Override
    public Table getTable() {
        return measurement;
    }
}
