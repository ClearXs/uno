package cc.allio.uno.data.orm.dsl.influxdb.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.ddl.AlterTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.google.common.collect.Lists;
import com.influxdb.client.domain.BucketRetentionRules;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * influxdb alter bucket(table) operator
 *
 * @author j.x
 * @since 1.1.8
 */
@Getter
@AutoService(AlterTableOperator.class)
@Operator.Group(OperatorKey.INFLUXDB_LITERAL)
public class InfluxdbAlterBucketOperator implements AlterTableOperator<InfluxdbAlterBucketOperator> {

    private Table fromBucket;
    private Table toBucket;
    private Long retention;
    private List<BucketRetentionRules> retentionRules;

    @Override
    public String getDSL() {
        return StringPool.EMPTY;
    }

    @Override
    public InfluxdbAlterBucketOperator parse(String dsl) {
        // nothing to do
        return self();
    }

    @Override
    public InfluxdbAlterBucketOperator customize(UnaryOperator<InfluxdbAlterBucketOperator> operatorFunc) {
        return operatorFunc.apply(new InfluxdbAlterBucketOperator());
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
    public InfluxdbAlterBucketOperator from(Table table) {
        this.fromBucket = toBucket;
        return self();
    }

    @Override
    public Table getTable() {
        return fromBucket;
    }

    @Override
    public InfluxdbAlterBucketOperator alertColumns(Collection<ColumnDef> columnDefs) {
        // nothing to do
        return self();
    }

    @Override
    public InfluxdbAlterBucketOperator addColumns(Collection<ColumnDef> columnDefs) {
        // nothing to do
        return self();
    }

    @Override
    public InfluxdbAlterBucketOperator deleteColumns(Collection<DSLName> columns) {
        // nothing to do
        return self();
    }

    @Override
    public InfluxdbAlterBucketOperator rename(Table to) {
        this.toBucket = to;
        return self();
    }

    /**
     * retain bucket time
     *
     * @param retention the long type retention
     * @return return {@link InfluxdbAlterBucketOperator}
     */
    public InfluxdbAlterBucketOperator addRetentionRule(UnaryOperator<BucketRetentionRules> func) {
        BucketRetentionRules rules = Optional.ofNullable(func).map(f -> f.apply(new BucketRetentionRules())).orElse(null);
        if (retentionRules == null) {
            retentionRules = Lists.newArrayList();
        }
        if (rules != null) {
            retentionRules.add(rules);
        }
        return self();
    }
}
