package cc.allio.uno.sequential.convert;

import cc.allio.uno.sequential.TestSequential;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class TestSequentialConvertTest extends BaseTestCase {

    TestSequentialConvert converter = new TestSequentialConvert(TestSequential.class);;


    @Test
    void testConvertType() {
        assertEquals(TestSequential.class, converter.getConvertType());
    }

    @Test
    void testExecuteAction() {
        assertDoesNotThrow(() -> {
            TestSequential sequential = (TestSequential) converter.execute(null, "{\"toText\":\"toText\"}");
            assertEquals("test", sequential.getType());
            assertEquals("toText", sequential.getToText());
            assertNotNull(sequential.getUnknownDate());
            assertNull(sequential.getExcludeField());
        });
    }

}
