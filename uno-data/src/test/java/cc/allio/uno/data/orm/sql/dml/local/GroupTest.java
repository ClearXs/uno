package cc.allio.uno.data.orm.sql.dml.local;

import cc.allio.uno.data.orm.sql.dml.local.expression.TestExpressionContext;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

/**
 * Group语句构建测试
 *
 * @author jiangwei
 * @date 2022/10/1 14:31
 * @since 1.1.0
 */
class GroupTest extends BaseTestCase {

    @Test
    void testByOne() {
        GroupStatement group = new GroupStatement(TestExpressionContext.INSTANCE);
        String sql = group.byOne("userName")
                .byOnes("z")
                .getSQL();
        assertEquals("GROUP BY user_name , z", sql);
    }

}
