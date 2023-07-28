package cc.allio.uno.data.orm.sql;

import java.util.Collection;

/**
 * 包含Column的语句
 *
 * @author jiangwei
 * @date 2023/1/9 15:45
 * @since 1.1.4
 * @deprecated 1.1.4版本删除
 */
@Deprecated
public interface ColumnStatement<T extends ColumnStatement<T>> extends Statement<T> {

    /**
     * 获取当前语句字段实例列表。
     *
     * @return Column列表
     * @see RuntimeColumn
     */
    Collection<RuntimeColumn> getColumns();
}
