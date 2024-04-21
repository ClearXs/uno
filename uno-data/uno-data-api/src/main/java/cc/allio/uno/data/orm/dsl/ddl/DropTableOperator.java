package cc.allio.uno.data.orm.dsl.ddl;

import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.opeartorgroup.OperatorGroup;
import cc.allio.uno.data.orm.dsl.TableOperator;

/**
 * Drop Operator
 *
 * @author j.x
 * @date 2023/4/16 12:52
 * @see OperatorGroup
 * @since 1.1.4
 */
public interface DropTableOperator<T extends DropTableOperator<T>> extends Operator<T>, TableOperator<T> {

    /**
     * Drop xxxx if exist
     *
     * @param ifExist ifExist
     * @return self
     */
    T ifExist(Boolean ifExist);
}
