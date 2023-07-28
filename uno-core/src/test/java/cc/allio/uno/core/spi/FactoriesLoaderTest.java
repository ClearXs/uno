package cc.allio.uno.core.spi;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.function.Consumer;

/**
 * 单元测试FactoriesLoader
 *
 * @author jiangwei
 * @date 2022/4/1 10:10
 * @see FactoriesLoader
 * @since 1.0.6
 */
class FactoriesLoaderTest extends BaseTestCase {

    @Override
    protected void onInit() throws Throwable {
        Consumer<Object> c = System.out::println;
        c.accept(new Object());
    }

    /**
     * Test Case: 测试加载只有一个类型的实现
     */
    @Test
    void testLoadSingleFactories() {
        FactoriesLoader factoriesLoader = new FactoriesLoader("META-INF/test1.factories");
        assertDoesNotThrow(() -> {
            Set<Class<Demo1>> classes = factoriesLoader.loadFactoriesByType(Demo1.class, Thread.currentThread().getContextClassLoader());
            assertEquals(1, classes.size());
            for (Class<Demo1> aClass : classes) {
                assertEquals(Demo1Impl.class, aClass);
            }
        });
    }

    /**
     * Test Case: 测试加载多个一个类型的实现
     */
    @Test
    void testLoadMultipleFactories() {
        FactoriesLoader factoriesLoader = new FactoriesLoader("META-INF/test2.factories");
        assertDoesNotThrow(() -> {
            Set<Class<Demo1>> demo1Class = factoriesLoader.loadFactoriesByType(Demo1.class, Thread.currentThread().getContextClassLoader());
            assertEquals(2, demo1Class.size());
            Set<Class<Demo2>> demo2Class = factoriesLoader.loadFactoriesByType(Demo2.class, Thread.currentThread().getContextClassLoader());
            assertEquals(1, demo2Class.size());
        });
    }

    @Override
    protected void onDown() throws Throwable {

    }
}
