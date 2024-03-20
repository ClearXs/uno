package cc.allio.uno.data.orm.dsl.dml;

import cc.allio.uno.data.orm.dsl.OperatorGroup;
import cc.allio.uno.data.orm.dsl.PrepareOperator;
import cc.allio.uno.data.orm.dsl.TableOperator;
import cc.allio.uno.data.orm.dsl.WhereOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;

/**
 * DeleteOperator
 *
 * @author j.x
 * @date 2023/4/16 18:42
 * @see OperatorGroup
 * @since 1.1.4
 */
public interface DeleteOperator extends
        PrepareOperator<DeleteOperator>, TableOperator<DeleteOperator>, WhereOperator<DeleteOperator> {
}
