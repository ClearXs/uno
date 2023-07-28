package cc.allio.uno.test.env.annotation.properties;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 注解于某个注解之上。表示被注解为配置注解，把注解描述的配置放入spring环境中。
 * <p>
 *     注解的坏处在于：嵌入对象很难以描述。（所以在所有的配置注解缺少嵌入的描述）
 * </p>
 *
 * @author jiangwei
 * @date 2023/3/3 11:51
 * @see AnnoPropertiesParser
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Properties {

    @AliasFor("prefix")
    String value() default "";

    /**
     * 配置前缀
     */
    @AliasFor("value")
    String prefix() default "";
}
