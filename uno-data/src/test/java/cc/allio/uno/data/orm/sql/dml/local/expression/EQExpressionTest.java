package cc.allio.uno.data.orm.sql.dml.local.expression;

import cc.allio.uno.data.orm.sql.RuntimeColumn;
import cc.allio.uno.data.orm.sql.dml.local.WhereColumn;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.Map;

class EQExpressionTest extends BaseTestCase {

    @Test
    void testEQ() {
        RuntimeColumn runtimeColumn = new WhereColumn("userName", new Object[]{"test"}, null);
        EQExpression expression = new EQExpression(runtimeColumn, TestExpressionContext.INSTANCE);
        assertEquals("user_name = {{user_name_eq_0}}", expression.getSQL());
    }

    @Test
    void testLiteral() {
        RuntimeColumn runtimeColumn = new WhereColumn("userName", new Object[]{"test"}, null);
        EQExpression expression = new EQExpression(runtimeColumn, TestExpressionContext.INSTANCE);
        String literalSQL = expression.getLiteralSQL();
        System.out.println(literalSQL);
    }

    @Test
    void testExpValues() {
        RuntimeColumn runtimeColumn = new WhereColumn("userName", new Object[]{"test"}, null);
        EQExpression expression = new EQExpression(runtimeColumn, TestExpressionContext.INSTANCE);
        Map<String, Object> expMapping = expression.getExpMapping();
        System.out.println(expMapping);
    }
}
