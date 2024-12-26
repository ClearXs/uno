package cc.allio.uno.data.orm.dsl.sql.dml;

import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import cc.allio.uno.data.orm.dsl.TokenOperator;
import cc.allio.uno.data.orm.dsl.TokenOperatorAdapter;

/**
 * BinaryOperator
 *
 * @author j.x
 * @since 1.1.4
 */
public class DruidTokenOperatorAdapter implements TokenOperatorAdapter<SQLBinaryOperator> {
    private static final DruidTokenOperatorAdapter INSTANCE = new DruidTokenOperatorAdapter();

    public static DruidTokenOperatorAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public SQLBinaryOperator adapt(TokenOperator o) {
        for (SQLBinaryOperator operator : SQLBinaryOperator.values()) {
            if (operator.getName().equals(o.getName())) {
                return operator;
            }
        }
        return null;
    }

    @Override
    public TokenOperator reverse(SQLBinaryOperator binaryOperator) {
        for (TokenOperator value : TokenOperator.values()) {
            if (value.getName().equals(binaryOperator.name)) {
                return value;
            }
        }
        return null;
    }
}
