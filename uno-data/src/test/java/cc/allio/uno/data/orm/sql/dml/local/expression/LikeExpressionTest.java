package cc.allio.uno.data.orm.sql.dml.local.expression;

import cc.allio.uno.data.orm.sql.RuntimeColumn;
import cc.allio.uno.data.orm.sql.dml.local.WhereColumn;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class LikeExpressionTest extends BaseTestCase {

    @Test
    void test$Like() {
        RuntimeColumn runtimeColumn = new WhereColumn("userName", new Object[]{"test"}, null);
        LikeExpression expression = new LikeExpression(runtimeColumn, TestExpressionContext.INSTANCE, LikeExpression.Strategy.LEFT);
        assertEquals("user_name LIKE '%{{user_name_LIKE_0}}'", expression.getSQL());
    }

    @Test
    void testLike$() {
        RuntimeColumn runtimeColumn = new WhereColumn("userName", new Object[]{"test"}, null);
        LikeExpression expression = new LikeExpression(runtimeColumn, TestExpressionContext.INSTANCE, LikeExpression.Strategy.RIGHT);
        assertEquals("user_name LIKE '{{user_name_LIKE_0}}%'", expression.getSQL());
    }

    @Test
    void test$Like$() {
        RuntimeColumn runtimeColumn = new WhereColumn("userName", new Object[]{"test"}, null);
        LikeExpression expression = new LikeExpression(runtimeColumn, TestExpressionContext.INSTANCE, LikeExpression.Strategy.ALL);
        assertEquals("user_name LIKE '%{{user_name_LIKE_0}}%'", expression.getSQL());
    }
}
