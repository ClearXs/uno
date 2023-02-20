package cc.allio.uno.data.sql.expression;

import cc.allio.uno.data.orm.dialect.func.Func;
import cc.allio.uno.data.sql.Alias;
import cc.allio.uno.data.sql.query.SelectColumn;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class FunctionExpressionTest extends BaseTestCase {
    SelectColumn column = new SelectColumn("user_name", null, null);

    @Test
    void testMin() {
        FunctionExpression expression = new FunctionExpression(TestExpressionContext.INSTANCE, Func.MIN_FUNCTION, new Object[]{column});
        assertEquals("min(user_name)", expression.getSQL());
    }

    @Test
    void testMax() {
        FunctionExpression expression = new FunctionExpression(TestExpressionContext.INSTANCE, Func.MAX_FUNCTION, new Object[]{column});
        assertEquals("max(user_name)", expression.getSQL());
    }

    @Test
    void testCount() {
        FunctionExpression expression = new FunctionExpression(TestExpressionContext.INSTANCE, Func.COUNT_FUNCTION, new Object[]{column});
        assertEquals("count(user_name)", expression.getSQL());
    }

    @Test
    void testCountAlias() {
        FunctionExpression expression = new FunctionExpression(TestExpressionContext.INSTANCE, Func.COUNT_FUNCTION, new Object[]{column, new Alias("count")});
        assertEquals("count(user_name) count", expression.getSQL());
    }
}
