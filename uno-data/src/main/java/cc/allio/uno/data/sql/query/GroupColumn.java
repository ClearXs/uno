package cc.allio.uno.data.sql.query;

import cc.allio.uno.data.sql.Condition;
import cc.allio.uno.data.sql.RuntimeColumn;

/**
 * Group Column
 *
 * @author jiangwei
 * @date 2023/1/5 10:53
 * @since 1.1.4
 */
public class GroupColumn extends RuntimeColumn {

    public GroupColumn(String name, Object[] value, Condition condition) {
        super(name, value, condition);
    }
}
