package cc.allio.uno.data.orm.sql;

import cc.allio.uno.data.orm.sql.dml.local.OrderCondition;

/**
 * SQL条件，如'OR'、'WHERE'...
 *
 * @author jiangwei
 * @date 2023/1/5 10:47
 * @see OrderCondition
 * @since 1.1.4
 * @deprecated 1.1.4版本删除
 */
@Deprecated
public interface Condition {

    /**
     * 获取条件名称
     *
     * @return 条件名称
     */
    String getName();
}
