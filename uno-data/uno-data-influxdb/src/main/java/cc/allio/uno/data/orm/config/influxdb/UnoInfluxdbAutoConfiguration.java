package cc.allio.uno.data.orm.config.influxdb;

import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.CommandExecutorRegistry;
import cc.allio.uno.data.orm.executor.ExecutorOptionsBuilder;
import cc.allio.uno.data.orm.executor.influxdb.InfluxdbCommandExecutor;
import cc.allio.uno.data.orm.executor.influxdb.InfluxdbCommandExecutorLoader;
import cc.allio.uno.data.orm.executor.influxdb.InfluxdbCommandExecutorProcessor;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
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
 * configuration for uno influxdb
 *
 * @author j.x
 * @since 1.1.8
 */
@Configuration
@EnableConfigurationProperties(InfluxdbProperties.class)
public class UnoInfluxdbAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public InfluxdbCommandExecutorLoader influxdbCommandExecutorLoader() {
        return new InfluxdbCommandExecutorLoader();
    }

    @Bean
    @ConditionalOnBean(InfluxdbCommandExecutorLoader.class)
    public InfluxdbCommandExecutorProcessor influxdbCommandExecutorProcessor() {
        return new InfluxdbCommandExecutorProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(CommandExecutorRegistry.class)
    @ConditionalOnProperty(prefix = "allio.uno.data.influxdb", name = "enabled", havingValue = "true")
    public InfluxdbCommandExecutor defaultInfluxdbCommandExecutor(InfluxdbCommandExecutorLoader commandExecutorLoader,
                                                                  InfluxdbProperties influxdbProperties,
                                                                  CommandExecutorRegistry commandExecutorRegistry,
                                                                  ObjectProvider<List<Interceptor>> interceptorProvider) {
        List<Interceptor> interceptors = interceptorProvider.getIfAvailable(List::of);
        // create command option
        ExecutorOptions executorOptions =
                ExecutorOptionsBuilder.create(DBType.INFLUXDB)
                        .address(influxdbProperties.getAddress())
                        .password(influxdbProperties.getPassword())
                        .database(influxdbProperties.getBucket())
                        .username(influxdbProperties.getUsername())
                        .password(influxdbProperties.getPassword())
                        .executorKey(ExecutorKey.INFLUXDB)
                        .operatorKey(OperatorKey.INFLUXDB)
                        .interceptors(interceptors)
                        .systemDefault(influxdbProperties.getSystemDefault())
                        .setProperty(InfluxdbProperties.ORGANIZATION, influxdbProperties.getOrganization())
                        .setProperty(InfluxdbProperties.LOG_LEVEL, influxdbProperties.getLogLevel())
                        .setProperty(InfluxdbProperties.WRITE_PRECISION, influxdbProperties.getWritePrecision())
                        .setProperty(InfluxdbProperties.TOKEN, influxdbProperties.getToken())
                        .build();
        return commandExecutorRegistry.register(executorOptions, () -> commandExecutorLoader.load(executorOptions), false);
    }
}
