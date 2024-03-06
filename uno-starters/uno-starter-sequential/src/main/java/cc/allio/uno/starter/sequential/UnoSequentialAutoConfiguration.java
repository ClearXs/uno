package cc.allio.uno.starter.sequential;

import cc.allio.uno.core.bus.EventBusFactory;
import cc.allio.uno.core.type.TypeManager;
import cc.allio.uno.sequnetial.SubscriptionProperties;
import cc.allio.uno.sequnetial.bus.SequentialEventBus;
import cc.allio.uno.sequnetial.process.DefaultProcessor;
import cc.allio.uno.sequnetial.process.Processor;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(SubscriptionProperties.class)
public class UnoSequentialAutoConfiguration {

    @Bean
    public SequentialEventBus sequentialEventBus() {
        SequentialEventBus eventBus = new SequentialEventBus();
        EventBusFactory.reset(eventBus);
        return eventBus;
    }

    /**
     * 依赖于{@link TypeManager}。需要确保容器中存在{@link TypeManager}实例
     */
    @Bean
    @ConditionalOnBean({SequentialEventBus.class, TypeManager.class})
    public Processor springProcessor(SequentialEventBus eventBus, TypeManager typeManager) {
        return new DefaultProcessor(typeManager);
    }

}
