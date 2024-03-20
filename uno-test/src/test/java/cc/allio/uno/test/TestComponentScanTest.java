package cc.allio.uno.test;

import cc.allio.uno.test.config.TestConfiguration;
import org.junit.jupiter.api.Test;

import java.util.Set;

@RunTest(components = TestConfiguration.class)
class TestComponentScanTest extends BaseTestCase {

    @Test
    void testCom(@Parameter CoreTest coreTest) {
        Set<Class<?>> componentsClasses = coreTest.getRunTestAttributes().getComponentsClasses();
        assertEquals(3, componentsClasses.size());
    }
}
