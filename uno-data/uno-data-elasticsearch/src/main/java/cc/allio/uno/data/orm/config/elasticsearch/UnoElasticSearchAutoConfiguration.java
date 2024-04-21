package cc.allio.uno.data.orm.config.elasticsearch;

import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.CommandExecutorRegistry;
import cc.allio.uno.data.orm.executor.ExecutorInitializerAutoConfiguration;
import cc.allio.uno.data.orm.executor.ExecutorOptionsBuilder;
import cc.allio.uno.data.orm.executor.elasticsearch.EsCommandExecutor;
import cc.allio.uno.data.orm.executor.elasticsearch.EsCommandExecutorLoader;
import cc.allio.uno.data.orm.executor.elasticsearch.EsCommandExecutorProcessor;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

import java.util.List;

/**
 * configuration for elasticsearch
 *
 * @author j.x
 * @date 2023/3/14 20:01
 * @since 1.1.4
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(ExecutorInitializerAutoConfiguration.class)
@AutoConfigureAfter(ElasticsearchRestClientAutoConfiguration.class)
@ConditionalOnClass(RestClientBuilder.class)
@EnableConfigurationProperties(ElasticSearchProperties.class)
public class UnoElasticSearchAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EsCommandExecutorLoader esCommandExecutorLoader(RestClientBuilder builder) {
        return new EsCommandExecutorLoader(builder);
    }

    @Bean
    @ConditionalOnBean(EsCommandExecutorLoader.class)
    public EsCommandExecutorProcessor esExecutorProcessor() {
        return new EsCommandExecutorProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(CommandExecutorRegistry.class)
    @ConditionalOnProperty(prefix = "allio.uno.data.elasticsearch", name = "enabled", havingValue = "true")
    public EsCommandExecutor defaultDbCommandExecutor(EsCommandExecutorLoader commandExecutorLoader,
                                                      ElasticSearchProperties elasticSearchProperties,
                                                      CommandExecutorRegistry commandExecutorRegistry,
                                                      ObjectProvider<List<Interceptor>> interceptorProvider) {
        List<Interceptor> interceptors = interceptorProvider.getIfAvailable(List::of);
        ExecutorOptions executorOptions =
                ExecutorOptionsBuilder.create(DBType.ELASTICSEARCH)
                        .address(elasticSearchProperties.getAddress())
                        .username(elasticSearchProperties.getUsername())
                        .password(elasticSearchProperties.getPassword())
                        .executorKey(ExecutorKey.ELASTICSEARCH)
                        .operatorKey(OperatorKey.ELASTICSEARCH)
                        .interceptors(interceptors)
                        .build();
        return commandExecutorRegistry.register(executorOptions, () -> commandExecutorLoader.load(executorOptions), false);
    }
}
