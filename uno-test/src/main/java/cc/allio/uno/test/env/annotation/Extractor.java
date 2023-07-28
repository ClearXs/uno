package cc.allio.uno.test.env.annotation;

import java.lang.annotation.*;

/**
 * 注解抽取器
 *
 * @author jiangwei
 * @date 2023/3/2 18:04
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
