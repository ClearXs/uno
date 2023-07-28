package cc.allio.uno.data.orm.sql.ddl;

import cc.allio.uno.data.orm.sql.dml.SQLQueryOperator;
import cc.allio.uno.data.orm.sql.SQLPrepareOperator;
import cc.allio.uno.data.orm.sql.SQLTableOperator;

/**
 * SQL表结构查询
 *
 * @author jiangwei
 * @date 2023/6/8 19:19
 * @since SWP-2.0.1
 */
public interface SQLShowColumnsOperator extends SQLPrepareOperator<SQLShowColumnsOperator>, SQLTableOperator<SQLShowColumnsOperator> {

    /**
     * 转换为{@link SQLQueryOperator}
     *
     * @return SQLQueryOperator for instance
     */
    SQLQueryOperator toQueryOperator();
}
