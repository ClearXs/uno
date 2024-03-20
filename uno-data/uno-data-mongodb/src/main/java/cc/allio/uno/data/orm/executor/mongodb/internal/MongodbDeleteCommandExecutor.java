package cc.allio.uno.data.orm.executor.mongodb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.mongodb.dml.MongodbDeleteOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.DOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;

/**
 * mongodb insert command executor
 *
 * @author j.x
 * @date 2024/3/15 11:30
 * @since 1.1.7
 */
@Slf4j
@AutoService(DOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.MONGODB_LITERAL)
public class MongodbDeleteCommandExecutor implements DOInnerCommandExecutor<MongodbDeleteOperator> {

    final MongoDatabase database;

    public MongodbDeleteCommandExecutor(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public Boolean doExec(MongodbDeleteOperator operator, ResultSetHandler<Boolean> handler) throws Throwable {
        return null;
    }
}
