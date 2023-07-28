package cc.allio.uno.data.orm.sql.dml.local;

import cc.allio.uno.data.orm.sql.Condition;
import cc.allio.uno.data.orm.sql.RuntimeColumn;

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
