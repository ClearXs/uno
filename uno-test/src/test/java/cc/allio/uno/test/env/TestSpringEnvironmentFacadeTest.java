package cc.allio.uno.test.env;

import cc.allio.uno.test.CoreTest;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

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
        EnvironmentFacade facade = new EnvironmentFacade();
        facade.concat(new ExampleTestEnvironment());
        assertEquals(1, facade.size());
    }

    /**
     * Test Case: 测试复合嵌套下，Facade嵌套多个Facade，此时其实例大小为0
     */
    @Test
    void testComposeSizeIsZero() {
        EnvironmentFacade facade1 = new EnvironmentFacade();
        EnvironmentFacade facade2 = new EnvironmentFacade();
        EnvironmentFacade facade3 = new EnvironmentFacade();
        EnvironmentFacade concat = new EnvironmentFacade()
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
        EnvironmentFacade multiLayer  = new EnvironmentFacade().concat(new EnvironmentFacade().concat(new EnvironmentFacade().concat(exampleTestEnvironment)));
        assertEquals(1, multiLayer.size());

    }

    @Override
    protected void onInit() throws Throwable {

    }

    @Override
    protected void onDown() throws Throwable {

    }

    static class ExampleTestEnvironment implements Environment {

        @Override
        public void support(CoreTest test) {

        }

        @Override
        public Class<? extends Annotation>[] getPropertiesAnnotation() {
            return null;
        }
    }
}
