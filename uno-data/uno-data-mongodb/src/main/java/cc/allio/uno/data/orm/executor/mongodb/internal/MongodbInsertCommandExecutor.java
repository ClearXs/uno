package cc.allio.uno.data.orm.executor.mongodb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.mongodb.dml.MongodbInsertOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.IOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;

/**
 * mongodb insert command executor
 *
 * @author j.x
 * @date 2024/3/15 11:32
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
        return null;
    }
}
