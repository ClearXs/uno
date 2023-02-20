package cc.allio.uno.data.sql;

import cc.allio.uno.data.sql.expression.TestExpressionContext;
import cc.allio.uno.data.sql.query.OrderStatement;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

/**
 * Order语句测试
 *
 * @author jiangwei
 * @date 2022/10/1 14:40
 * @since 1.1.0
 */
class OrderTest extends BaseTestCase {

    @Test
    void byAscAndByDesc() {
        OrderStatement orderStatement = new OrderStatement(TestExpressionContext.INSTANCE);
        String sql = orderStatement.byAsc("userName")
                .byDesc("iPhone")
                .getSQL();
        assertEquals("ORDER user_name ASC , i_phone DESC", sql);
    }

    @Test
    void testBy() {
        OrderStatement orderStatement = new OrderStatement(TestExpressionContext.INSTANCE);
        String sql = orderStatement.by("userName").getSQL();
        assertEquals("ORDER user_name DESC", sql);
    }

}
