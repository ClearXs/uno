package cc.allio.uno.test.env;

import cc.allio.uno.test.env.annotation.properties.*;
import cc.allio.uno.test.CoreTest;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.cloud.loadbalancer.config.BlockingLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.loadbalancer.config.LoadBalancerCacheAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;

import java.lang.annotation.Annotation;

/**
 * Feign测试环境
 *
 * @author jiangwei
 * @date 2022/10/29 09:56
 * @since 1.1.0
 */
public class FeignEnvironment extends VisitorEnvironment {

    @Override
    protected void onSupport(CoreTest coreTest) throws Throwable {
        coreTest.registerAutoConfiguration(
                JacksonAutoConfiguration.class,
                LoadBalancerAutoConfiguration.class,
                org.springframework.cloud.loadbalancer.config.LoadBalancerAutoConfiguration.class,
                BlockingLoadBalancerClientAutoConfiguration.class,
                LoadBalancerCacheAutoConfiguration.class,
                LoadBalancerClientConfiguration.class,
                RestTemplateAutoConfiguration.class,
                RibbonAutoConfiguration.class,
                FeignRibbonClientAutoConfiguration.class,
                FeignAutoConfiguration.class);
    }

    @Override
    public Class<? extends Annotation>[] getPropertiesAnnotation() {
        return new Class[]{FeignClientProperties.class, FeignHttpClientProperties.class, FeignEncoderProperties.class,
                RibbonEagerLoadProperties.class, ServerIntrospectorProperties.class};
    }

}
