package cc.allio.uno.data.orm.executor.mongodb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.Requires;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.mongodb.dml.MongodbUpdateOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.result.ResultGroup;
import cc.allio.uno.data.orm.executor.result.ResultRow;
import cc.allio.uno.data.orm.executor.handler.BoolResultHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.UOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Map;

/**
 * mongodb update command executor
 *
 * @author j.x
 * @since 1.1.7
 */
@Slf4j
@AutoService(UOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.MONGODB_LITERAL)
public class MongodbUpdateCommandExecutor implements UOInnerCommandExecutor<MongodbUpdateOperator> {

    final MongoDatabase database;

    public MongodbUpdateCommandExecutor(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public Boolean doExec(MongodbUpdateOperator operator, ResultSetHandler<Boolean> handler) throws Throwable {
        // validate
        Table fromColl = operator.getTable();
        Requires.isNotNull(fromColl, "from collection");

        // initial result group
        ResultGroup resultGroup = new ResultGroup();
        ResultRow.ResultRowBuilder builder = ResultRow.builder();
        builder.column(BoolResultHandler.GUESS_UPDATE_OR_UPDATE);

        Bson filter = operator.getFilter();
        Bson update = operator.getUpdate();
        MongoCollection<Document> collection = database.getCollection(fromColl.getName().format());
        try {
            UpdateResult updateResult = collection.updateMany(filter, update);
            builder.value(updateResult.wasAcknowledged() && updateResult.getModifiedCount() > 0);
        } catch (Throwable ex) {
            log.error("Failed to mongodb update document. the filter bson is {}, update bson is {}", filter, update, ex);
            builder.value(false);
        }
        ResultRow resultRow = builder.build();
        print(log, Map.of("fromColl", fromColl.getName().format(), "filter", filter, "update", update, "result", resultRow.getValue()));
        resultGroup.addRow(resultRow);
        return handler.apply(resultGroup);
    }
}
