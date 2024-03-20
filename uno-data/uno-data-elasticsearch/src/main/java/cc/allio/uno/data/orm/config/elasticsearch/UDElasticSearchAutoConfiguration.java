package cc.allio.uno.data.orm.config.elasticsearch;

import cc.allio.uno.data.orm.executor.ExecutorInitializerAutoConfiguration;
import cc.allio.uno.data.orm.executor.elasticsearch.EsCommandExecutorLoader;
import cc.allio.uno.data.orm.executor.elasticsearch.EsCommandExecutorProcessor;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.context.annotation.*;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(ExecutorInitializerAutoConfiguration.class)
@AutoConfigureAfter(ElasticsearchRestClientAutoConfiguration.class)
@ConditionalOnClass(RestClientBuilder.class)
public class UDElasticSearchAutoConfiguration {

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
}
