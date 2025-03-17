package cc.allio.uno.data.orm.executor.mongodb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.Requires;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.mongodb.ddl.MongodbCreateCollectionOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.result.ResultGroup;
import cc.allio.uno.data.orm.executor.result.ResultRow;
import cc.allio.uno.data.orm.executor.handler.BoolResultHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.CTOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.mongodb.client.MongoDatabase;
import com.mongodb.internal.operation.CreateCollectionOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * mongodb of collection command executor
 *
 * @author j.x
 * @see CreateCollectionOperation
 * @since 1.1.7
 */
@Slf4j
@AutoService(CTOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.MONGODB_LITERAL)
public class MongodbCreateCollectionCommandExecutor implements CTOInnerCommandExecutor<MongodbCreateCollectionOperator> {

    final MongoDatabase database;

    public MongodbCreateCollectionCommandExecutor(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public Boolean doExec(MongodbCreateCollectionOperator operator, ResultSetHandler<Boolean> handler) throws Throwable {
        Table fromColl = operator.getFromColl();
        Requires.isNotNull(fromColl, "from collection");
        ResultGroup resultGroup = new ResultGroup();
        ResultRow.ResultRowBuilder builder = ResultRow.builder();
        builder.column(BoolResultHandler.GUESS_UPDATE_OR_UPDATE);
        try {
            database.createCollection(fromColl.getName().format());
            builder.value(true);
        } catch (Throwable ex) {
            log.error("mongodb of collection has error, the from collection is {}", fromColl.getName().format(), ex);
            builder.value(false);
        }
        ResultRow resultRow = builder.build();
        resultGroup.addRow(resultRow);
        print(log, Map.of("fromColl", fromColl.getName().format(), "result", resultRow.getValue()));
        return handler.apply(resultGroup);
    }
}
