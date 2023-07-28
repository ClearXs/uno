package cc.allio.uno.data.orm.sql.dml.local;

import cc.allio.uno.data.orm.sql.Condition;
import cc.allio.uno.data.orm.sql.RuntimeColumn;

/**
 * order column
 *
 * @author jiangwei
 * @date 2023/1/5 10:54
 * @since 1.1.4
 */
public class OrderColumn extends RuntimeColumn {

    public OrderColumn(String name, Object[] value, Condition condition) {
        super(name, value, condition);
    }
}
