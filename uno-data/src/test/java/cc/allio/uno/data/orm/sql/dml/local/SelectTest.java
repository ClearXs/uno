package cc.allio.uno.data.orm.sql.dml.local;

import cc.allio.uno.data.orm.sql.dml.local.expression.TestExpressionContext;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

/**
 * Select语句测试
 *
 * @author jiangwei
 * @date 2022/10/1 14:21
 * @since 1.1.0
 */
class SelectTest extends BaseTestCase {

    SelectStatement selectStatement = new SelectStatement(TestExpressionContext.INSTANCE);

    @Test
    void testSelectFieldName() {
        String sql = selectStatement.select("z")
                .select("userName")
                .getSQL();
        assertEquals("SELECT z , user_name", sql);
    }

    @Test
    void testSelectMultiFields() {
        String sql = selectStatement.select(new String[]{"userName", "userAge"}).getSQL();
        assertEquals("SELECT user_name , user_age", sql);
    }

    @Test
    void testSelectAlias() {
        String sql = selectStatement.select("userName", "a").getSQL();
        assertEquals("SELECT user_name AS a", sql);
    }

    @Test
    void testSelectAll() {
        String sql = selectStatement.selectAll().getSQL();
        assertEquals("SELECT *", sql);
    }

    @Test
    void testSelectMin() {
        String sql = selectStatement.min("userName").getSQL();
        assertEquals("SELECT MIN(user_name)", sql);
    }

    @Test
    void testSelectCount() {
        String sql = selectStatement.count().getSQL();
        assertEquals("SELECT COUNT(*) count", sql);
    }

}
