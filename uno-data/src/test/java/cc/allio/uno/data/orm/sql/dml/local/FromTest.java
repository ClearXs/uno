package cc.allio.uno.data.orm.sql.dml.local;

import cc.allio.uno.data.orm.sql.dml.local.expression.TestExpressionContext;
import cc.allio.uno.data.orm.sql.FromStatement;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class FromTest extends BaseTestCase {

    @Test
    void testFrom() {
        FromStatement fromStatement = new FromStatement(TestExpressionContext.INSTANCE);
        String sql = fromStatement.from("tableA").getSQL();
        assertEquals("FROM table_a", sql);

        String sql2 = fromStatement.from("tableB").getSQL();
        assertEquals("FROM table_b", sql2);


    }
}
