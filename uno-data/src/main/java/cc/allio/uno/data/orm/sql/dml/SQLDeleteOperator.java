package cc.allio.uno.data.orm.sql.dml;

import cc.allio.uno.data.orm.sql.SQLPrepareOperator;
import cc.allio.uno.data.orm.sql.SQLTableOperator;
import cc.allio.uno.data.orm.sql.SQLWhereOperator;

/**
 * SQLDeleteOperator
 *
 * @author jiangwei
 * @date 2023/4/16 18:42
 * @since 1.1.4
 */
public interface SQLDeleteOperator extends
        SQLPrepareOperator<SQLDeleteOperator>, SQLTableOperator<SQLDeleteOperator>, SQLWhereOperator<SQLDeleteOperator> {
}
