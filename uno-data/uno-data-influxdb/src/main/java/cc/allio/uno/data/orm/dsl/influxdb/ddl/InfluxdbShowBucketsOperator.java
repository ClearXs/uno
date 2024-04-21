package cc.allio.uno.data.orm.dsl.influxdb.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.ddl.ShowTablesOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * influxdb show buckets operator
 *
 * @author j.x
 * @date 2024/4/13 19:41
 * @since 1.1.8
 */
@Getter
@AutoService(ShowTablesOperator.class)
@Operator.Group(OperatorKey.INFLUXDB_LITERAL)
public class InfluxdbShowBucketsOperator implements ShowTablesOperator<InfluxdbShowBucketsOperator> {

    private List<Table> fromBuckets;

    @Override
    public QueryOperator<?> toQueryOperator() {
        throw Exceptions.unOperate("toQueryOperator");
    }

    @Override
    public InfluxdbShowBucketsOperator schema(String schema) {
        // nothing to do
        return self();
    }

    @Override
    public InfluxdbShowBucketsOperator from(Table table) {
        if (fromBuckets == null) {
            this.fromBuckets = Lists.newArrayList();
        }
        fromBuckets.add(table);
        return self();
    }

    @Override
    public Table getTable() {
        throw Exceptions.unOperate("getTable");
    }

    @Override
    public InfluxdbShowBucketsOperator database(Database database) {
        // nothing to do
        return self();
    }

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
    public InfluxdbShowBucketsOperator parse(String dsl) {
        // nothing to do
        return self();
    }

    @Override
    public InfluxdbShowBucketsOperator customize(UnaryOperator<InfluxdbShowBucketsOperator> operatorFunc) {
        return operatorFunc.apply(new InfluxdbShowBucketsOperator());
    }

    @Override
    public void reset() {
        this.fromBuckets = null;
    }

    @Override
    public void setDBType(DBType dbType) {
        // nothing to do
    }

    @Override
    public DBType getDBType() {
        return DBType.INFLUXDB;
    }
}
