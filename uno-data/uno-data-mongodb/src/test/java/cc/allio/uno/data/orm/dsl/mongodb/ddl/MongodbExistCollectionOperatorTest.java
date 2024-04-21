package cc.allio.uno.data.orm.dsl.mongodb.ddl;

import cc.allio.uno.data.orm.dsl.opeartorgroup.OperatorGroup;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class MongodbExistCollectionOperatorTest extends BaseTestCase {

    @Test
    void testExistCollection() {
        MongodbExistCollectionOperator existTableOperator = OperatorGroup.getExistTableOperator(MongodbExistCollectionOperator.class, OperatorKey.MONGODB);

        assertNotNull(existTableOperator);

        String dsl = existTableOperator.from(Table.of("dual")).getDSL();
        assertEquals("{\"listCollections\": 1, \"filter\": {\"name\": {\"$eq\": \"dual\"}}}", dsl);
    }
}
