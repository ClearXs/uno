package cc.allio.uno.data.orm.dsl.mongodb.dml;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.PrepareValue;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.dml.DeleteOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import org.bson.BsonDocument;

import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * mongodb delete document operator
 *
 * @author j.x
 * @since 1.1.7
 */
@AutoService(DeleteOperator.class)
@Operator.Group(OperatorKey.MONGODB_LITERAL)
public class MongodbDeleteOperator extends MongodbWhereOperatorImpl<MongodbDeleteOperator> implements DeleteOperator<MongodbDeleteOperator> {

    private Table fromColl;

    public MongodbDeleteOperator() {
        super();
    }

    @Override
    public String getDSL() {
        return getFilter().toBsonDocument().toJson();
    }

    @Override
    public MongodbDeleteOperator parse(String dsl) {
        this.filter = BsonDocument.parse(dsl);
        return self();
    }

    @Override
    public MongodbDeleteOperator customize(UnaryOperator<MongodbDeleteOperator> operatorFunc) {
        return operatorFunc.apply(new MongodbDeleteOperator());
    }

    @Override
    public void reset() {
        clear();
        this.fromColl = null;
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
        return Collections.emptyList();
    }

    @Override
    public MongodbDeleteOperator from(Table table) {
        this.fromColl = table;
        return self();
    }

    @Override
    public Table getTable() {
        return fromColl;
    }
}
