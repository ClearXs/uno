package cc.allio.uno.data.orm.dsl.mongodb.ddl;

import cc.allio.uno.data.orm.dsl.opeartorgroup.OperatorGroup;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class MongoAlterCollectionOperatorTest extends BaseTestCase {

    @Test
    void testRename() {
        MongodbAlterCollectionOperator alterTableOperator = OperatorGroup.getAlterTableOperator(MongodbAlterCollectionOperator.class, OperatorKey.MONGODB);
        assertNotNull(alterTableOperator);
        alterTableOperator.from(Table.of("a")).rename(Table.of("b"));

        String dsl = alterTableOperator.getDSL();
        assertEquals("{\"renameCollection\": \"a\", \"to\": \"b\"}", dsl);
    }
}
