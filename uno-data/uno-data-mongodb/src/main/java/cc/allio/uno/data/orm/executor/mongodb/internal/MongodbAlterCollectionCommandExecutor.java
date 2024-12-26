package cc.allio.uno.data.orm.executor.mongodb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.Requires;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.mongodb.ddl.MongodbAlterCollectionOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.ResultGroup;
import cc.allio.uno.data.orm.executor.ResultRow;
import cc.allio.uno.data.orm.executor.handler.BoolResultHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.ATOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.mongodb.MongoNamespace;
import com.mongodb.client.MongoDatabase;
import com.mongodb.internal.operation.RenameCollectionOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * mongodb alter collection command executor
 *
 * @author j.x
 * @see RenameCollectionOperation
 * @since 1.1.7
 */
@Slf4j
@AutoService(ATOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.MONGODB_LITERAL)
public class MongodbAlterCollectionCommandExecutor implements ATOInnerCommandExecutor<MongodbAlterCollectionOperator> {

    final MongoDatabase database;

    public MongodbAlterCollectionCommandExecutor(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public Boolean doExec(MongodbAlterCollectionOperator operator, ResultSetHandler<Boolean> handler) throws Throwable {
        Table fromColl = operator.getFromColl();
        Table toColl = operator.getToColl();
        Requires.isNotNull(fromColl, "from collection");
        Requires.isNotNull(toColl, "to collection");
        ResultGroup resultGroup = new ResultGroup();
        ResultRow.ResultRowBuilder builder = ResultRow.builder();
        builder.column(BoolResultHandler.GUESS_UPDATE_OR_UPDATE);
        try {
            String from = fromColl.getName().format();
            String to = toColl.getName().format();
            database.getCollection(from).renameCollection(new MongoNamespace(database.getName(), to));
            builder.value(true);
        } catch (Throwable ex) {
            log.error("mongodb alter table has error, the from collection {} alter to collection {}", fromColl.getName().format(), toColl.getName().format(), ex);
            builder.value(false);
        }
        ResultRow resultRow = builder.build();
        resultGroup.addRow(resultRow);
        print(log, Map.of("fromColl", fromColl.getName().format(), "toColl", toColl.getName().format(), "result", resultRow.getValue()));
        return handler.apply(resultGroup);
    }
}
