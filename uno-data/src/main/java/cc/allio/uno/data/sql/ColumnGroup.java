package cc.allio.uno.data.sql;

import java.util.LinkedHashSet;

/**
 * 字段{@link RuntimeColumn}组存储
 *
 * @author jiangwei
 * @date 2023/1/6 09:12
 * @since 1.1.4
 */
public class ColumnGroup extends LinkedHashSet<RuntimeColumn> {

    /**
     * 返回下一个{@link RuntimeColumn}实例
     *
     * @return RuntimeColumn
     */
    public RuntimeColumn next() {
        return iterator().next();
    }
}
