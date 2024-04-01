package cc.allio.uno.data.orm.config.influxdb;

import cc.allio.uno.data.orm.executor.influxdb.InfluxdbCommandExecutorLoader;
import cc.allio.uno.data.orm.executor.influxdb.InfluxdbCommandExecutorProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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
}
