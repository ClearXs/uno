package cc.allio.uno.data.sql.expression;

import cc.allio.uno.data.sql.Table;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class FromExpressionTest extends BaseTestCase {

    @Test
    void testFrom() {
        FromExpression expression = new FromExpression(new Table("dual"), TestExpressionContext.INSTANCE);
        assertEquals("{{dual_FROM_0}}", expression.getSQL());
    }
}
