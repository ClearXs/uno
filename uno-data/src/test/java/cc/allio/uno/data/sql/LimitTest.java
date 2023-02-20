package cc.allio.uno.data.sql;

import cc.allio.uno.data.sql.query.Limit;
import cc.allio.uno.data.sql.query.LimitStatement;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class LimitTest extends BaseTestCase {

    Limit<?> limit = new LimitStatement();

    @Test
    void testPage() {
        String sql = limit.page(1, 10).getSQL();
        assertEquals("LIMIT 0 OFFSET 10", sql);
    }
}
