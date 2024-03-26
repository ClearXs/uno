package cc.allio.uno.data.orm.dsl.mongodb.ddl;

import cc.allio.uno.data.orm.dsl.OperatorGroup;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class MongodbDropCollectionOperatorTest extends BaseTestCase {

    @Test
    void testDropCollection() {

        MongodbDropCollectionOperator dropTableOperator = OperatorGroup.getDropTableOperator(MongodbDropCollectionOperator.class, OperatorKey.MONGODB);

        assertNotNull(dropTableOperator);

        String dsl = dropTableOperator.from(Table.of("dual")).getDSL();

        assertEquals("{\"drop\": \"dual\"}", dsl);

    }
}
