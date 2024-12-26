package cc.allio.uno.test;

import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Uno 集成于Junit5。
 * <p>生命周期：</p>
 * <i>beforeAll -> postProcessTestInstance -> beforeEach -> beforeTestExecution -> afterTestExecution -> afterEach -> afterAll</i>
 *
 * @author j.x
 * @see SpringExtension
 * @since 1.1.4
 */
public class UnoExtension implements BeforeAllCallback, AfterAllCallback, TestInstancePostProcessor,
        BeforeEachCallback, AfterEachCallback, BeforeTestExecutionCallback,
        AfterTestExecutionCallback, ParameterResolver {

    private static final ExtensionContext.Namespace UNO_TEST_NAMESPACE = ExtensionContext.Namespace.create(UnoExtension.class);

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        getTestManager(context).afterEntryClass();
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Object testInstance = context.getRequiredTestInstance();
        Method testMethod = context.getRequiredTestMethod();
        Throwable testException = context.getExecutionException().orElse(null);
        getTestManager(context).afterEntryMethod(testInstance, testMethod, testException);
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        getTestManager(context).beforeEntryClass();
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Object testInstance = context.getRequiredTestInstance();
        Method testMethod = context.getRequiredTestMethod();
        getTestManager(context).beforeEntryMethod(testInstance, testMethod);
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        getTestManager(context).prepareTestInstance(testInstance);

    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        Object testInstance = context.getRequiredTestInstance();
        Method testMethod = context.getRequiredTestMethod();
        Throwable testException = context.getExecutionException().orElse(null);
        getTestManager(context).afterTestExecution(testInstance, testMethod, testException);

    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        Object testInstance = context.getRequiredTestInstance();
        Method testMethod = context.getRequiredTestMethod();
        getTestManager(context).beforeTestExecution(testInstance, testMethod);

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        return parameter.isAnnotationPresent(cc.allio.uno.test.Parameter.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        Class<?> type = parameter.getType();
        return getTestManager(extensionContext).getTestContext().get(type);
    }

    /**
     * 从ExtensionContext中获取 TestManager
     *
     * @param context extensionContext
     * @return TestManager
     */
    public TestManager getTestManager(ExtensionContext context) {
        Class<?> testClass = context.getRequiredTestClass();
        ExtensionContext.Store store = context.getRoot().getStore(UNO_TEST_NAMESPACE);
        return store.getOrComputeIfAbsent(testClass, TestManager::new, TestManager.class);
    }
}
