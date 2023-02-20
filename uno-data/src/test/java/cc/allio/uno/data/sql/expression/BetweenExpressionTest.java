package cc.allio.uno.data.sql.expression;

import cc.allio.uno.data.sql.query.WhereColumn;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class BetweenExpressionTest extends BaseTestCase {

    @Test
    void testSingleValue() {
        WhereColumn whereColumn = new WhereColumn("userName", new Object[]{"1"}, null);
        BetweenExpression expression = new BetweenExpression(whereColumn, TestExpressionContext.INSTANCE, BetweenExpression.Strategy.IN);
        String sql = expression.getSQL();
        assertEquals("user_name BETWEEN %s AND %s", sql);
    }

    @Test
    void testBetween() {
        WhereColumn whereColumn = new WhereColumn("userName", new Object[]{"1", 2}, null);
        BetweenExpression expression = new BetweenExpression(whereColumn, TestExpressionContext.INSTANCE, BetweenExpression.Strategy.IN);
        String sql = expression.getSQL();
        assertEquals("user_name BETWEEN {{user_name_bt0}} AND {{user_name_bt1}}", sql);
    }

    @Test
    void testNotBetween() {
        WhereColumn whereColumn = new WhereColumn("userName", new Object[]{"1", 2}, null);
        BetweenExpression expression = new BetweenExpression(whereColumn, TestExpressionContext.INSTANCE, BetweenExpression.Strategy.NOT);
        String sql = expression.getSQL();
        assertEquals("user_name NOT BETWEEN {{user_name_bt0}} AND {{user_name_bt1}}", sql);
    }
}
