package cc.allio.uno.data.sql.query;

import cc.allio.uno.data.sql.Condition;
import cc.allio.uno.data.sql.RuntimeColumn;

/**
 * Where Column
 *
 * @author jiangwei
 * @date 2023/1/5 18:52
 * @since 1.1.4
 */
public class WhereColumn extends RuntimeColumn {

    public WhereColumn(String name, Object[] value, Condition condition) {
        super(name, value, condition);
    }
}
