package cc.allio.uno.data.orm.sql.ddl.elasticsearch;

import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class ElasticSearchShowColumnsOperatorTest extends BaseTestCase {

    ElasticSearchShowColumnsOperator searchShowColumnsOperator = new ElasticSearchShowColumnsOperator();

    @Test
    void testMapping() {
        searchShowColumnsOperator.from("bank");
    }
}
