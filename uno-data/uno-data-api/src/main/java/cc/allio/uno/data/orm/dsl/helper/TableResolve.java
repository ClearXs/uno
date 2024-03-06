package cc.allio.uno.data.orm.dsl.helper;

import java.lang.annotation.*;

/**
 * 在{@link PojoWrapper#findTable(Class)}时使用
 *
 * @author jiangwei
 * @date 2024/2/6 20:06
 * @since 1.1.7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TableResolve {

    Class<? extends TableResolver> value();
}
