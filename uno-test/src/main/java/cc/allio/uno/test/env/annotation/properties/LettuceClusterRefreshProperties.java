
package cc.allio.uno.test.env.annotation.properties;

import java.lang.annotation.*;

/**
 * {@link org.springframework.boot.autoconfigure.data.redis.RedisProperties.Pool}的注解描述
 *
 * @author j.x
 * @date 2023/3/13 19:44
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Properties("spring.redis.lettuce.cluster.refresh")
public @interface LettuceClusterRefreshProperties {

    /**
     * Cluster topology refresh period.
     */
    @Empty
    long period() default 0L;

    /**
     * Whether adaptive topology refreshing using all available refresh
     * triggers should be used.
     */
    @Empty
    boolean adaptive() default false;


}
