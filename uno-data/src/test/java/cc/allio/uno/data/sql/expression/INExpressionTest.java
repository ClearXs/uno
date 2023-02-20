package cc.allio.uno.data.sql.expression;

import cc.allio.uno.data.sql.RuntimeColumn;
import cc.allio.uno.data.sql.query.WhereColumn;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class INExpressionTest extends BaseTestCase {

    @Test
    void testIN() {
        RuntimeColumn runtimeColumn = new WhereColumn("userName", new Object[]{"121", 21, "32"}, null);
        INExpression expression = new INExpression(runtimeColumn, TestExpressionContext.INSTANCE);
        assertEquals("user_name IN ({{user_name_IN_0}},{{user_name_IN_1}},{{user_name_IN_2}})", expression.getSQL());
    }
}
