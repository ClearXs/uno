package cc.allio.uno.data.orm.executor.internal;

import cc.allio.uno.data.orm.dsl.RedisInsertOperator;
import cc.allio.uno.data.orm.dsl.RedisShowColumnsOperator;
import cc.allio.uno.data.orm.dsl.ddl.ShowColumnsOperator;
import cc.allio.uno.data.orm.dsl.dml.DeleteOperator;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.test.BaseTestCase;
import lombok.var;
import org.junit.jupiter.api.Test;

class SPIInnerCommandScannerTest extends BaseTestCase {

    @Test
    void testScanDirectlyType() {
        SPIInnerCommandScanner scanner = new SPIInnerCommandScanner(ExecutorKey.REDIS);
        InnerCommandExecutorManager manager = scanner.scan();
        assertNotNull(manager);

        SCOInnerCommandExecutor<Object, ShowColumnsOperator<?>> showColumn = manager.getShowColumn();
        assertNotNull(showColumn);
        InnerCommandExecutor<Object, RedisShowColumnsOperator, Object> showByGet = manager.get(RedisShowColumnsOperator.class);
        assertEquals(showColumn, showByGet);

        IOInnerCommandExecutor<InsertOperator> insert = manager.getInsert();
        assertNotNull(insert);
        var insertByGet = manager.get(RedisInsertOperator.class);
        assertEquals(insert, insertByGet);

        // test null
        QOInnerCommandExecutor<Object, QueryOperator> query = manager.getQuery();
        assertNull(query);
    }

    @Test
    void testIndeterminateType() {
        SPIInnerCommandScanner scanner = new SPIInnerCommandScanner(ExecutorKey.NEO4j);

        assertDoesNotThrow(() -> {
            InnerCommandExecutorManager manager = scanner.scan();
            assertNotNull(manager);

            DOInnerCommandExecutor<DeleteOperator> delete = manager.getDelete();
            assertNull(delete);
        });

    }
}
