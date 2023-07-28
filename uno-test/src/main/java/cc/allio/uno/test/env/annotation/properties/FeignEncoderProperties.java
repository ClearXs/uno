package cc.allio.uno.test.env.annotation.properties;

import java.lang.annotation.*;

/**
 * {@link org.springframework.cloud.openfeign.support.FeignEncoderProperties}的注解描述
 *
 * @author jiangwei
 * @date 2023/3/9 12:40
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Properties("feign.encoder")
public @interface FeignEncoderProperties {

    /**
     * Indicates whether the charset should be derived from the Content-Type header.
     */
    boolean charsetFromContentType() default false;
}
