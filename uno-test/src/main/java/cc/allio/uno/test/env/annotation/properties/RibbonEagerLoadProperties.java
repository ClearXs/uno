package cc.allio.uno.test.env.annotation.properties;

import java.lang.annotation.*;

/**
 * {@link org.springframework.cloud.netflix.ribbon.RibbonEagerLoadProperties}注解描述
 *
 * @author j.x
 * @date 2023/3/9 15:36
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Properties("ribbon.eager-load")
public @interface RibbonEagerLoadProperties {

    boolean enabled() default false;

    String[] clients() default {};
}
