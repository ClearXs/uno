package cc.allio.uno.data.orm.dsl.mongodb.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.CreateTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import lombok.Getter;
import org.bson.BsonDocument;
import org.bson.BsonString;

/**
 * mongodb create collection operator
 *
 * @author j.x
 * @date 2024/3/12 00:57
 * @since 1.1.7
 */
@Getter
@AutoService(CreateTableOperator.class)
@Operator.Group(OperatorKey.MONGODB_LITERAL)
public class MongodbCreateCollectionOperator implements CreateTableOperator {

    private Table fromColl;

    // @see https://www.mongodb.com/docs/mongodb-vscode/playground-databases/
    @Override
    public String getDSL() {
        if (fromColl == null) {
            throw Exceptions.unNull("from coll is null");
        }
        BsonDocument bson = new BsonDocument("create", new BsonString(fromColl.getName().format()));
        return bson.toJson();
    }

    @Override
    public CreateTableOperator parse(String dsl) {
        reset();
        return self();
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
    public CreateTableOperator from(Table table) {
        this.fromColl = table;
        return self();
    }

    @Override
    public Table getTable() {
        return this.fromColl;
    }

    @Override
    public CreateTableOperator column(ColumnDef columnDef) {
        // nothing to do
        return self();
    }

    @Override
    public CreateTableOperator comment(String comment) {
        // nothing to do
        return self();
    }
}
