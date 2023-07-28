package cc.allio.uno.data.orm.sql.dml;

import cc.allio.uno.data.orm.sql.SQLAssociation;

/**
 * Query 集联的动作定义
 *
 * @author jiangwei
 * @date 2022/9/30 13:51
 * @since 1.1.0
 * @deprecated 1.1.4版本删除
 */
@Deprecated
public interface QuerySQLAssociation extends SQLAssociation {

    /**
     * select 集联操作
     *
     * @return select实例
     */
    SelectDelegate thenSelect();

    /**
     * from 级联操作
     *
     * @return from实例
     */
    SelectFrom thenFrom();

    /**
     * where 集联操作
     *
     * @return where实例
     */
    WhereDelegate thenWhere();

    /**
     * order 集联操作
     *
     * @return order实例
     */
    OrderDelegate thenOrder();

    /**
     * group 集联操作
     *
     * @return group实例
     */
    GroupDelegate thenGroup();

    /**
     * limit 集联操作
     *
     * @return limit实例
     */
    LimitDelegate thenLimit();
}
