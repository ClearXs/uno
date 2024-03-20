package cc.allio.uno.data.orm.dsl.mongodb.ddl;

import cc.allio.uno.data.orm.dsl.OperatorGroup;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class MongoAlterCollectionOperatorTest extends BaseTestCase {

    @Test
    void testRename() {
        MongodbAlterCollectionOperator alterTableOperator = OperatorGroup.getAlterTableOperator(MongodbAlterCollectionOperator.class, OperatorKey.MONGODB, DBType.MONGODB);

        assertNotNull(alterTableOperator);
    }
}
