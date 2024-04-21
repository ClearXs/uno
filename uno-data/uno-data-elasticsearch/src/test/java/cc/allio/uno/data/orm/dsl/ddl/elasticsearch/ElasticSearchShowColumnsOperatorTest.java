package cc.allio.uno.data.orm.dsl.ddl.elasticsearch;

import cc.allio.uno.data.orm.dsl.elasticsearch.ddl.EsShowColumnsOperator;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class ElasticSearchShowColumnsOperatorTest extends BaseTestCase {

    EsShowColumnsOperator searchShowColumnsOperator = new EsShowColumnsOperator();

    @Test
    void testMapping() {
        searchShowColumnsOperator.from("bank");
    }
}
