package cc.allio.uno.test.env.annotation.properties;

import java.lang.annotation.*;

/**
 * {@link  org.springframework.cloud.netflix.ribbon.ServerIntrospectorProperties}注解描述
 *
 * @author j.x
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Properties("ribbon")
public @interface ServerIntrospectorProperties {

    int[] securePorts() default {};
}
