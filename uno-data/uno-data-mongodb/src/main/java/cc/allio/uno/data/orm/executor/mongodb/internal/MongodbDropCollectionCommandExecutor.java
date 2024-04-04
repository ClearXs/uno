package cc.allio.uno.data.orm.executor.mongodb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.Requires;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.mongodb.ddl.MongodbDropCollectionOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.ResultGroup;
import cc.allio.uno.data.orm.executor.ResultRow;
import cc.allio.uno.data.orm.executor.handler.BoolResultHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.DTOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.internal.operation.DropCollectionOperation;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.util.Map;

/**
 * mongodb drop collection command executor
 *
 * @author j.x
 * @date 2024/3/15 11:17
 * @see DropCollectionOperation
 * @since 1.1.7
 */
@Slf4j
@AutoService(DTOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.MONGODB_LITERAL)
public class MongodbDropCollectionCommandExecutor implements DTOInnerCommandExecutor<MongodbDropCollectionOperator> {

    final MongoDatabase database;

    public MongodbDropCollectionCommandExecutor(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public Boolean doExec(MongodbDropCollectionOperator operator, ResultSetHandler<Boolean> handler) throws Throwable {
        Table fromColl = operator.getTable();
        Requires.isNotNull(fromColl, "from collection");
        ResultGroup resultGroup = new ResultGroup();
        ResultRow.ResultRowBuilder builder = ResultRow.builder();
        builder.column(BoolResultHandler.GUESS_UPDATE_OR_UPDATE);
        try {
            MongoCollection<Document> collection = database.getCollection(fromColl.getName().format());
            collection.drop();
            builder.value(true);
        } catch (Throwable ex) {
            log.error("mongodb drop collection has error", ex);
            builder.value(false);
        }
        ResultRow resultRow = builder.build();
        print(log, Map.of("fromColl", fromColl.getName().format(), "result", resultRow.getValue()));
        resultGroup.addRow(resultRow);
        return handler.apply(resultGroup);
    }
}
