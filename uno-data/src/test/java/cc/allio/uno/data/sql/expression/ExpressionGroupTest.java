package cc.allio.uno.data.sql.expression;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.sql.PlainColumn;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class ExpressionGroupTest extends BaseTestCase {

    @Test
    void testOfferToSQL() {
        PlainColumn column1 = new PlainColumn("userName", null, null);
        ExpressionGroup expressionGroup = new ExpressionGroup();
        expressionGroup.offer(new PlainExpression(column1, TestExpressionContext.INSTANCE), StringPool.COMMA);
        assertEquals("user_name", expressionGroup.getSQL());

        PlainColumn column2 = new PlainColumn("userAge", null, null);
        expressionGroup.offer(new PlainExpression(column2, TestExpressionContext.INSTANCE), StringPool.COMMA);
        assertEquals("user_name , user_age", expressionGroup.getSQL());

    }
}
