package cc.allio.uno.data.orm.config.mongodb;

import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.CommandExecutorRegistry;
import cc.allio.uno.data.orm.executor.ExecutorOptionsBuilder;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import cc.allio.uno.data.orm.executor.mongodb.MongodbCommandExecutor;
import cc.allio.uno.data.orm.executor.mongodb.MongodbCommandExecutorLoader;
import cc.allio.uno.data.orm.executor.mongodb.MongodbCommandExecutorProcessor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * configuration for mongodb
 *
 * @author j.x
 * @date 2024/3/14 19:37
 * @since 1.1.7
 */
@Configuration
@EnableConfigurationProperties(MongodbProperties.class)
public class UnoMongodbAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MongodbCommandExecutorLoader mongodbCommandExecutorLoader() {
        return new MongodbCommandExecutorLoader();
    }

    @Bean
    @ConditionalOnBean(MongodbCommandExecutorLoader.class)
    public MongodbCommandExecutorProcessor mongodbCommandExecutorProcessor() {
        return new MongodbCommandExecutorProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(CommandExecutorRegistry.class)
    @ConditionalOnProperty(prefix = "allio.uno.data.mongodb", name = "enabled", havingValue = "true")
    public MongodbCommandExecutor defaultDbCommandExecutor(MongodbCommandExecutorLoader commandExecutorLoader,
                                                           MongodbProperties mongodbProperties,
                                                           CommandExecutorRegistry commandExecutorRegistry,
                                                           ObjectProvider<List<Interceptor>> interceptorProvider) {
        List<Interceptor> interceptors = interceptorProvider.getIfAvailable(List::of);
        ExecutorOptions executorOptions =
                ExecutorOptionsBuilder.create(DBType.MONGODB)
                        .address(mongodbProperties.getAddress())
                        .username(mongodbProperties.getUsername())
                        .password(mongodbProperties.getPassword())
                        .executorKey(ExecutorKey.MONGODB)
                        .operatorKey(OperatorKey.MONGODB)
                        .interceptors(interceptors)
                        .build();
        return commandExecutorRegistry.register(executorOptions, () -> commandExecutorLoader.load(executorOptions), false);
    }
}
