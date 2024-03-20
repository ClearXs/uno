package cc.allio.uno.data.orm.dsl.dml.elasticsearch;

import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class ElasticSearchInsertOperatorTest extends BaseTestCase {

    EsInsertOperator operator = new EsInsertOperator();

    @Test
    void testSingeInsert() {
        String sql = operator.from("test").insert("t1", "t2").getDSL();
        assertEquals("[{\"registry\":{\"_id\":\"1112832259796238336\",\"_index\":\"test\"}}]", sql);
    }
}
