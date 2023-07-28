package cc.allio.uno.test.env.annotation.properties;

import java.lang.annotation.*;

/**
 * {@link org.springframework.cloud.openfeign.FeignClientProperties}的注解描述
 *
 * @author jiangwei
 * @date 2023/3/9 12:08
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Properties("feign.client")
public @interface FeignClientProperties {

    boolean defaultToProperties() default true;

    String defaultConfig() default "default";

    boolean decodeSlash() default true;

}
