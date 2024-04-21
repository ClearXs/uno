package cc.allio.uno.data.orm.dsl.dml;

import cc.allio.uno.data.orm.dsl.opeartorgroup.OperatorGroup;
import cc.allio.uno.data.orm.dsl.PrepareOperator;
import cc.allio.uno.data.orm.dsl.TableOperator;
import cc.allio.uno.data.orm.dsl.WhereOperator;

/**
 * DeleteOperator
 *
 * @author j.x
 * @date 2023/4/16 18:42
 * @see OperatorGroup
 * @since 1.1.4
 */
public interface DeleteOperator<T extends DeleteOperator<T>> extends PrepareOperator<T>, TableOperator<T>, WhereOperator<T> {
}
