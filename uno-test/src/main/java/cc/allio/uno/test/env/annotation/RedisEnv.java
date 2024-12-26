package cc.allio.uno.test.env.annotation;

import cc.allio.uno.test.env.annotation.properties.RedisProperties;
import cc.allio.uno.test.env.RedisEnvironment;

import java.lang.annotation.*;

/**
 * 混合注解，构建Redis运行环境。
 * <p>
 * 对应的注解配置为：
 *     <ul>
 *         <li>{@link RedisProperties}</li>
 *     </ul>
 * </p>
 *
 * @author j.x
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AnnoConfigure
@Env({RedisEnvironment.class})
public @interface RedisEnv {
}
