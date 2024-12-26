package cc.allio.uno.test.env.annotation;

import java.lang.annotation.*;

/**
 * 注解抽取器
 *
 * @author j.x
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Extractor {

    /**
     * 值数据
     *
     * @return Class
     */
    Class<? extends EnvConfigure> value();
}
