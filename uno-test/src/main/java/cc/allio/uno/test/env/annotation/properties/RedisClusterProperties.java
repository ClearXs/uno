package cc.allio.uno.test.env.annotation.properties;

import java.lang.annotation.*;

/**
 * {@link org.springframework.boot.autoconfigure.data.redis.RedisProperties.Cluster}的注解描述
 *
 * @author jiangwei
 * @date 2023/3/13 19:44
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Properties("spring.redis.cluster")
public @interface RedisClusterProperties {

    /**
     * Comma-separated list of "host:port" pairs to bootstrap from. This represents an "initial" list of cluster nodes and is required to have at least one entry.
     */
    @Empty
    String[] nodes() default {};

    /**
     * Maximum number of redirects to follow when executing commands across the cluster.
     */
    @Empty
    int maxRedirects() default 0;
}
