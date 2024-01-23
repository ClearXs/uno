package cc.allio.uno.data.orm.dsl.ddl;

import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.TableOperator;

/**
 * Drop table
 *
 * @author jiangwei
 * @date 2023/4/16 12:52
 * @since 1.1.4
 */
public interface DropTableOperator extends Operator<DropTableOperator>, TableOperator<DropTableOperator> {

    /**
     * Drop table if exist
     *
     * @param ifExist ifExist
     * @return SQLDropTableOperator
     */
    DropTableOperator ifExist(Boolean ifExist);
}
