package cc.allio.uno.data.orm.dsl.mongodb.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.ddl.AlterTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import lombok.Getter;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonString;

import java.util.Collection;
import java.util.function.UnaryOperator;

/**
 * mongodb alter collection(table) operator
 *
 * @author j.x
 * @since 1.1.7
 */
@Getter
@AutoService(AlterTableOperator.class)
@Operator.Group(OperatorKey.MONGODB_LITERAL)
public class MongodbAlterCollectionOperator implements AlterTableOperator<MongodbAlterCollectionOperator> {

    private Table fromColl;
    private Table toColl;

    // @see https://www.mongodb.com/docs/manual/reference/command/renameCollection/
    @Override
    public String getDSL() {
        if (fromColl == null || toColl == null) {
            throw Exceptions.unNull("from coll is null or to coll is null");
        }
        BsonDocument bson =
                new BsonDocument("renameCollection", new BsonString(fromColl.getName().format()))
                        .append("to", new BsonString(toColl.getName().format()));
        return bson.toJson();
    }

    @Override
    public MongodbAlterCollectionOperator parse(String dsl) {
        // nothing to do
        reset();
        return self();
    }

    @Override
    public MongodbAlterCollectionOperator customize(UnaryOperator<MongodbAlterCollectionOperator> operatorFunc) {
        return operatorFunc.apply(new MongodbAlterCollectionOperator());
    }

    @Override
    public void reset() {
        this.fromColl = null;
        this.toColl = null;
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
    public MongodbAlterCollectionOperator from(Table table) {
        this.fromColl = table;
        return self();
    }

    @Override
    public Table getTable() {
        return this.fromColl;
    }

    @Override
    public MongodbAlterCollectionOperator alertColumns(Collection<ColumnDef> columnDefs) {
        // nothing to do
        return self();
    }

    @Override
    public MongodbAlterCollectionOperator addColumns(Collection<ColumnDef> columnDefs) {
        // nothing to do
        return self();
    }

    @Override
    public MongodbAlterCollectionOperator deleteColumns(Collection<DSLName> columns) {
        // nothing to do
        return self();
    }

    @Override
    public MongodbAlterCollectionOperator rename(Table to) {
        this.toColl = to;
        return self();
    }
}
