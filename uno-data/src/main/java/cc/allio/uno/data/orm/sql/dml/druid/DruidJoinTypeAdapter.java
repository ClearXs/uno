package cc.allio.uno.data.orm.sql.dml.druid;

import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import cc.allio.uno.data.orm.sql.JoinType;
import cc.allio.uno.data.orm.sql.JoinTypeAdapter;

/**
 * DruidJoinTypeAdapter
 *
 * @author jiangwei
 * @date 2023/4/13 13:25
 * @since 1.1.4
 */
public class DruidJoinTypeAdapter implements JoinTypeAdapter<SQLJoinTableSource.JoinType> {
    private static final DruidJoinTypeAdapter INSTANCE = new DruidJoinTypeAdapter();

    @Override
    public SQLJoinTableSource.JoinType get(JoinType o) {
        for (SQLJoinTableSource.JoinType joinType : SQLJoinTableSource.JoinType.values()) {
            if (joinType.name.equals(o.name)) {
                return joinType;
            }
        }
        return null;
    }

    @Override
    public JoinType reversal(SQLJoinTableSource.JoinType joinType) {
        for (JoinType value : JoinType.values()) {
            if (value.name.equals(joinType.name)) {
                return value;
            }
        }
        return null;
    }

    public static DruidJoinTypeAdapter getInstance() {
        return INSTANCE;
    }

}
