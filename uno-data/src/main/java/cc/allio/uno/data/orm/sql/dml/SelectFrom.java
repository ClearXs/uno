package cc.allio.uno.data.orm.sql.dml;

import cc.allio.uno.data.orm.sql.From;
import cc.allio.uno.data.orm.sql.JoinType;

/**
 * from delegate
 *
 * @author jiangwei
 * @date 2023/1/11 09:50
 * @since 1.1.4
 * @deprecated 1.1.4版本删除
 */
@Deprecated
public interface SelectFrom<T extends SelectFrom<T>> extends From<T> {

    /**
     * FROM SUB_QUERY
     *
     * @return SubQueryFrom
     */
    SubQueryFrom subQuery();

    /**
     * JOIN
     *
     * @return JoinFrom
     */
    JoinFrom join(JoinType joinType);

    default JoinFrom leftJoin() {
        return join(JoinType.LEFT_OUTER_JOIN);
    }

    default JoinFrom rightJoin() {
        return join(JoinType.RIGHT_OUTER_JOIN);
    }
}
