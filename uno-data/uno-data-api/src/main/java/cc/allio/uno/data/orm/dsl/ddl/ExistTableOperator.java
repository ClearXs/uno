package cc.allio.uno.data.orm.dsl.ddl;

import cc.allio.uno.data.orm.dsl.PrepareOperator;
import cc.allio.uno.data.orm.dsl.TableOperator;

/**
 * SQL 查询是否存在指定表
 *
 * @author jiangwei
 * @date 2023/4/17 09:46
 * @since 1.1.4
 */
public interface ExistTableOperator extends PrepareOperator<ExistTableOperator>, TableOperator<ExistTableOperator> {

}
