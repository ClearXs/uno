package cc.allio.uno.data.orm.sql.dml.local;

import cc.allio.uno.data.orm.sql.dml.Limit;
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
