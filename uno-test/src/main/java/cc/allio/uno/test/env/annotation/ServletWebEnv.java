package cc.allio.uno.test.env.annotation;

import cc.allio.uno.test.env.ServletWebEnvironment;
import cc.allio.uno.test.env.annotation.properties.ServerProperties;

import java.lang.annotation.*;

/**
 * 混合注解，构建servlet web环境。
 * <p>
 * 对应的注解配置：
 *     <ul>
 *         <li>{@link ServerProperties}</li>
 *     </ul>
 * </p>
 *
 * @author j.x
 * @date 2023/3/9 22:07
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AnnoConfigure
@Extractor(ServletWebConfigure.class)
@Env(ServletWebEnvironment.class)
public @interface ServletWebEnv {
}
