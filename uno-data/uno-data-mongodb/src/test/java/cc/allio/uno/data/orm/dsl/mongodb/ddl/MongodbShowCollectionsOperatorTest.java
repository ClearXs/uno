package cc.allio.uno.data.orm.dsl.mongodb.ddl;

import cc.allio.uno.data.orm.dsl.opeartorgroup.Operators;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class MongodbShowCollectionsOperatorTest extends BaseTestCase {

    @Test
    void testShowCollections() {
        MongodbShowCollectionsOperator showTablesOperator = Operators.getShowTablesOperator(MongodbShowCollectionsOperator.class, OperatorKey.MONGODB);
        assertNotNull(showTablesOperator);

        String dsl = showTablesOperator.from("dual1").from("dual2").getDSL();

        assertEquals("{\"listCollections\": 1, \"filter\": {\"name\": {\"$eq\": [\"dual1\", \"dual2\"]}}}", dsl);
    }
}
