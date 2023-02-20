package cc.allio.uno.starter.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Reactive-Redis-Template工厂对象
 *
 * @author jiangwei
 * @date 2022/11/11 18:57
 * @since 1.0.8
 */
public class ReactiveRedisTemplateFactory {

    ReactiveRedisConnectionFactory connectionFactory;
    ObjectMapper mapper;

    public ReactiveRedisTemplateFactory(ObjectMapper objectMapper, ReactiveRedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        this.mapper = objectMapper;
    }

    /**
     * 根据指定类型创建通用ReactiveRedisTemplate
     *
     * @param generic 泛型class对象
     * @param <T>     泛型类型
     * @return ReactiveRedisTemplate对象实例
     */
    public <T> ReactiveRedisTemplate<String, T> genericTemplate(Class<T> generic) {
        RedisSerializationContext.RedisSerializationContextBuilder<String, T> builder =
                RedisSerializationContext.newSerializationContext();
        Jackson2JsonRedisSerializer<T> redisSerializer = new Jackson2JsonRedisSerializer<>(generic);
        redisSerializer.setObjectMapper(mapper);
        builder.key(new StringRedisSerializer());
        builder.value(redisSerializer);
        builder.hashKey(new StringRedisSerializer());
        builder.hashValue(redisSerializer);
        return new ReactiveRedisTemplate<>(connectionFactory, builder.build());
    }
}
