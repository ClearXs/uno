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

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * mongodb of collection operator
 *
 * @author j.x
 * @since 1.1.7
 */
@Getter
@AutoService(CreateTableOperator.class)
@Operator.Group(OperatorKey.MONGODB_LITERAL)
public class MongodbCreateCollectionOperator implements CreateTableOperator<MongodbCreateCollectionOperator> {

    private Table fromColl;
    private List<ColumnDef> columnDefs = new ArrayList<>();

    // @see https://www.mongodb.com/docs/mongodb-vscode/playground-databases/
    @Override
    public String getDSL() {
        if (fromColl == null) {
            throw Exceptions.unNull("from coll is null");
        }
        BsonDocument bson = new BsonDocument("of", new BsonString(fromColl.getName().format()));
        return bson.toJson();
    }

    @Override
    public MongodbCreateCollectionOperator parse(String dsl) {
        reset();
        return self();
    }

    @Override
    public MongodbCreateCollectionOperator customize(UnaryOperator<MongodbCreateCollectionOperator> operatorFunc) {
        return operatorFunc.apply(new MongodbCreateCollectionOperator());
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
    public MongodbCreateCollectionOperator from(Table table) {
        this.fromColl = table;
        return self();
    }

    @Override
    public Table getTable() {
        return this.fromColl;
    }

    @Override
    public MongodbCreateCollectionOperator column(ColumnDef columnDef) {
        columnDefs.add(columnDef);
        // nothing to do
        return self();
    }

    @Override
    public MongodbCreateCollectionOperator comment(String comment) {
        // nothing to do
        return self();
    }

    @Override
    public List<ColumnDef> getColumns() {
        return columnDefs;
    }
}
