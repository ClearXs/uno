package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.ExecutorKey;
import cc.allio.uno.data.orm.executor.ExecutorLoader;
import cc.allio.uno.data.orm.executor.ExecutorOptions;
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
        ExecutorOptions executorOptions = new ExecutorOptions();
        executorOptions.addInterceptors(interceptors);
        executorOptions.setDbType(DBType.ELASTIC_SEARCH);
        executorOptions.setOperatorKey(OperatorKey.ELASTICSEARCH);
        executorOptions.setExecutorKey(ExecutorKey.ELASTICSEARCH);
        return new EsCommandExecutor(executorOptions, restClientBuilder);
    }
}
