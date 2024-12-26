package cc.allio.uno.test;

import cc.allio.uno.test.env.Visitor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * 不更改原先{@link CoreTest}的基础上，重写并增加具有{@link TestContext}等的相关方法
 *
 * @author j.x
 * @since 1.1.4
 */
@Slf4j
public class ExecutableCoreTest extends CoreTest {

    private final Class<?> testClass;
    @Getter
    private final TestContext testContext;
    @Getter
    private final RunTestAttributes runTestAttributes;

    public ExecutableCoreTest(Class<?> testClass, TestContext testContext, RunTestAttributes runTestAttributes) {
        this.testClass = testClass;
        this.testContext = testContext;
        this.runTestAttributes = runTestAttributes;
        setCoreRunner(runTestAttributes.getCoreRunner());
        setVisitors(runTestAttributes.getVisitors());
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

    /**
     * obtain test instance
     */
    public Object getTestInstance() {
        return testContext.getTestInstance().orElse(this);
    }
}
