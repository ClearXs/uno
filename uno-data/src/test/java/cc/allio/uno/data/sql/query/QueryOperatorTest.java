package cc.allio.uno.data.sql.query;

import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class QueryOperatorTest extends BaseTestCase {

    QueryOperator queryOperator = new QueryOperator();

    @Test
    void testSelectFrom() {
        String sql = queryOperator.thenSelect().select("z").thenFrom().from("dual").getSQL();
        assertEquals("SELECT z FROM dual", sql);
    }

    @Test
    void testSelectDistinct() {
        String sql = queryOperator.thenSelect().distinct().select("name").thenFrom().from("dual").getSQL();
        assertEquals("SELECT DISTINCT name FROM dual", sql);
    }

    @Test
    void testSelectWhere() {
        String sql = queryOperator
                .thenFrom().from("tableA")
                .thenSelect().selectAll()
                .thenWhere().eq("a", "1").eq("b", 2)
                .getSQL();
        assertEquals("SELECT * FROM table_a WHERE a = '1' AND b = '2'", sql);
    }

    @Test
    void testSelectFromWhereOrder() {
    }
}
