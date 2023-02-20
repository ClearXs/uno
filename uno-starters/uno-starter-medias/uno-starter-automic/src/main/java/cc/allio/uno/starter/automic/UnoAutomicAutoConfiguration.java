package cc.allio.uno.starter.automic;

import cc.allio.uno.starter.automic.command.AutomicCommandController;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AutomicMediaProperties.class)
public class UnoAutomicAutoConfiguration {

    @Bean
    public AutomicCommandController automicCommandController(AutomicMediaProperties mediaProperties) {
        return new AutomicCommandController(mediaProperties);
    }
}
