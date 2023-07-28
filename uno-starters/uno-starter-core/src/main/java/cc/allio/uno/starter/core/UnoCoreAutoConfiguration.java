package cc.allio.uno.starter.core;

import cc.allio.uno.core.env.Env;
import cc.allio.uno.core.env.Envs;
import cc.allio.uno.core.env.SpringEnv;
import cc.allio.uno.core.env.SystemEnv;
import cc.allio.uno.core.util.CoreBeanUtil;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Uno Core自动配置类
 *
 * @author jiangwei
 * @date 2022/2/7 14:43
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class UnoCoreAutoConfiguration implements ApplicationContextAware {

    @Bean("unoBeanUtil")
    public CoreBeanUtil coreBeanUtil() {
        return new CoreBeanUtil();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ConfigurableEnvironment environment = (ConfigurableEnvironment) applicationContext.getEnvironment();
        Env currentEnv = Envs.getCurrentEnv();
        if (currentEnv instanceof SystemEnv) {
            Envs.reset(new SpringEnv((SystemEnv) currentEnv, environment));
        }
    }
}
