package cc.allio.uno.test.env.annotation.properties;

import org.springframework.http.HttpMethod;
import reactor.util.retry.RetryBackoffSpec;

import java.lang.annotation.*;
import java.time.Duration;

/**
 * {@link org.springframework.cloud.client.loadbalancer.LoadBalancerRetryProperties}的注解描述
 *
 * @author jiangwei
 * @date 2023/3/9 12:42
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Properties("spring.cloud.loadbalancer.retry")
public @interface LoadBalancerRetryProperties {

    boolean enabled() default true;

    /**
     * Indicates retries should be attempted on operations other than
     * {@link HttpMethod#GET}.
     */
    boolean retryOnAllOperations() default false;

    /**
     * Number of retries to be executed on the same ServiceInstance.     * @return
     */
    int maxRetriesOnSameServiceInstance() default 0;

    /**
     * Number of retries to be executed on the next ServiceInstance. A ServiceInstance is chosen before each retry call.
     */
    int maxRetriesOnNextServiceInstance() default 1;

    /**
     * A Array of status codes that should trigger a retry.
     */
    int[] retryableStatusCodes() default {};

    /**
     * Properties for Reactor Retry backoffs in Spring Cloud LoadBalancer.
     */
    Backoff backoff();

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Properties
    @interface Backoff {

        /**
         * Indicates whether Reactor Retry backoffs should be applied.
         */
        boolean enable() default false;

        /**
         * Used to setValue {@link RetryBackoffSpec#minBackoff}.
         */
        @PropertiesType(Duration.class)
        long minBackoff() default 5;

        /**
         * Used to setValue {@link RetryBackoffSpec#maxBackoff}.
         */
        @PropertiesType(Duration.class)
        long maxBackoff() default Long.MAX_VALUE;

        /**
         * Used to setValue {@link RetryBackoffSpec#jitter}.
         */
        double jitter() default 0.5d;
    }
}
