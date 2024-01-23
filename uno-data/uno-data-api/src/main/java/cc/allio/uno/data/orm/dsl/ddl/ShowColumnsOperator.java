package cc.allio.uno.data.orm.dsl.ddl;

import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.PrepareOperator;
import cc.allio.uno.data.orm.dsl.TableOperator;

/**
 * SQL表结构查询
 *
 * @author jiangwei
 * @date 2023/6/8 19:19
 * @since SWP-2.0.1
 */
public interface ShowColumnsOperator extends PrepareOperator<ShowColumnsOperator>, TableOperator<ShowColumnsOperator> {

    /**
     * 转换为{@link QueryOperator}
     *
     * @return SQLQueryOperator for instance
     */
    QueryOperator toQueryOperator();
}
