package cc.allio.uno.data.orm.dsl.helper;

import cc.allio.uno.data.orm.dsl.ColumnDef;

import java.lang.reflect.Field;

/**
 * 根据指定的{@link java.lang.reflect.Field}解析出{@link cc.allio.uno.data.orm.dsl.ColumnDef}
 *
 * @author jiangwei
 * @date 2024/2/6 20:08
 * @see ColumnDefResolve
 * @since 1.1.6
 */
public interface ColumnDefResolver {

    /**
     * 执行解析动作
     *
     * @param field field
     * @return ColumnDef instance or null
     */
    ColumnDef resolve(Field field);
}
