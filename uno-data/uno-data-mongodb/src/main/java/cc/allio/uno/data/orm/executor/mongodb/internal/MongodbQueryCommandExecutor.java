package cc.allio.uno.data.orm.executor.mongodb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.mongodb.dml.MongodbQueryOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.handler.ListResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.QOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * mongodb query command executor
 *
 * @author j.x
 * @date 2024/3/15 11:34
 * @since 1.1.7
 */
@Slf4j
@AutoService(QOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.MONGODB_LITERAL)
public class MongodbQueryCommandExecutor<R> implements QOInnerCommandExecutor<R, MongodbQueryOperator> {

    final MongoDatabase database;

    public MongodbQueryCommandExecutor(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public List<R> doExec(MongodbQueryOperator operator, ListResultSetHandler<R> handler) throws Throwable {
        return null;
    }
}
