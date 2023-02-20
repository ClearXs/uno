package cc.allio.uno.component.sequential.convert;

import cc.allio.uno.component.sequential.TestSequential;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class TestSequentialConvertTest extends BaseTestCase {

    TestSequentialConvert converter;

    @Override
    protected void onInit() {
        converter = new TestSequentialConvert(TestSequential.class);
    }

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

    @Override
    protected void onDown() throws Throwable {

    }
}
