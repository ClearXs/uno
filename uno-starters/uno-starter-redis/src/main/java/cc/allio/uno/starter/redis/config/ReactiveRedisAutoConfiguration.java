package cc.allio.uno.starter.redis.config;

import cc.allio.uno.starter.redis.ReactiveRedisTemplateFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;

@Configuration(proxyBeanMethods = false)
@EnableAutoConfiguration
@AutoConfigureAfter({RedisAutoConfiguration.class, RedisReactiveAutoConfiguration.class})
public class ReactiveRedisAutoConfiguration {

    @Bean
    public ReactiveRedisTemplateFactory reactiveRedisTemplateFactory(ObjectMapper objectMapper, ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        return new ReactiveRedisTemplateFactory(objectMapper, reactiveRedisConnectionFactory);
    }

}
