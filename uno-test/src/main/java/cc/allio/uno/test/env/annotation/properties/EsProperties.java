package cc.allio.uno.test.env.annotation.properties;

import java.lang.annotation.*;

/**
 * es 配置
 *
 * @author j.x
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Properties("spring.elasticsearch.rest")
public @interface EsProperties {

    /**
     * Comma-separated list of the Elasticsearch instances to use.
     */
    String[] uris() default {"http://localhost:9200"};

    /**
     * Credentials username.
     */
    String username() default "";

    /**
     * Credentials password.
     */
    String password() default "";
}
