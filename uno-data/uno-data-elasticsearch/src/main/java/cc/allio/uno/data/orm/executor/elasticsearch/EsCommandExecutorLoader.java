package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.BaseCommandExecutorLoader;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.CommandExecutorLoader;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import cc.allio.uno.data.orm.executor.options.ExecutorOptionsImpl;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import com.google.auto.service.AutoService;
import org.elasticsearch.client.RestClientBuilder;

import java.util.List;

/**
 * impl for elasticsearch command loader
 *
 * @author j.x
 * @date 2024/3/21 00:07
 * @since 1.1.7
 */
@AutoService(CommandExecutorLoader.class)
public class EsCommandExecutorLoader extends BaseCommandExecutorLoader<EsCommandExecutor> {

    private final RestClientBuilder restClientBuilder;

    public EsCommandExecutorLoader(RestClientBuilder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
    }

    @Override
    public EsCommandExecutor onLoad(List<Interceptor> interceptors) {
        ExecutorOptions executorOptions = new ExecutorOptionsImpl(DBType.ELASTICSEARCH, ExecutorKey.ELASTICSEARCH, OperatorKey.ELASTICSEARCH);
        executorOptions.addInterceptors(interceptors);
        return load(executorOptions);
    }

    @Override
    public EsCommandExecutor onLoad(ExecutorOptions executorOptions) {
        return new EsCommandExecutor(executorOptions, restClientBuilder);
    }

    @Override
    public boolean match(DBType dbType) {
        return DBType.ELASTICSEARCH == dbType;
    }
}
