package cc.allio.uno.data.orm.dsl.mongodb.ddl;

import cc.allio.uno.data.orm.dsl.opeartorgroup.OperatorGroup;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class MongodbCreateCollectionOperatorTest extends BaseTestCase {

    @Test
    void testCrate() {
        MongodbCreateCollectionOperator createTableOperator = OperatorGroup.getCreateTableOperator(MongodbCreateCollectionOperator.class, OperatorKey.MONGODB);
        assertNotNull(createTableOperator);

        String dsl = createTableOperator.from(Table.of("dual")).getDSL();
        assertEquals("{\"create\": \"dual\"}", dsl);

    }
}
