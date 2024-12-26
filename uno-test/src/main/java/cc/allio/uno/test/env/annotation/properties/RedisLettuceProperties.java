package cc.allio.uno.test.env.annotation.properties;

import java.lang.annotation.*;

/**
 * {@link org.springframework.boot.autoconfigure.data.redis.RedisProperties.Pool}的注解描述
 *
 * @author j.x
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Properties("spring.redis.lettuce")
public @interface RedisLettuceProperties {

    /**
     * Shutdown timeout.
     */
    @Empty
    long shutdownTimeout() default 100L;


}
