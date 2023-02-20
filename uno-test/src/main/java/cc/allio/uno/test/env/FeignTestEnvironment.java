package cc.allio.uno.test.env;

import cc.allio.uno.test.BaseCoreTest;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import reactivefeign.spring.config.ReactiveFeignAutoConfiguration;
import reactivefeign.spring.config.ReactiveFeignClientsConfiguration;

/**
 * Feign测试环境
 *
 * @author jiangwei
 * @date 2022/10/29 09:56
 * @since 1.1.0
 */
public class FeignTestEnvironment implements TestSpringEnvironment {

    @Override
    public void support(BaseCoreTest test) {
        test.registerComponent(
                LoadBalancerAutoConfiguration.class,
                RibbonAutoConfiguration.class,
                FeignRibbonClientAutoConfiguration.class,
                FeignAutoConfiguration.class,
                // reactive configuration
                ReactiveFeignAutoConfiguration.class,
                ReactiveFeignClientsConfiguration.class,
                // Webclient$Builder
                WebClientAutoConfiguration.class);
    }
}
