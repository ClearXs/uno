package cc.allio.uno.data.orm.dsl.dml.elasticsearch;

import cc.allio.uno.data.orm.dsl.elasticsearch.dml.EsQueryOperator;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ElasticsearchQueryOperatorTest extends BaseTestCase {

    EsQueryOperator queryOperator = new EsQueryOperator();

    @BeforeEach
    public void setup() {
        queryOperator.reset();
    }

    @Test
    void testEQ() {
        String sql = queryOperator.eq("test", "1").from("bank").getDSL();
        assertEquals("{\"bool\":{\"must\":[{\"term\":{\"test\":{\"value\":\"1\"}}}]}}", sql);
    }

    @Test
    void testMatch() {
        String sql = queryOperator.like("test", "1").from("bank").getDSL();
        assertEquals("{\"bool\":{\"must\":[{\"match\":{\"test\":{\"query\":\"1\"}}}]}}", sql);
    }

    @Test
    void testGt() {
        String sql = queryOperator.gt("test", "1").from("bank").getDSL();
        assertEquals("{\"bool\":{\"must\":[{\"range\":{\"test\":{\"gt\":\"1\"}}}]}}", sql);
    }

    @Test
    void testLike$() {
        String sql = queryOperator.like$("test", "1").from("bank").getDSL();
        assertEquals("{\"bool\":{\"must\":[{\"wildcard\":{\"test\":{\"value\":\"1*\"}}}]}}", sql);
    }

    @Test
    void testAndOrAndNot() {
        String sql = queryOperator.like$("test", "1").from("bank").or().eq("t1", "2").getDSL();
        assertEquals("{\"bool\":{\"must\":[{\"wildcard\":{\"test\":{\"value\":\"1*\"}}}],\"should\":[{\"term\":{\"t1\":{\"value\":\"2\"}}}]}}", sql);
    }
}
