package cc.allio.uno.data.orm.executor.options;

import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class ExecutorOptionsTest extends BaseTestCase {

    @Test
    public void testOptionsObtains() {
        ExecutorOptions executorOptions = new ExecutorOptionsImpl(DBType.H2, ExecutorKey.DB, OperatorKey.SQL);

        DBType dbType = executorOptions.getDbType();

        assertEquals(DBType.H2, dbType);

        ExecutorKey executorKey = executorOptions.getExecutorKey();

        assertEquals(ExecutorKey.DB, executorKey);

        OperatorKey operatorKey = executorOptions.getOperatorKey();
        assertEquals(OperatorKey.SQL, operatorKey);

        String address = executorOptions.getAddress();

        assertNull(address);
    }
}
