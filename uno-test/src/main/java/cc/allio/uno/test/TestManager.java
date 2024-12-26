package cc.allio.uno.test;

import cc.allio.uno.core.bus.EventBus;
import cc.allio.uno.test.listener.Listener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.TestContextManager;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 测试框架管理器，参考自{@link TestContextManager}实现。
 * <p>通过事件机制，当test经历不同的测试阶段时发布对应的事件。事件使用于{@link EventBus}</p>
 *
 * @author j.x
 * @since 1.1.4
 */
@Getter
@Slf4j
public final class TestManager {

    /**
     * 测试阶段周期
     */
    public static final String INIT = "INIT";
    public static final String BEFORE_ENTRY_CLASS = "BEFORE_ENTRY_CLASS";
    public static final String AFTER_ENTRY_CLASS = "AFTER_ENTRY_CLASS";
    public static final String PREPARE_TEST_INSTANCE = "PREPARE_TEST_INSTANCE";
    public static final String BEFORE_ENTRY_METHOD = "BEFORE_ENTRY_METHOD";
    public static final String AFTER_ENTRY_METHOD = "AFTER_ENTRY_METHOD";
    public static final String BEFORE_TEST_EXECUTION = "BEFORE_TEST_EXECUTION";
    public static final String AFTER_TEST_EXECUTION = "AFTER_TEST_EXECUTION";

    // 测试上下文，保存在不同测试阶段的中间数据
    private final TestContext testContext;

    public TestManager(Class<?> testClass) {
        this.testContext = new TestContext(testClass);
        this.testContext.setStage(INIT);
    }

    /**
     * 用于在执行类中的任何测试之前对测试类进行预处理。例如使用{@link org.junit.jupiter.api.BeforeAll}
     *
     * @see TestContextManager#beforeTestClass()
     */
    public void beforeEntryClass() {
        getTestContext().setStage(BEFORE_ENTRY_CLASS);
        for (Listener lis : getTestContext().getRunTestAttributes().getListeners()) {
            try {
                lis.beforeEntryClass(getTestContext());
            } catch (Exception ex) {
                log.error("Test Execution stage: [beforeEntryClass], catch Exception: ", ex);
                throw new UndeclaredThrowableException(ex);
            }
        }
    }

    /**
     * 用于在类内执行所有测试后对测试类进行后处理。例如使用{@link org.junit.jupiter.api.AfterAll}
     *
     * @see TestContextManager#afterTestClass()
     */
    public void afterEntryClass() {
        getTestContext().setStage(AFTER_ENTRY_CLASS);
        for (Listener lis : getTestContext().getRunTestAttributes().getListeners()) {
            try {
                lis.afterEntryClass(getTestContext());
            } catch (Exception ex) {
                log.error("Test Execution stage: [afterEntryClass], catch Exception: ", ex);
                throw new UndeclaredThrowableException(ex);
            }
        }
    }

    /**
     * 在测试类实例化之后立即被调用
     *
     * @param testInstance 测试实例
     * @see TestContextManager#prepareTestInstance(Object)
     */
    public void prepareTestInstance(Object testInstance) {
        getTestContext().setStage(PREPARE_TEST_INSTANCE);
        getTestContext().setTestInstance(testInstance);
        for (Listener lis : getTestContext().getRunTestAttributes().getListeners()) {

            try {
                lis.prepareTestInstance(getTestContext());
            } catch (Exception ex) {
                log.error("Test Execution stage: [prepareTestInstance], catch Exception: ", ex);
                throw new UndeclaredThrowableException(ex);
            }
        }
    }

    /**
     * 用于在底层测试框架的生命周期回调执行之前对测试进行预处理——例如，设置测试fixture，启动事务等。例如使用{@link org.junit.jupiter.api.BeforeEach}
     *
     * @param testInstance 测试实例
     * @param testMethod   测试方法
     * @see TestContextManager#beforeTestMethod(Object, Method)
     */
    public void beforeEntryMethod(Object testInstance, Method testMethod) {
        getTestContext().setStage(BEFORE_ENTRY_METHOD);
        getTestContext().setTestInstance(testInstance);
        getTestContext().setTestMethod(testMethod);
        for (Listener lis : getTestContext().getRunTestAttributes().getListeners()) {
            try {
                lis.beforeEntryMethod(getTestContext());
            } catch (Exception ex) {
                log.error("Test Execution stage: [beforeEntryMethod], catch Exception: ", ex);
                throw new UndeclaredThrowableException(ex);
            }
        }
    }

    /**
     * 用于在执行底层测试框架的生命周期后回调之后对测试进行后处理——例如，拆除测试fixture，结束事务等。例如使用{@link org.junit.jupiter.api.AfterEach}
     *
     * @param testInstance 测试实例
     * @param testMethod   测试方法
     * @param exception    在测试方法执行期间或由TestExecutionListener抛出的异常，如果没有抛出则为null
     * @see TestContextManager#afterTestMethod(Object, Method, Throwable)
     */
    public void afterEntryMethod(Object testInstance, Method testMethod, Throwable exception) {
        if (exception != null) {
            throw new UndeclaredThrowableException(exception);
        }
        getTestContext().setStage(AFTER_ENTRY_METHOD);
        getTestContext().setTestInstance(testInstance);
        getTestContext().setTestMethod(testMethod);
        for (Listener lis : getTestContext().getRunTestAttributes().getListeners()) {
            try {
                lis.afterEntryMethod(getTestContext());
            } catch (Exception ex) {
                log.error("Test Execution stage: [afterEntryMethod], catch Exception: ", ex);
                throw new UndeclaredThrowableException(ex);
            }
        }
    }

    /**
     * 钩子，用于在所提供的测试上下文中执行测试方法之前立即对测试进行预处理——例如，用于计时或记录目的。
     *
     * @param testInstance 测试实例
     * @param testMethod   测试方法
     * @see TestContextManager#beforeTestExecution(Object, Method)
     */
    public void beforeTestExecution(Object testInstance, Method testMethod) {
        getTestContext().setStage(BEFORE_TEST_EXECUTION);
        getTestContext().setTestInstance(testInstance);
        getTestContext().setTestMethod(testMethod);
        for (Listener lis : getTestContext().getRunTestAttributes().getListeners()) {
            try {
                lis.beforeTestExecution(getTestContext());
            } catch (Exception ex) {
                log.error("Test Execution stage: [beforeTestExecution], catch Exception: ", ex);
                throw new UndeclaredThrowableException(ex);
            }
        }
    }

    /**
     * 用于在所提供的测试上下文中执行测试方法后立即对测试进行后处理——例如，用于计时或记录目的。
     *
     * @param testInstance 测试实例
     * @param testMethod   测试方法
     * @param exception    在测试方法执行期间或由TestExecutionListener抛出的异常，如果没有抛出则为null
     * @see TestContextManager#afterTestExecution(Object, Method, Throwable)
     */
    public void afterTestExecution(Object testInstance, Method testMethod, Throwable exception) {
        if (exception != null) {
            throw new UndeclaredThrowableException(exception);
        }
        getTestContext().setStage(AFTER_TEST_EXECUTION);
        getTestContext().setTestInstance(testInstance);
        getTestContext().setTestMethod(testMethod);
        for (Listener lis : getTestContext().getRunTestAttributes().getListeners()) {
            try {
                lis.afterTestExecution(getTestContext());
            } catch (Exception ex) {
                log.error("Test Execution stage: [afterTestExecution], catch Exception: ", ex);
                throw new UndeclaredThrowableException(ex);
            }
        }
    }

}
