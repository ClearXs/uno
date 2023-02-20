package cc.allio.uno.test.env;

import cc.allio.uno.test.BaseCoreTest;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

/**
 * Test环境外观类测试
 *
 * @author jiangwei
 * @date 2022/9/15 22:36
 * @since 1.1.0
 */
class TestSpringEnvironmentFacadeTest extends BaseTestCase {

    @Test
    void testConcat() {
        TestSpringEnvironmentFacade facade = new TestSpringEnvironmentFacade();
        facade.concat(new ExampleTestEnvironment());
        assertEquals(1, facade.size());
    }

    /**
     * Test Case: 测试复合嵌套下，Facade嵌套多个Facade，此时其实例大小为0
     */
    @Test
    void testComposeSizeIsZero() {
        TestSpringEnvironmentFacade facade1 = new TestSpringEnvironmentFacade();
        TestSpringEnvironmentFacade facade2 = new TestSpringEnvironmentFacade();
        TestSpringEnvironmentFacade facade3 = new TestSpringEnvironmentFacade();
        TestSpringEnvironmentFacade concat = new TestSpringEnvironmentFacade()
                .concat(facade1)
                .concat(facade2)
                .concat(facade3);
        assertEquals(0, concat.size());
    }

    /**
     * Test Case: 测试复合嵌套下，实例大小不为0
     */
    @Test
    void testComposeSizeIsNotZero() {
        ExampleTestEnvironment exampleTestEnvironment = new ExampleTestEnvironment();
        TestSpringEnvironmentFacade multiLayer  = new TestSpringEnvironmentFacade().concat(new TestSpringEnvironmentFacade().concat(new TestSpringEnvironmentFacade().concat(exampleTestEnvironment)));
        assertEquals(1, multiLayer.size());

    }

    @Override
    protected void onInit() throws Throwable {

    }

    @Override
    protected void onDown() throws Throwable {

    }

    static class ExampleTestEnvironment implements TestSpringEnvironment {

        @Override
        public void support(BaseCoreTest test) {

        }
    }
}
