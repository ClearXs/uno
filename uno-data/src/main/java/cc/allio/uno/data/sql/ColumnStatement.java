package cc.allio.uno.data.sql;

import java.util.Collection;

/**
 * 包含Column的语句
 *
 * @author jiangwei
 * @date 2023/1/9 15:45
 * @since 1.1.4
 */
public interface ColumnStatement<T extends ColumnStatement<T>> extends Statement<T> {

    /**
     * 获取当前语句字段实例列表。
     *
     * @return Column列表
     * @see RuntimeColumn
     */
    Collection<RuntimeColumn> getColumns();
}
