package cc.allio.uno.stater.srs;

import cc.allio.uno.component.media.command.CommandController;
import cc.allio.uno.stater.srs.command.SrsCommandController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * uno-srs自动配置类
 *
 * @author jiangwei
 * @date 2022/3/30 16:10
 * @since 1.0.6
 */
@Configuration
@EnableConfigurationProperties(SrsMediaProperties.class)
public class UnoSrsAutoConfiguration {

    @Bean
    public CommandController srsCommandController(SrsMediaProperties mediaProperties) {
        return new SrsCommandController(mediaProperties);
    }

    @Bean
    @ConditionalOnProperty(value = "automic.uno.media.endpoint", havingValue = "true", matchIfMissing = true)
    public UnoSrsEndpoint endpoint() {
        return new UnoSrsEndpoint();
    }

}
