package cc.allio.uno.data.orm.dsl.influxdb.dml;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * influx db insert operator
 * <p>when invoke relevant {@link #columns(String...)} api, means of influxdb fields, if of tags, please use </p>
 *
 * @author j.x
 * @since 1.1.8
 */
@AutoService(InsertOperator.class)
@Operator.Group(OperatorKey.INFLUXDB_LITERAL)
public class InfluxdbInsertOperator implements InsertOperator<InfluxdbInsertOperator> {

    private Table measurement;

    private List<DSLName> columns;
    @Getter
    private List<Map<DSLName, Object>> fields;
    @Getter
    private List<Map<DSLName, Object>> tags;

    public InfluxdbInsertOperator() {
        this.columns = Lists.newArrayList();
        this.fields = Lists.newArrayList();
        this.tags = Lists.newArrayList();
    }

    @Override
    public InfluxdbInsertOperator strictFill(String f, Supplier<Object> v) {
        DSLName dslName = DSLName.of(f);
        // set to field
        for (Map<DSLName, Object> field : fields) {
            if (field.containsKey(dslName)) {
                field.put(dslName, v.get());
            }
        }
        // set to tags
        for (Map<DSLName, Object> tag : tags) {
            if (tag.containsKey(dslName)) {
                tag.put(dslName, v.get());
            }
        }
        return self();
    }

    @Override
    public InfluxdbInsertOperator columns(Collection<DSLName> columns) {
        this.columns.addAll(columns);
        return self();
    }

    @Override
    public InfluxdbInsertOperator values(List<Object> values) {
        if (CollectionUtils.isEmpty(values)) {
            return self();
        }
        Map<DSLName, Object> field = Maps.newHashMap();
        for (int i = 0; i < columns.size(); i++) {
            DSLName col = columns.get(i);
            Object v = values.get(i);
            field.put(col, v);
        }
        this.fields.add(field);
        return self();
    }

    /**
     * set to measurement tags
     *
     * @see #tags(Map)
     */
    public InfluxdbInsertOperator tags(String f1, Object v1) {
        return tags(Map.of(DSLName.of(f1), getValueIfNull(v1)));
    }

    /**
     * set to measurement tags
     *
     * @see #tags(Map)
     */
    public InfluxdbInsertOperator tags(String f1, Object v1, String f2, Object v2) {
        return tags(Map.of(
                DSLName.of(f1), getValueIfNull(v1),
                DSLName.of(f2), getValueIfNull(v2)));
    }

    /**
     * set to measurement tags
     *
     * @see #tags(Map)
     */
    public InfluxdbInsertOperator tags(String f1, Object v1, String f2, Object v2, String f3, Object v3) {
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
    public InfluxdbInsertOperator tags(Map<DSLName, Object> values) {
        if (CollectionUtils.isEmpty(values)) {
            return self();
        }
        this.tags.add(values);
        return self();
    }

    @Override
    public boolean isBatched() {
        return !fields.isEmpty();
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
    public InfluxdbInsertOperator parse(String dsl) {
        // nothing to do
        return self();
    }

    @Override
    public InfluxdbInsertOperator customize(UnaryOperator<InfluxdbInsertOperator> operatorFunc) {
        return operatorFunc.apply(new InfluxdbInsertOperator());
    }

    @Override
    public void reset() {
        this.measurement = null;
        this.columns = Lists.newArrayList();
        this.fields = Lists.newArrayList();
        this.tags = Lists.newArrayList();
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
    public InfluxdbInsertOperator from(Table table) {
        this.measurement = table;
        return self();
    }

    @Override
    public Table getTable() {
        return measurement;
    }
}
