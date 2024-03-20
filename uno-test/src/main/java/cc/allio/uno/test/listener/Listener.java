package cc.allio.uno.test.listener;

import cc.allio.uno.test.TestContext;

/**
 * 测试回调监听器
 *
 * @author j.x
 * @date 2023/3/2 20:08
 * @since 1.1.4
 */
public interface Listener {

    /**
     * 用于在执行类中的任何测试之前对测试类进行预处理。例如使用{@link org.junit.jupiter.api.BeforeAll}
     *
     * @param testContext 测试执行过程中上下文
     */
    default void beforeEntryClass(TestContext testContext) throws Exception {
    }

    /**
     * 用于在类内执行所有测试后对测试类进行后处理。例如使用{@link org.junit.jupiter.api.AfterAll}
     *
     * @param testContext 测试执行过程上下文
     */
    default void afterEntryClass(TestContext testContext) throws Exception {
    }

    /**
     * 在测试类实例化之后立即被调用
     *
     * @param testContext 测试执行过程上下文
     */
    default void prepareTestInstance(TestContext testContext) throws Exception {
    }

    /**
     * 用于在底层测试框架的生命周期回调执行之前对测试进行预处理——例如，设置测试fixture，启动事务等。例如使用{@link org.junit.jupiter.api.BeforeEach}
     *
     * @param testContext 测试执行过程上下文
     */
    default void beforeEntryMethod(TestContext testContext) throws Exception {
    }

    /**
     * 用于在执行底层测试框架的生命周期后回调之后对测试进行后处理——例如，拆除测试fixture，结束事务等。例如使用{@link org.junit.jupiter.api.AfterEach}
     *
     * @param testContext 测试执行过程上下文
     */
    default void afterEntryMethod(TestContext testContext) throws Exception {

    }

    /**
     * 钩子，用于在所提供的测试上下文中执行测试方法之前立即对测试进行预处理——例如，用于计时或记录目的。
     *
     * @param testContext 测试执行过程上下文
     */
    default void beforeTestExecution(TestContext testContext) throws Exception {
    }

    /**
     * 用于在所提供的测试上下文中执行测试方法后立即对测试进行后处理——例如，用于计时或记录目的。
     *
     * @param testContext 测试执行过程上下文
     */
    default void afterTestExecution(TestContext testContext) throws Exception {

    }
}
