package cc.allio.uno.data.orm.executor.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.RedisInsertOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;

@AutoService(IOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.REDIS_LITERAL)
public class RedisInsertCommandExecutor implements IOInnerCommandExecutor<RedisInsertOperator>{

    @Override
    public Boolean doExec(RedisInsertOperator operator, ResultSetHandler<Boolean> handler) throws Throwable {
        return null;
    }
}
