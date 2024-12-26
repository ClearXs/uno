package cc.allio.uno.test.env.annotation;

import cc.allio.uno.test.env.Environment;

import java.lang.annotation.*;

/**
 * 标识为注解上，指定使用的环境
 *
 * @author j.x
 * @since 1.1.4
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Env {

    /**
     * 提供构建环境的class对象
     *
     * @return Environment env
     */
    Class<? extends Environment>[] value();

}
