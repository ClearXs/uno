package cc.allio.uno.test;

import cc.allio.uno.test.env.Visitor;
import cc.allio.uno.test.runner.CoreRunner;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * 不更改原先{@link CoreTest}的基础上，重写并增加具有{@link TestContext}等的相关方法
 *
 * @author jiangwei
 * @date 2023/3/6 11:45
 * @since 1.1.4
 */
@Slf4j
public class ExecutableCoreTest extends CoreTest {

    private final Class<?> testClass;
    private final TestContext testContext;

    public ExecutableCoreTest(Class<?> testClass, TestContext testContext) {
        this.testClass = testClass;
        this.testContext = testContext;
    }

    /**
     * set CoreRunner
     *
     * @param coreRunner coreRunner
     * @see RunTestAttributes#getCoreRunner()
     */
    @Override
    @Deprecated
    public void setCoreRunner(CoreRunner coreRunner) {
    }

    /**
     * get coreRunner
     *
     * @return coreRunner
     */
    @Override
    public CoreRunner getCoreRunner() {
        return getRunTestAttributes().getCoreRunner();
    }

    /**
     * 设置Visitor
     *
     * @param visitors visitors
     * @see RunTestAttributes#addVisitorClasses(Class[])
     */
    @Override
    @Deprecated
    public void setVisitors(Set<Visitor> visitors) {
    }

    /**
     * 获取Visitor
     *
     * @return visitors
     */
    @Override
    public Set<Visitor> getVisitors() {
        return getRunTestAttributes().getVisitors();
    }

    /**
     * 获取当前测试class对象
     *
     * @return testClass
     */
    @Override
    public Class<?> getTestClass() {
        return testClass;
    }

    @Override
    public Object getTestInstance() {
        return testContext.getTestInstance().orElse(this);
    }

    @Override
    public RunTestAttributes getRunTestAttributes() {
        return testContext.getRunTestAttributes();
    }

    /**
     * 获取 TestContext
     *
     * @return TestContext实例
     */
    public TestContext getTestContext() {
        return testContext;
    }
}
