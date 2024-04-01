package cc.allio.uno.data.orm.config.mongodb;

import cc.allio.uno.data.orm.executor.mongodb.MongodbCommandExecutorLoader;
import cc.allio.uno.data.orm.executor.mongodb.MongodbCommandExecutorProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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
}
