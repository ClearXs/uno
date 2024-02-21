package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.ExecutorLoader;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import cc.allio.uno.data.orm.executor.options.ExecutorOptionsImpl;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import org.elasticsearch.client.RestClientBuilder;

import java.util.List;

public class EsCommandExecutorLoader implements ExecutorLoader {

    private final RestClientBuilder restClientBuilder;

    public EsCommandExecutorLoader(RestClientBuilder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
    }

    @Override
    public CommandExecutor load(List<Interceptor> interceptors) {
        ExecutorOptions executorOptions = new ExecutorOptionsImpl(DBType.ELASTIC_SEARCH, ExecutorKey.ELASTICSEARCH, OperatorKey.ELASTICSEARCH);
        executorOptions.addInterceptors(interceptors);
        return load(executorOptions);
    }

    @Override
    public CommandExecutor load(ExecutorOptions executorOptions) {
        return new EsCommandExecutor(executorOptions, restClientBuilder);
    }

    @Override
    public boolean match(DBType dbType) {
        return DBType.ELASTIC_SEARCH == dbType;
    }
}
