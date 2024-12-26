package cc.allio.uno.data.orm.executor.mongodb;

import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.BaseCommandExecutorLoader;
import cc.allio.uno.data.orm.executor.CommandExecutorLoader;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import com.google.auto.service.AutoService;

import java.util.List;

/**
 * loader mongodb
 *
 * @author j.x
 * @since 1.1.7
 */
@AutoService(CommandExecutorLoader.class)
public class MongodbCommandExecutorLoader extends BaseCommandExecutorLoader<MongodbCommandExecutor> {

    @Override
    public MongodbCommandExecutor onLoad(List<Interceptor> interceptors) {
        return null;
    }

    @Override
    public MongodbCommandExecutor onLoad(ExecutorOptions executorOptions) {
        return new MongodbCommandExecutor(executorOptions);
    }

    @Override
    public boolean match(DBType dbType) {
        return DBType.MONGODB == dbType;
    }
}
