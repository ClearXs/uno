package cc.allio.uno.starter.kafka;

import cc.allio.uno.kafka.UnoKafkaManagement;
import cc.allio.uno.kafka.UnoKafkaProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Uno Kafka自动配置类
 *
 * @author j.x
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(UnoKafkaProperties.class)
public class UnoKafkaAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "cc.uno.kafka", value = "enable", havingValue = "true")
    public UnoKafkaManagement unoKafkaManagement(UnoKafkaProperties kafkaProperties) {
        return new UnoKafkaManagement(kafkaProperties);
    }
}
