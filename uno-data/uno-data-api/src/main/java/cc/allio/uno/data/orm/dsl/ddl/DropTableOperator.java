package cc.allio.uno.data.orm.dsl.ddl;

import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.opeartorgroup.Operators;
import cc.allio.uno.data.orm.dsl.TableOperator;

/**
 * Drop Operator
 *
 * @author j.x
 * @see Operators
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
