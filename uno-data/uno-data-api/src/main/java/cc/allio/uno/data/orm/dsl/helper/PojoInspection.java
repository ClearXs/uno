package cc.allio.uno.data.orm.dsl.helper;

import java.lang.annotation.*;

/**
 * 加在实体类，决定该pojo如何进行检查
 *
 * @author j.x
 * @date 2024/2/6 20:39
 * @since 1.1.7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PojoInspection {

    Class<? extends PojoInspect> value();
}
