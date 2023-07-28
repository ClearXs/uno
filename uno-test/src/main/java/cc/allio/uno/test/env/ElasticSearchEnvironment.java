package cc.allio.uno.test.env;

import cc.allio.uno.test.env.annotation.properties.EsProperties;
import cc.allio.uno.test.CoreTest;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;

import java.lang.annotation.Annotation;

/**
 * 配置的es环境
 *
 * @author jiangwei
 * @date 2023/7/5 16:03
 * @since 1.1.4
 */
public class ElasticSearchEnvironment extends VisitorEnvironment {
    @Override
    public Class<? extends Annotation>[] getPropertiesAnnotation() {
        return new Class[]{EsProperties.class};
    }

    @Override
    protected void onSupport(CoreTest coreTest) throws Throwable {
        coreTest.registerAutoConfiguration(ElasticsearchRestClientAutoConfiguration.class);
    }
}
