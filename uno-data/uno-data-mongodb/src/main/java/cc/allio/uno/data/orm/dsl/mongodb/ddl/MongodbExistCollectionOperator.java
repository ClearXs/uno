package cc.allio.uno.data.orm.dsl.mongodb.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.PrepareValue;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.ExistTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * mongodb exist collection operator
 *
 * @author j.x
 * @since 1.1.7
 */
@AutoService(ExistTableOperator.class)
@Operator.Group(OperatorKey.MONGODB_LITERAL)
public class MongodbExistCollectionOperator implements ExistTableOperator<MongodbExistCollectionOperator> {

    private Table fromColl;

    // @see https://www.mongodb.com/docs/manual/reference/command/listCollections/#std-label-list-collection-output
    @Override
    public String getDSL() {
        if (fromColl == null) {
            throw Exceptions.unNull("from coll is null");
        }
        BsonDocument bson = new BsonDocument("listCollections", new BsonInt32(1));
        BsonDocument filter = new BsonDocument("name", new BsonDocument("$eq", new BsonString(fromColl.getName().format())));
        bson.append("filter", filter);
        return bson.toJson();
    }

    @Override
    public MongodbExistCollectionOperator parse(String dsl) {
        reset();
        return self();
    }

    @Override
    public MongodbExistCollectionOperator customize(UnaryOperator<MongodbExistCollectionOperator> operatorFunc) {
        return operatorFunc.apply(new MongodbExistCollectionOperator());
    }

    @Override
    public void reset() {
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
        return List.of();
    }

    @Override
    public MongodbExistCollectionOperator from(Table table) {
        this.fromColl = table;
        return self();
    }

    @Override
    public Table getTable() {
        return fromColl;
    }
}
