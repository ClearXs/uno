package cc.allio.uno.test.env.annotation;

import java.lang.annotation.*;

/**
 * 用于标识注解，表示当前注解为配置信息
 *
 * @author j.x
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AnnoConfigure {

}
