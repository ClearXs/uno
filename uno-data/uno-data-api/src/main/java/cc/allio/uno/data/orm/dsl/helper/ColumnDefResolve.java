package cc.allio.uno.data.orm.dsl.helper;

import java.lang.annotation.*;

/**
 * 用于在{@link PojoWrapper#getColumnDefs()}进行使用
 *
 * @author j.x
 * @date 2024/2/6 20:07
 * @since 1.1.7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnDefResolve {

    Class<? extends ColumnDefResolver> value();
}
