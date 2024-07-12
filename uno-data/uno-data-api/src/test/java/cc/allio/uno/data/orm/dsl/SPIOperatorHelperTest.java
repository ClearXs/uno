package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.data.orm.dsl.ddl.CreateTableOperator;
import cc.allio.uno.data.orm.dsl.ddl.ShowColumnsOperator;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class SPIOperatorHelperTest extends BaseTestCase {

    @Test
    void testJustFindOne() {
        InsertOperator sqlInsertOperator = SPIOperatorHelper.lazyGet(InsertOperator.class, OperatorKey.REDIS);
        assertNotNull(sqlInsertOperator);
    }

    @Test
    void testFindMulti() {
        assertThrows(DSLException.class, () -> SPIOperatorHelper.lazyGet(CreateTableOperator.class, OperatorKey.SQL));
    }

    @Test
    void testMultiOperatorKey() {
        ShowColumnsOperator insert = SPIOperatorHelper.lazyGet(ShowColumnsOperator.class, OperatorKey.REDIS);
        assertNotNull(insert);
    }

    @Test
    void testSameKeySecondOperator() {
        ShowColumnsOperator show = SPIOperatorHelper.lazyGet(ShowColumnsOperator.class, OperatorKey.REDIS);
        assertNotNull(show);
        InsertOperator insertOperator = SPIOperatorHelper.lazyGet(InsertOperator.class, OperatorKey.REDIS);
        assertNotNull(insertOperator);
    }

    @Test
    void testNotFoundOperator() {
        assertThrows(DSLException.class, () -> SPIOperatorHelper.lazyGet(UpdateOperator.class, OperatorKey.SQL));
    }

    @Test
    void testFindHireachicalOperator() {
        RedisShowColumnsOperator show = SPIOperatorHelper.lazyGet(RedisShowColumnsOperator.class, OperatorKey.REDIS);
        assertNotNull(show);
    }
}
