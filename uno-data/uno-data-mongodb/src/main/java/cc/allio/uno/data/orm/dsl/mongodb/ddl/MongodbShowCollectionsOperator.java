package cc.allio.uno.data.orm.dsl.mongodb.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.ddl.ShowTablesOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * mongodb show collections operator
 *
 * @author j.x
 * @date 2024/3/12 01:11
 * @since 1.1.7
 */
@Getter
@AutoService(ShowTablesOperator.class)
@Operator.Group(OperatorKey.MONGODB_LITERAL)
public class MongodbShowCollectionsOperator implements ShowTablesOperator<MongodbShowCollectionsOperator> {

    private Database fromDb;

    private final List<Table> tables = Lists.newArrayList();

    @Override
    public MongodbShowCollectionsOperator database(Database database) {
        this.fromDb = database;
        return self();
    }

    // @see https://www.mongodb.com/docs/manual/reference/command/listCollections/#std-label-list-collection-output
    @Override
    public String getDSL() {
        if (tables.isEmpty()) {
            throw Exceptions.unOperate("check collections is empty");
        }
        BsonDocument bson = new BsonDocument("listCollections", new BsonInt32(1));
        BsonArray collections = new BsonArray();
        for (Table table : tables) {
            collections.add(new BsonString(table.getName().format()));
        }
        BsonDocument filter = new BsonDocument("name", new BsonDocument("$eq", collections));
        bson.append("filter", filter);
        return bson.toJson();
    }

    @Override
    public MongodbShowCollectionsOperator parse(String dsl) {
        reset();
        return self();
    }

    @Override
    public MongodbShowCollectionsOperator customize(UnaryOperator<MongodbShowCollectionsOperator> operatorFunc) {
        return operatorFunc.apply(new MongodbShowCollectionsOperator());
    }

    @Override
    public void reset() {
        this.fromDb = null;
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
    public Table getTable() {
        throw Exceptions.unOperate("getTable");
    }

    @Override
    public QueryOperator<?> toQueryOperator() {
        throw Exceptions.unOperate("toQueryOperator");
    }

    @Override
    public MongodbShowCollectionsOperator schema(String schema) {
        // nothing to do
        return self();
    }

    @Override
    public MongodbShowCollectionsOperator from(Table table) {
        this.tables.add(table);
        return self();
    }
}
