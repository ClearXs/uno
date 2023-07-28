package cc.allio.uno.data.orm.sql.dml.local.expression;

import cc.allio.uno.core.StringPool;

/**
 * distinct expression
 *
 * @author jiangwei
 * @date 2023/1/10 13:20
 * @since 1.1.4
 */
public class DistinctExpression implements SingleExpression {

    // constant
    public static final String DISTINCT = "DISTINCT";
    public static final String DISTINCT_ON = "DISTINCT ON";

    // ON Field
    private final PlainExpression onExpression;
    private final String alias;

    public DistinctExpression(PlainExpression onExpression, String alias) {
        this.onExpression = onExpression;
        this.alias = alias;
    }

    @Override
    public String getSQL() {
        // DISTINCT or DISTINCT ON (alias) alias
        return onExpression == null ?
                DISTINCT :
                DISTINCT_ON + StringPool.SPACE + StringPool.LEFT_BRACKET + onExpression.column + StringPool.RIGHT_BRACKET + StringPool.SPACE + alias;
    }
}
