package cc.allio.uno.test.env.annotation.properties;

import java.lang.annotation.*;

/**
 * {@link org.springframework.boot.autoconfigure.data.redis.RedisProperties.Sentinel}的注解描述
 *
 * @author j.x
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Properties("spring.redis.sentinel")
public @interface RedisSentinelProperties {

    /**
     * Name of the Redis server.
     */
    @Empty
    String master() default "";

    /**
     * Comma-separated list of "host:port" pairs.
     */
    @Empty
    String[] nodes() default {};

    /**
     * Password for authenticating with sentinel(s).
     */
    @Empty
    String password() default "";
}
