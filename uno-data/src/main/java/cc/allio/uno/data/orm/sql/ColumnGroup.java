package cc.allio.uno.data.orm.sql;

import java.util.LinkedHashSet;

/**
 * 字段{@link RuntimeColumn}组存储
 *
 * @author jiangwei
 * @date 2023/1/6 09:12
 * @since 1.1.4
 * @deprecated 1.1.4版本删除
 */
@Deprecated
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
