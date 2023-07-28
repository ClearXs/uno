package cc.allio.uno.data.orm.sql.dml.elasticsearch;

import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class ElasticSearchInsertOperatorTest extends BaseTestCase {

    ElasticSearchInsertOperator operator = new ElasticSearchInsertOperator();

    @Test
    void testSingeInsert() {
        String sql = operator.from("test").insert("t1", "t2").getSQL();
        assertEquals("[{\"create\":{\"_id\":\"1112832259796238336\",\"_index\":\"test\"}}]", sql);
    }
}
