package cc.allio.uno.test.env.annotation.properties;

import java.lang.annotation.*;

/**
 * 标识properties的真实类型
 *
 * @author j.x
 * @date 2023/3/9 12:53
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PropertiesType {

    Class<?> value();
}
