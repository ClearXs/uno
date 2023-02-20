package cc.allio.uno.data.sql.expression;

import cc.allio.uno.data.sql.RuntimeColumn;
import cc.allio.uno.data.sql.query.WhereColumn;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class EQExpressionTest extends BaseTestCase {

    @Test
    void testEQ() {
        RuntimeColumn runtimeColumn = new WhereColumn("userName", new Object[]{"test"}, null);
        EQExpression expression = new EQExpression(runtimeColumn, TestExpressionContext.INSTANCE);
        assertEquals("user_name = {{user_name_eq_0}}", expression.getSQL());
    }
}
