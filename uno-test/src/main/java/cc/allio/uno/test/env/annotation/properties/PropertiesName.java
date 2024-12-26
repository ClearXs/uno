package cc.allio.uno.test.env.annotation.properties;

import java.lang.annotation.*;

/**
 * 标识properties的名称。与spring配置文件相对应。如：
 * <p>
 * mybatis.configLocation = @PropertiesName("mybatis.configLocation")
 * </p>
 *
 * @author j.x
 * @since 1.1.4
 * @see
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PropertiesName {

    /**
     * 配置名称
     *
     * @return
     */
    String value();
}
