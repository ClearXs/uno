package cc.allio.uno.starter.sequential;

import cc.allio.uno.component.sequential.bus.SequentialMessageBus;
import cc.allio.uno.component.sequential.process.SpringProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class UnoSequentialAutoConfiguration {

    @Bean
    public SpringProcessor springProcessor() {
        return new SpringProcessor();
    }

    @Bean
    public SequentialMessageBus sequentialMessageBus() {
        return new SequentialMessageBus();
    }

}
