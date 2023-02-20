package cc.allio.uno.data.sql;

import cc.allio.uno.data.model.User;
import cc.allio.uno.data.sql.expression.TestExpressionContext;
import cc.allio.uno.data.sql.query.WhereStatement;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

/**
 * Where-SQL语句测试
 *
 * @author jiangwei
 * @date 2022/10/1 14:48
 * @since 1.1.0
 */
class WhereTest extends BaseTestCase {

    WhereStatement where = new WhereStatement(TestExpressionContext.INSTANCE);

    @Test
    void testEmpty() {
        String sql = where.getSQL();
        assertEquals("", sql);
    }

    @Test
    void testGt() {
        where.gt("userName", 1);
        String sql = where.getSQL();
        assertEquals("WHERE user_name > {{user_name_gt_0}}", sql);
    }

    @Test
    void testGte() {
        where.gte("userName", 1);
        String sql = where.getSQL();
        assertEquals("WHERE user_name >= {{user_name_gte_0}}", sql);
    }

    @Test
    void testLt() {
        where.lt("userName", 1);
        String sql = where.getSQL();
        assertEquals("WHERE user_name < {{user_name_lt_0}}", sql);
    }

    @Test
    void testLte() {
        where.lte("userName", 1);
        String sql = where.getSQL();
        assertEquals("WHERE user_name <= {{user_name_lte_0}}", sql);
    }

    @Test
    void testEq() {
        where.eq("userName", 1);
        String sql = where.getSQL();
        assertEquals("WHERE user_name = {{user_name_eq_0}}", sql);
    }

    @Test
    void testEQAndLT() {
        where.eq("userName", 1);
        where.lt("userName", 1);
        String sql = where.getSQL();
        assertEquals("WHERE user_name = {{user_name_eq_0}} AND user_name < {{user_name_lt_0}}", sql);
    }

    @Test
    void testEQOrLT() {
        where.eq("userName", 1)
                .or()
                .lt("userName", 1);
        String sql = where.getSQL();
        assertEquals("WHERE user_name = {{user_name_eq_0}} OR user_name < {{user_name_lt_0}}", sql);
    }

    @Test
    void testUserEQAndLEOrLike() {
        String sql = where.lt(User::getName, "1")
                .lt(User::getRoleId, "2")
                .or()
                .like$(User::getId, "3")
                .getSQL();
        assertEquals("WHERE name < {{name_lt_0}} AND role_id < {{role_id_lt_0}} OR id LIKE '{{id_LIKE_0}}%'", sql);
    }
}
