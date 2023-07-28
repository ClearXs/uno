package cc.allio.uno.data.orm.sql.dml.local.expression;

import cc.allio.uno.data.orm.sql.Table;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class FromExpressionTest extends BaseTestCase {

    @Test
    void testFrom() {
        FromExpression expression = new FromExpression(Table.of("dual"), TestExpressionContext.INSTANCE);
        assertEquals("{{dual_FROM_0}}", expression.getSQL());
    }
}
