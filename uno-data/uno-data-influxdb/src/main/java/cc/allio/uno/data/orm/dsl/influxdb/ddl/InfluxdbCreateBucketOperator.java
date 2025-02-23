package cc.allio.uno.data.orm.dsl.influxdb.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.CreateTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.google.common.collect.Lists;
import com.influxdb.client.domain.BucketRetentionRules;
import com.influxdb.client.domain.Label;
import com.influxdb.client.domain.SchemaType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * influxdb of bucket operator
 *
 * @author j.x
 * @since 1.1.8
 */
@Getter
@AutoService(CreateTableOperator.class)
@Operator.Group(OperatorKey.INFLUXDB_LITERAL)
public class InfluxdbCreateBucketOperator implements CreateTableOperator<InfluxdbCreateBucketOperator> {

    private Table fromBucket;
    @Getter
    private List<BucketRetentionRules> retentionRules;
    @Getter
    private SchemaType schemaType;
    @Getter
    private List<Label> labels;
    @Getter
    private String description;

    private List<ColumnDef> columnDefs = new ArrayList<>();

    @Override
    public InfluxdbCreateBucketOperator column(ColumnDef columnDef) {
        columnDefs.add(columnDef);
        // nothing to do
        return self();
    }

    @Override
    public InfluxdbCreateBucketOperator comment(String comment) {
        this.description = comment;
        return self();
    }

    @Override
    public List<ColumnDef> getColumns() {
        return columnDefs;
    }

    @Override
    public String getDSL() {
        return StringPool.EMPTY;
    }

    @Override
    public InfluxdbCreateBucketOperator parse(String dsl) {
        // nothing to do
        return self();
    }

    @Override
    public InfluxdbCreateBucketOperator customize(UnaryOperator<InfluxdbCreateBucketOperator> operatorFunc) {
        return operatorFunc.apply(new InfluxdbCreateBucketOperator());
    }

    @Override
    public void reset() {
        this.fromBucket = null;
        this.retentionRules = null;
        this.schemaType = null;
        this.labels = null;
        this.description = null;
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
    public InfluxdbCreateBucketOperator from(Table table) {
        this.fromBucket = table;
        return self();
    }

    @Override
    public Table getTable() {
        return fromBucket;
    }

    // customize influxdb bucket attributes
    // @see https://docs.influxdata.com/influxdb/v2/admin/buckets/create-bucket/

    /**
     * add bucket retention rule strategy
     *
     * @param func the retention rule func
     * @return self
     * @see BucketRetentionRules
     */
    public InfluxdbCreateBucketOperator addRetentionRule(UnaryOperator<BucketRetentionRules> func) {
        BucketRetentionRules rules = Optional.ofNullable(func).map(f -> f.apply(new BucketRetentionRules())).orElse(null);
        if (retentionRules == null) {
            this.retentionRules = Lists.newArrayList();
        }
        if (rules != null) {
            retentionRules.add(rules);
        }
        return self();
    }

    /**
     * set {@link SchemaType}
     *
     * @param schemaType the schema type
     * @return self
     */
    public InfluxdbCreateBucketOperator schemaType(SchemaType schemaType) {
        this.schemaType = schemaType;
        return self();
    }

    /**
     * add {@link Label}
     *
     * @param func the label func
     * @return self
     */
    public InfluxdbCreateBucketOperator addLabel(UnaryOperator<Label> func) {
        Label label = Optional.ofNullable(func).map(f -> f.apply(new Label())).orElse(null);
        if (labels == null) {
            this.labels = Lists.newArrayList();
        }
        if (label != null) {
            labels.add(label);
        }
        return self();
    }
}
