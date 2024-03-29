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

/**
 * mongodb alter collection(table) operator
 *
 * @author j.x
 * @date 2024/3/11 23:12
 * @since 1.1.7
 */
@Getter
@AutoService(AlterTableOperator.class)
@Operator.Group(OperatorKey.MONGODB_LITERAL)
public class MongodbAlterCollectionOperator implements AlterTableOperator {

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
    public AlterTableOperator parse(String dsl) {
        // nothing to do
        reset();
        return self();
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
    public AlterTableOperator from(Table table) {
        this.fromColl = table;
        return self();
    }

    @Override
    public Table getTable() {
        return this.fromColl;
    }

    @Override
    public AlterTableOperator alertColumns(Collection<ColumnDef> columnDefs) {
        // nothing to do
        return self();
    }

    @Override
    public AlterTableOperator addColumns(Collection<ColumnDef> columnDefs) {
        // nothing to do
        return self();
    }

    @Override
    public AlterTableOperator deleteColumns(Collection<DSLName> columns) {
        // nothing to do
        return self();
    }

    @Override
    public AlterTableOperator rename(Table to) {
        this.toColl = to;
        return self();
    }
}
