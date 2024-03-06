package cc.allio.uno.data.orm.dsl.helper;

import java.lang.annotation.*;

/**
 * 加在pojo上，由它自身提供column的定义
 *
 * @author jiangwei
 * @date 2024/2/6 20:39
 * @since 1.1.7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ColumnDefListResolve {

    Class<? extends ColumnDefListResolver> value();
}
