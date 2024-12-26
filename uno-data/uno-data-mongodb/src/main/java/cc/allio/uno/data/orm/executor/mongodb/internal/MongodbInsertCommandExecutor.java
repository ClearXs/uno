package cc.allio.uno.data.orm.executor.mongodb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.Requires;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.mongodb.dml.MongodbInsertOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.ResultGroup;
import cc.allio.uno.data.orm.executor.ResultRow;
import cc.allio.uno.data.orm.executor.handler.BoolResultHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.IOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertManyResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonArray;
import org.bson.Document;

import java.util.List;
import java.util.Map;

/**
 * mongodb insert command executor
 *
 * @author j.x
 * @since 1.1.7
 */
@Slf4j
@AutoService(IOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.MONGODB_LITERAL)
public class MongodbInsertCommandExecutor implements IOInnerCommandExecutor<MongodbInsertOperator> {

    final MongoDatabase database;

    public MongodbInsertCommandExecutor(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public Boolean doExec(MongodbInsertOperator operator, ResultSetHandler<Boolean> handler) throws Throwable {
        Table fromColl = operator.getTable();
        Requires.isNotNull(fromColl, "from collection");

        // initial result group
        ResultGroup resultGroup = new ResultGroup();
        ResultRow.ResultRowBuilder builder = ResultRow.builder();
        builder.column(BoolResultHandler.GUESS_UPDATE_OR_UPDATE);

        List<Document> docs = operator.getDocs();
        MongoCollection<Document> collection = database.getCollection(fromColl.getName().format());

        try {
            InsertManyResult insertManyResult = collection.insertMany(docs);
            // ack and insert id is not empty
            builder.value(insertManyResult.wasAcknowledged() && !insertManyResult.getInsertedIds().isEmpty());
        } catch (Throwable ex) {
            log.error("Failed to mongodb insert document. the docs {}", docs, ex);
            builder.value(false);
        }

        ResultRow resultRow = builder.build();
        print(log, Map.of("fromColl", fromColl.getName().format(), "docs", docs, "result", resultRow.getValue()));
        resultGroup.addRow(resultRow);
        return handler.apply(resultGroup);
    }
}
