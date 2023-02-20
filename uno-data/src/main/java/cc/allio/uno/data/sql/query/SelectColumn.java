package cc.allio.uno.data.sql.query;

import cc.allio.uno.data.sql.Condition;
import cc.allio.uno.data.sql.RuntimeColumn;

/**
 * select column
 *
 * @author jiangwei
 * @date 2023/1/5 10:54
 * @since 1.1.4
 */
public class SelectColumn extends RuntimeColumn {

    public SelectColumn(String name, Object[] value, Condition condition) {
        super(name, value, condition);
    }
}
