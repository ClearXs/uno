package cc.allio.uno.data.test.executor;

import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.CommandExecutorLoader;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import com.google.auto.service.AutoService;

import java.util.List;

@AutoService(CommandExecutorLoader.class)
public class FakeCommandExecutorLoader implements CommandExecutorLoader<FakeCommandExecutor> {

    @Override
    public FakeCommandExecutor load(List<Interceptor> interceptors) {
        return null;
    }

    @Override
    public FakeCommandExecutor load(ExecutorOptions executorOptions) {
        return new FakeCommandExecutor();
    }

    @Override
    public boolean match(DBType dbType) {
        return true;
    }
}
