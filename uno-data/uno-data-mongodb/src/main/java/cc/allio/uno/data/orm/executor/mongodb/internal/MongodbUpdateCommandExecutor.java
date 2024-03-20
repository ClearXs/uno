package cc.allio.uno.data.orm.executor.mongodb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.mongodb.dml.MongodbUpdateOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.UOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;

/**
 * mongodb update command executor
 *
 * @author j.x
 * @date 2024/3/15 11:35
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
        return null;
    }
}
