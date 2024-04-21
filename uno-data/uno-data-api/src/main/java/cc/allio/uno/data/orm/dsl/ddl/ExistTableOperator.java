package cc.allio.uno.data.orm.dsl.ddl;

import cc.allio.uno.data.orm.dsl.opeartorgroup.OperatorGroup;
import cc.allio.uno.data.orm.dsl.PrepareOperator;
import cc.allio.uno.data.orm.dsl.TableOperator;

/**
 * Exist Operator
 *
 * @author j.x
 * @date 2023/4/17 09:46
 * @since 1.1.4
 * @see OperatorGroup
 */
public interface ExistTableOperator<T extends ExistTableOperator<T>> extends PrepareOperator<T>, TableOperator<T> {
}
