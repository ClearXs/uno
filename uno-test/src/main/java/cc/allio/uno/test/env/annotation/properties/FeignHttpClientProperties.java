package cc.allio.uno.test.env.annotation.properties;

import java.lang.annotation.*;

import static org.springframework.cloud.openfeign.support.FeignHttpClientProperties.*;

/**
 * {@link org.springframework.cloud.openfeign.support.FeignHttpClientProperties}的注解描述
 *
 * @author jiangwei
 * @date 2023/3/9 12:10
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Properties("feign.httpclient")
public @interface FeignHttpClientProperties {

    boolean disableSslValidation() default DEFAULT_DISABLE_SSL_VALIDATION;

    int maxConnections() default DEFAULT_MAX_CONNECTIONS;

    int maxConnectionsPerRoute() default DEFAULT_MAX_CONNECTIONS_PER_ROUTE;

    long timeToLive() default DEFAULT_TIME_TO_LIVE;

    boolean followRedirects() default DEFAULT_FOLLOW_REDIRECTS;

    int connectionTimeout() default DEFAULT_CONNECTION_TIMEOUT;

    /**
     * Apache HttpClient5 additional properties.
     */
    Hc5Properties hc5();

    /**
     * {@link org.springframework.cloud.openfeign.support.FeignHttpClientProperties.Hc5Properties}注解描述
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Properties
    @interface Hc5Properties {

        /**
         * Pool concurrency policies.
         */
        org.springframework.cloud.openfeign.support.FeignHttpClientProperties.Hc5Properties.PoolConcurrencyPolicy poolConcurrencyPolicy() default org.springframework.cloud.openfeign.support.FeignHttpClientProperties.Hc5Properties.PoolConcurrencyPolicy.STRICT;

        /**
         * Pool connection re-use policies.
         */
        org.springframework.cloud.openfeign.support.FeignHttpClientProperties.Hc5Properties.PoolReusePolicy poolReusePolicy() default org.springframework.cloud.openfeign.support.FeignHttpClientProperties.Hc5Properties.PoolReusePolicy.FIFO;

        /**
         * Default value for socket timeout.
         */
        int socketTimeout() default 5;

    }
}
