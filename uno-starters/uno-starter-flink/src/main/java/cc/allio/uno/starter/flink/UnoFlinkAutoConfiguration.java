package cc.allio.uno.starter.flink;

import cc.allio.uno.component.flink.UnoFlinkProperties;
import cc.allio.uno.component.flink.task.FlinkTaskBuilder;
import cc.allio.uno.component.flink.task.OrdinaryFlinkTask;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * flink自动配置类
 *
 * @author jiangwei
 * @date 2022/2/27 17:52
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(UnoFlinkProperties.class)
public class UnoFlinkAutoConfiguration {

    @Bean
    public <IN, OUT> OrdinaryFlinkTask<IN, OUT> ordinaryFlinkTask() {
        return FlinkTaskBuilder
                .<IN, OUT>shelves()
                .buildName("Flink-singleton-ordinary-task")
                .buildOrdinary();
    }
}
