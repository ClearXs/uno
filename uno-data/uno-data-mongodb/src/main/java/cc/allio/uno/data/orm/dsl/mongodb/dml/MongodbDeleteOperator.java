package cc.allio.uno.data.orm.dsl.mongodb.dml;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.PrepareValue;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.dml.DeleteOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.mongodb.client.model.Filters;
import org.bson.BsonDocument;

import java.util.List;

/**
 * mongodb delete document operator
 *
 * @author j.x
 * @date 2024/3/14 00:31
 * @since 1.1.7
 */
@AutoService(DeleteOperator.class)
@Operator.Group(OperatorKey.MONGODB_LITERAL)
public class MongodbDeleteOperator extends MongodbWhereOperatorImpl<DeleteOperator> implements DeleteOperator {

    private Table fromColl;

    public MongodbDeleteOperator() {
        super();
    }

    @Override
    public String getDSL() {
        return getFilter().toBsonDocument().toJson();
    }

    @Override
    public DeleteOperator parse(String dsl) {
        this.filter = BsonDocument.parse(dsl);
        return self();
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
        throw Exceptions.unOperate("getPrepareValues");
    }

    @Override
    public DeleteOperator from(Table table) {
        this.fromColl = table;
        return self();
    }

    @Override
    public Table getTable() {
        return fromColl;
    }
}
