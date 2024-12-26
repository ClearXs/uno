package cc.allio.uno.data.orm.dsl.mongodb.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.DropTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.mongodb.internal.operation.DropCollectionOperation;
import org.bson.BsonDocument;
import org.bson.BsonString;

import java.util.function.UnaryOperator;

/**
 * mongodb drop collection operator
 *
 * @author j.x
 * @since 1.1.7
 */
@AutoService(DropTableOperator.class)
@Operator.Group(OperatorKey.MONGODB_LITERAL)
public class MongodbDropCollectionOperator implements DropTableOperator<MongodbDropCollectionOperator> {

    private Table fromColl;

    @Override
    public String getDSL() {
        if (fromColl == null) {
            throw Exceptions.unNull("from coll is null");
        }
        BsonDocument bson = new BsonDocument("drop", new BsonString(fromColl.getName().format()));
        return bson.toJson();
    }

    @Override
    public MongodbDropCollectionOperator parse(String dsl) {
        reset();
        return self();
    }

    @Override
    public MongodbDropCollectionOperator customize(UnaryOperator<MongodbDropCollectionOperator> operatorFunc) {
        return operatorFunc.apply(new MongodbDropCollectionOperator());
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
    public MongodbDropCollectionOperator from(Table table) {
        this.fromColl = table;
        return self();
    }

    @Override
    public Table getTable() {
        return fromColl;
    }

    @Override
    public MongodbDropCollectionOperator ifExist(Boolean ifExist) {
        // nothing to do
        return self();
    }
}
