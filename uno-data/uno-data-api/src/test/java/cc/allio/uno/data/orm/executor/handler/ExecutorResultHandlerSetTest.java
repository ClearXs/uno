package cc.allio.uno.data.orm.executor.handler;

import cc.allio.uno.data.orm.User;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import cc.allio.uno.data.orm.executor.options.ExecutorOptionsImpl;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class ExecutorResultHandlerSetTest extends BaseTestCase {

    ExecutorOptions executorOptions = new ExecutorOptionsImpl(DBType.H2, ExecutorKey.DB, OperatorKey.SQL);

    @Test
    void testObtainBoolResultHandler() {
        BoolResultHandler boolResultHandler = executorOptions.obtainBoolResultHandler();
        assertNotNull(boolResultHandler);

        assertEquals(SPIBoolResultHandler.class, boolResultHandler.getClass());
    }

    @Test
    void testObtainBeanResultSetHandler() {
        BeanResultSetHandler<User> userBeanResultSetHandler = executorOptions.obtainBeanResultSetHandler(User.class);
        assertNotNull(userBeanResultSetHandler);
    }

    @Test
    void testObtainMapResultSetHandler() {
        MapResultSetHandler mapResultSetHandler = executorOptions.obtainMapResultSetHandler();
        assertNotNull(mapResultSetHandler);
    }

    @Test
    void testObtainDefaultResultSetHandler() {
        DefaultResultSetHandler defaultResultSetHandler = executorOptions.obtainDefaultResultSetHandler();
        assertNotNull(defaultResultSetHandler);
    }

    @Test
    void testObtainListBeanResultSetHandler() {
        ListBeanResultSetHandler<User> userListBeanResultSetHandler = executorOptions.obtainListBeanResultSetHandler(User.class);
        assertNotNull(userListBeanResultSetHandler);
    }

    @Test
    void testObtainDefaultListResultSetHandler() {
        DefaultListResultSetHandler defaultListResultSetHandler = executorOptions.obtainDefaultListResultSetHandler();
        assertNotNull(defaultListResultSetHandler);
    }

    @Test
    void testObtainListMapResultHandler() {
        ListMapResultHandler listMapResultHandler = executorOptions.obtainListMapResultHandler();
        assertNotNull(listMapResultHandler);
    }

    @Test
    void testObtainColumnDefListResultSetHandler() {
        ColumnDefListResultSetHandler columnDefListResultSetHandler = executorOptions.obtainColumnDefListResultSetHandler();
        assertNotNull(columnDefListResultSetHandler);
    }

    @Test
    void testObtainTableListResultSetHandler() {
        TableListResultSetHandler tableListResultSetHandler = executorOptions.obtainTableListResultSetHandler();
        assertNotNull(tableListResultSetHandler);
    }
}
