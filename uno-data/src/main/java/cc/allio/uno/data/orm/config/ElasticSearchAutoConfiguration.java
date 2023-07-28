package cc.allio.uno.data.orm.config;

import cc.allio.uno.data.orm.executor.SQLCommandExecutorFactory;
import cc.allio.uno.data.orm.executor.elasticsearch.EsSQLCommandExecutor;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.context.annotation.*;

import static cc.allio.uno.data.orm.executor.SQLCommandExecutor.ELASTICSEARCH_SQL_COMMAND_EXECUTOR_KEY;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(ElasticsearchRestClientAutoConfiguration.class)
@ConditionalOnClass(RestClientBuilder.class)
public class ElasticSearchAutoConfiguration {

    @Bean
    public EsSQLCommandExecutor esSQLCommandExecutor(RestClientBuilder builder) {
        return SQLCommandExecutorFactory.create(ELASTICSEARCH_SQL_COMMAND_EXECUTOR_KEY, new Object[]{builder});
    }

}
