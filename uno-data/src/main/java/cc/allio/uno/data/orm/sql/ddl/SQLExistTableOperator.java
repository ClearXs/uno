package cc.allio.uno.data.orm.sql.ddl;

import cc.allio.uno.data.orm.sql.SQLPrepareOperator;
import cc.allio.uno.data.orm.sql.SQLTableOperator;

/**
 * SQL 查询是否存在指定表
 *
 * @author jiangwei
 * @date 2023/4/17 09:46
 * @since 1.1.4
 */
public interface SQLExistTableOperator extends SQLPrepareOperator<SQLExistTableOperator>, SQLTableOperator<SQLExistTableOperator> {

}
