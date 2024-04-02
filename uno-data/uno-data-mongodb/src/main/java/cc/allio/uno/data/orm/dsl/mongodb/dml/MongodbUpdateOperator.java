package cc.allio.uno.data.orm.dsl.mongodb.dml;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.data.orm.dsl.mongodb.MongodbSupport;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.client.model.Updates;
import lombok.Getter;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * mongodb update document operator implementation
 *
 * @author j.x
 * @date 2024/3/14 01:34
 * @since 1.1.7
 */
@AutoService(UpdateOperator.class)
@Operator.Group(OperatorKey.MONGODB_LITERAL)
public class MongodbUpdateOperator extends MongodbWhereOperatorImpl<UpdateOperator> implements UpdateOperator {

    private Table fromColl;
    @Getter
    private Bson update;

    private OnceTrigger trigger;

    public MongodbUpdateOperator() {
        super();
        this.trigger = new OnceTrigger();
    }

    @Override
    public String getDSL() {


        return update.toBsonDocument().toJson();
    }

    @Override
    public UpdateOperator parse(String dsl) {
        throw Exceptions.unOperate("parse");
    }

    @Override
    public void reset() {
        clear();
        this.fromColl = null;
        this.update = null;
        this.trigger = new OnceTrigger();
    }

    @Override
    public void setDBType(DBType dbType) {
        // nothing to do
    }

    @Override
    public DBType getDBType() {
        return DBType.MONGODB;
    }

    @Override
    public String getPrepareDSL() {
        return getDSL();
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        throw Exceptions.unOperate("getPrepareValues");
    }

    @Override
    public UpdateOperator from(Table table) {
        this.fromColl = table;
        return self();
    }

    @Override
    public Table getTable() {
        return fromColl;
    }

    @Override
    public UpdateOperator updates(Map<DSLName, Object> values) {
        this.trigger.setAll(values);
        this.update = this.trigger.doIt();
        return self();
    }

    @Override
    public UpdateOperator strictFill(String f, Supplier<Object> v) {
        this.trigger.set(f, v.get());
        this.update = this.trigger.doIt();
        return self();
    }

    // cache to update k-v info. until to through method trigger once more get mongodb value
    static class OnceTrigger {

        private final Map<String, Object> updates;

        public OnceTrigger() {
            this.updates = Maps.newHashMap();
        }

        /**
         * set k-v to in cache
         *
         * @param k the key not null
         * @param v the v
         */
        public void set(String k, Object v) {
            Object bsonValue = MongodbSupport.toBsonValue(v);
            this.updates.put(k, bsonValue);
        }

        /**
         * set all values
         *
         * @param values the map values
         */
        public void setAll(Map<DSLName, Object> values) {
            for (Map.Entry<DSLName, Object> entry : values.entrySet()) {
                DSLName dslName = entry.getKey();
                Object value = entry.getValue();
                set(dslName.format(), value);
            }
        }

        /**
         * create a Bson Update instance from cache
         *
         * @return bson instance
         */
        public Bson doIt() {
            List<Bson> combines = Lists.newArrayList();
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                Bson field = Updates.set(key, value);
                combines.add(field);
            }
            return Updates.combine(combines);
        }
    }
}
