package cc.allio.uno.data.orm.dsl.dml.sql;

import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import cc.allio.uno.data.orm.dsl.JoinType;
import cc.allio.uno.data.orm.dsl.JoinTypeAdapter;

/**
 * DruidJoinTypeAdapter
 *
 * @author jiangwei
 * @date 2023/4/13 13:25
 * @since 1.1.4
 */
public final class DruidJoinTypeAdapter implements JoinTypeAdapter<SQLJoinTableSource.JoinType> {
    private static final DruidJoinTypeAdapter INSTANCE = new DruidJoinTypeAdapter();

    public static DruidJoinTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public SQLJoinTableSource.JoinType adapt(JoinType o) {
        for (SQLJoinTableSource.JoinType joinType : SQLJoinTableSource.JoinType.values()) {
            if (joinType.name.equals(o.name)) {
                return joinType;
            }
        }
        return null;
    }

    @Override
    public JoinType reverse(SQLJoinTableSource.JoinType joinType) {
        for (JoinType value : JoinType.values()) {
            if (value.name.equals(joinType.name)) {
                return value;
            }
        }
        return null;
    }
}
