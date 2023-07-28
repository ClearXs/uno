package cc.allio.uno.data.orm.sql.dml.local.expression;

import cc.allio.uno.data.orm.sql.RuntimeColumn;
import cc.allio.uno.data.orm.sql.dml.local.WhereColumn;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class LTExpressionTest extends BaseTestCase {

    @Test
    void testLT() {
        RuntimeColumn runtimeColumn = new WhereColumn("userName", new Object[]{"test"}, null);
        LTExpression expression = new LTExpression(runtimeColumn, TestExpressionContext.INSTANCE);
        assertEquals("user_name < {{user_name_lt_0}}", expression.getSQL());
    }
}
