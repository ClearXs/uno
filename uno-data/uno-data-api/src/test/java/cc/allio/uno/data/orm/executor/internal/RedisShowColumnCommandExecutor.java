package cc.allio.uno.data.orm.executor.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.RedisShowColumnsOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.handler.ListResultSetHandler;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;

import java.util.List;

@AutoService(SCOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.REDIS_LITERAL)
public class RedisShowColumnCommandExecutor implements SCOInnerCommandExecutor<ColumnDef, RedisShowColumnsOperator> {

    @Override
    public List<ColumnDef> doExec(RedisShowColumnsOperator operator, ListResultSetHandler<ColumnDef> handler) throws Throwable {
        return null;
    }
}
