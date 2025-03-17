package cc.allio.uno.data.orm.executor.mongodb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.Requires;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.mongodb.ddl.MongodbExistCollectionOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.result.ResultGroup;
import cc.allio.uno.data.orm.executor.result.ResultRow;
import cc.allio.uno.data.orm.executor.handler.BoolResultHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.ETOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.google.common.collect.Lists;
import com.mongodb.client.MongoDatabase;
import com.mongodb.internal.operation.ListCollectionsOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * mongodb exist collection command executor
 *
 * @author j.x
 * @see ListCollectionsOperation
 * @since 1.1.7
 */
@Slf4j
@AutoService(ETOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.MONGODB_LITERAL)
public class MongodbExistCollectionCommandExecutor implements ETOInnerCommandExecutor<MongodbExistCollectionOperator> {

    final MongoDatabase database;

    public MongodbExistCollectionCommandExecutor(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public Boolean doExec(MongodbExistCollectionOperator operator, ResultSetHandler<Boolean> handler) throws Throwable {
        Table fromColl = operator.getTable();
        Requires.isNotNull(fromColl, "from collection");
        ResultGroup resultGroup = new ResultGroup();
        ResultRow.ResultRowBuilder builder = ResultRow.builder();
        builder.column(BoolResultHandler.GUESS_UPDATE_OR_UPDATE);
        try {
            boolean present = Lists.newArrayList(database.listCollectionNames())
                    .stream()
                    .anyMatch(c -> c.equals(fromColl.getName().format()));
            builder.value(present);
        } catch (Throwable ex) {
            log.error("mongodb exist collection has error", ex);
            builder.value(false);
        }
        ResultRow resultRow = builder.build();
        print(log, Map.of("fromColl", fromColl.getName().format(), "result", resultRow.getValue()));
        resultGroup.addRow(resultRow);
        return handler.apply(resultGroup);
    }
}
