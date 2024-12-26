package cc.allio.uno.data.orm.dsl.ddl;

import cc.allio.uno.data.orm.dsl.opeartorgroup.Operators;
import cc.allio.uno.data.orm.dsl.PrepareOperator;
import cc.allio.uno.data.orm.dsl.TableOperator;

/**
 * Exist Operator
 *
 * @author j.x
 * @since 1.1.4
 * @see Operators
 */
public interface ExistTableOperator<T extends ExistTableOperator<T>> extends PrepareOperator<T>, TableOperator<T> {
}
