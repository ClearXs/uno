package cc.allio.uno.data.orm.executor;

import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Configuration
public class ExecutorInitializerAutoConfiguration {

    @Bean
    public ExecutorFactoryBean executorFactoryBean() {
        return new ExecutorFactoryBean();
    }

    @Bean
    public ExecutorInitializer executorInitializer(ObjectProvider<List<CommandExecutorLoader<? extends AggregateCommandExecutor>>> loaderProvider,
                                                   ObjectProvider<List<Interceptor>> interceptorProvider) {
        List<CommandExecutorLoader<? extends AggregateCommandExecutor>> executorLoaders = loaderProvider.getIfAvailable(Collections::emptyList);
        List<Interceptor> interceptors = interceptorProvider.getIfAvailable(Collections::emptyList);
        return new ExecutorInitializer(executorLoaders, interceptors);
    }
}
