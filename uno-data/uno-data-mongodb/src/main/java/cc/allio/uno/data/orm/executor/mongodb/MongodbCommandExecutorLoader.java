package cc.allio.uno.data.orm.executor.mongodb;

import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.CommandExecutorLoader;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import cc.allio.uno.data.orm.executor.options.ExecutorOptionsImpl;
import com.google.auto.service.AutoService;

import java.util.List;

/**
 * loader mongodb
 *
 * @author j.x
 * @date 2024/3/15 11:44
 * @since 1.1.7
 */
@AutoService(CommandExecutorLoader.class)
public class MongodbCommandExecutorLoader implements CommandExecutorLoader<MongodbCommandExecutor> {

    @Override
    public MongodbCommandExecutor load(List<Interceptor> interceptors) {
        ExecutorOptions executorOptions = new ExecutorOptionsImpl(DBType.MONGODB, ExecutorKey.MONGODB, OperatorKey.MONGODB);
        executorOptions.addInterceptors(interceptors);
        return load(executorOptions);
    }

    @Override
    public MongodbCommandExecutor load(ExecutorOptions executorOptions) {
        return new MongodbCommandExecutor(executorOptions);
    }

    @Override
    public boolean match(DBType dbType) {
        return DBType.MONGODB == dbType;
    }
}
