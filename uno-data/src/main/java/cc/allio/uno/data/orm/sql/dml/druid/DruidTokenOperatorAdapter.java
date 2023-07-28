package cc.allio.uno.data.orm.sql.dml.druid;

import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import cc.allio.uno.data.orm.sql.TokenOperator;
import cc.allio.uno.data.orm.sql.TokenOperatorAdapter;

/**
 * BinaryOperator
 *
 * @author jiangwei
 * @date 2023/4/13 13:21
 * @since 1.1.4
 */
public class DruidTokenOperatorAdapter implements TokenOperatorAdapter<SQLBinaryOperator> {
    private static final DruidTokenOperatorAdapter INSTANCE = new DruidTokenOperatorAdapter();

    @Override
    public SQLBinaryOperator get(TokenOperator o) {
        for (SQLBinaryOperator operator : SQLBinaryOperator.values()) {
            if (operator.getName().equals(o.getName())) {
                return operator;
            }
        }
        return null;
    }

    @Override
    public TokenOperator reversal(SQLBinaryOperator sqlBinaryOperator) {
        for (TokenOperator value : TokenOperator.values()) {
            if (value.getName().equals(sqlBinaryOperator.name)) {
                return value;
            }
        }
        return null;
    }

    public static DruidTokenOperatorAdapter getInstance() {
        return INSTANCE;
    }
}
