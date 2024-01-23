package cc.allio.uno.data.orm.dsl.ddl;

import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.TableOperator;

/**
 * SQL修改表字段操作
 *
 * @author jiangwei
 * @date 2023/6/8 19:21
 * @since 1.1.4
 */
public interface AlterTableOperator extends Operator<AlterTableOperator>, TableOperator<AlterTableOperator> {
}
