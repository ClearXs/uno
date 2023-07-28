package cc.allio.uno.data.orm.sql.ddl;

import cc.allio.uno.data.orm.sql.SQLOperator;
import cc.allio.uno.data.orm.sql.SQLTableOperator;

/**
 * Drop table
 *
 * @author jiangwei
 * @date 2023/4/16 12:52
 * @since 1.1.4
 */
public interface SQLDropTableOperator extends SQLOperator<SQLDropTableOperator>, SQLTableOperator<SQLDropTableOperator> {

    /**
     * Drop table if exist
     *
     * @param ifExist ifExist
     * @return SQLDropTableOperator
     */
    SQLDropTableOperator ifExist(Boolean ifExist);
}
