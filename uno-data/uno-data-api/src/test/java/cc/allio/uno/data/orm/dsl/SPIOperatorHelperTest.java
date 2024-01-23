package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.data.orm.dsl.ddl.CreateTableOperator;
import cc.allio.uno.data.orm.dsl.ddl.ShowColumnsOperator;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class SPIOperatorHelperTest extends BaseTestCase {

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
        var insert = SPIOperatorHelper.lazyGet(ShowColumnsOperator.class, OperatorKey.REDIS);
        assertNotNull(insert);
    }

    @Test
    void testSameKeySecondOperator() {
        var show = SPIOperatorHelper.lazyGet(ShowColumnsOperator.class, OperatorKey.REDIS);
        assertNotNull(show);

        var insertOperator = SPIOperatorHelper.lazyGet(InsertOperator.class, OperatorKey.REDIS);
        assertNotNull(insertOperator);
    }

    @Test
    void testNotFoundOperator() {
        assertThrows(DSLException.class, () -> SPIOperatorHelper.lazyGet(UpdateOperator.class, OperatorKey.SQL));
    }
}
