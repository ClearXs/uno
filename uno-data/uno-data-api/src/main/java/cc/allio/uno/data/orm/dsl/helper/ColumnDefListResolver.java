package cc.allio.uno.data.orm.dsl.helper;

import cc.allio.uno.data.orm.dsl.ColumnDef;

import java.util.List;

/**
 * 根据class解析出column的定义
 *
 * @author j.x
 * @date 2024/2/6 20:40
 * @since 1.1.7
 */
public interface ColumnDefListResolver {

    /**
     * 解析动作
     *
     * @param pojoClass pojoClass
     * @return column list
     */
    List<ColumnDef> resolve(Class<?> pojoClass);
}
