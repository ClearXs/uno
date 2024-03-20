package cc.allio.uno.data.orm.dsl.helper;

import cc.allio.uno.data.orm.dsl.Table;

/**
 * 根据指定的实体类，解析出表名
 *
 * @author j.x
 * @date 2024/2/6 20:05
 * @see TableResolve
 * @since 1.1.7
 */
public interface TableResolver {

    /**
     * 执行解析动作
     *
     * @param pojoClass pojoClass
     * @return table or null
     */
    Table resolve(Class<?> pojoClass);
}
