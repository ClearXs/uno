package cc.allio.uno.test.env.annotation.properties;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * {@link org.springframework.boot.autoconfigure.data.redis.RedisProperties}的注解描述
 * <p>
 * 子注解：
 *     <ul>
 *         <li>{@link RedisSentinelProperties}</li>
 *     </ul>
 * </p>
 *
 * @author jiangwei
 * @date 2023/3/13 18:05
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Properties("spring.redis")
@RedisSentinelProperties
@RedisClusterProperties
@JedisPoolProperties
@LettucePoolProperties
@LettuceClusterRefreshProperties
@RedisLettuceProperties
public @interface RedisProperties {

    /**
     * Database index used by the connection factory.
     */
    int database() default 0;

    /**
     * Connection URL. Overrides host, port, and password. User is ignored. Example: redis://user:password@example.com:6379
     */
    String url() default "";

    /**
     * Redis server host.
     */
    String host() default "localhost";

    /**
     * Redis server port.
     */
    int port() default 6379;

    /**
     * Login password of the redis server.
     */
    String password() default "";

    /**
     * Whether to enable SSL support.
     */
    boolean ssl() default false;

    /**
     * Connection timeout.
     */
    long timeout() default 30000L;

    /**
     * Client name to be set on connections with CLIENT SETNAME.
     */
    String clientName() default "";

    @AliasFor(annotation = RedisSentinelProperties.class, attribute = "master")
    String sentinelMaster() default "";

    @AliasFor(annotation = RedisSentinelProperties.class, attribute = "nodes")
    String[] sentinelNodes() default {};

    @AliasFor(annotation = RedisSentinelProperties.class, attribute = "password")
    String sentinelPassword() default "";

    @AliasFor(annotation = RedisClusterProperties.class, attribute = "nodes")
    String[] clusterNodes() default {};

    @AliasFor(annotation = RedisClusterProperties.class, attribute = "maxRedirects")
    int clusterMaxRedirects() default 0;

    @AliasFor(annotation = JedisPoolProperties.class, attribute = "maxIdle")
    int jedisPollMaxIdle() default 8;

    @AliasFor(annotation = JedisPoolProperties.class, attribute = "minIdle")
    int jedisPollMinIdle() default 0;

    @AliasFor(annotation = JedisPoolProperties.class, attribute = "maxActive")
    int jedisPollMaxActive() default 8;

    @AliasFor(annotation = JedisPoolProperties.class, attribute = "maxWait")
    long jedisPollMaxWait() default -1L;

    @AliasFor(annotation = JedisPoolProperties.class, attribute = "timeBetweenEvictionRuns")
    long jedisPollTimeBetweenEvictionRuns() default 0L;


    @AliasFor(annotation = RedisLettuceProperties.class, attribute = "shutdownTimeout")
    long lettuceShutdownTimeout() default 100L;

    @AliasFor(annotation = LettucePoolProperties.class, attribute = "maxIdle")
    int lettucePollMaxIdle() default 8;

    @AliasFor(annotation = LettucePoolProperties.class, attribute = "minIdle")
    int lettucePollMinIdle() default 0;

    @AliasFor(annotation = LettucePoolProperties.class, attribute = "maxActive")
    int lettucePollMaxActive() default 8;

    @AliasFor(annotation = LettucePoolProperties.class, attribute = "maxWait")
    long lettucePollMaxWait() default -1L;

    @AliasFor(annotation = LettucePoolProperties.class, attribute = "timeBetweenEvictionRuns")
    long lettucePollTimeBetweenEvictionRuns() default 0L;

    @AliasFor(annotation = LettuceClusterRefreshProperties.class, attribute = "period")
    long lettuceClusterRefreshPeriod() default 0L;

    @AliasFor(annotation = LettuceClusterRefreshProperties.class, attribute = "adaptive")
    boolean lettuceClusterRefreshAdaptive() default false;

}

