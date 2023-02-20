package cc.allio.uno.starter.core;

import cc.allio.uno.core.bus.SubscriptionProperties;
import cc.allio.uno.core.util.CoreBeanUtil;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Uno Core自动配置类
 *
 * @author jiangwei
 * @date 2022/2/7 14:43
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(SubscriptionProperties.class)
public class UnoCoreAutoConfiguration {

    @Bean("unoBeanUtil")
    public CoreBeanUtil coreBeanUtil() {
        return new CoreBeanUtil();
    }
}
