package cc.allio.uno.test;

import cc.allio.uno.core.api.OptionalContext;
import com.google.common.collect.Maps;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.*;

/**
 * test-context
 *
 * @author jiangwei
 * @date 2023/3/2 19:24
 * @since 1.1.4
 */
public class TestContext implements OptionalContext {

    /**
     * Key常量
     */
    public static final String TEST_CLASS = "cc.allio.uno.test.TestClass";
    public static final String TEST_INSTANCE = "cc.allio.uno.test.TestInstance";
    public static final String TEST_METHOD = "cc.allio.uno.test.TestMethod";
    // 标识web server的类型，包含有三个值: none、servlet、reactive
    public static final String WEB_SERVER = "cc.allio.uno.test.WebServer";

    // 缓存实例对象
    public final Map<String, Optional<Object>> store;
    // 记录测试阶段
    private final ThreadLocal<String> stage;

    public TestContext(Class<?> testClass) {
        this.store = Maps.newConcurrentMap();
        putAttribute(TEST_CLASS, testClass);
        putAttribute(CoreTest.class.getName(), new ExecutableCoreTest(testClass, this));
        putAttribute(RunTestAttributes.class.getName(), new RunTestAttributes(testClass));
        this.stage = ThreadLocal.withInitial(() -> null);
    }

    /**
     * 根据指定的type获取Object对象
     *
     * @param type class type
     * @return Optional
     */
    public Object get(Class<?> type) {
        return store.get(type.getName())
                .orElseGet(() -> {
                    try {
                        return getCoreTest().getBean(type);
                    } catch (Throwable ex) {
                        return null;
                    }
                });
    }

    @Override
    public Optional<Object> get(String key) {
        return store.getOrDefault(key, Optional.empty());
    }

    @Override
    public void putAttribute(String key, Object obj) {
        store.put(key, Optional.ofNullable(obj));
    }

    @Override
    public Optional<ApplicationContext> getApplicationContext() {
        return get(CoreTest.class.getName(), CoreTest.class).map(CoreTest::getContext);
    }

    @Override
    public Map<String, Object> getAll() {
        return (Map<String, Object>) stage;
    }

    public void setTestInstance(Object testInstance) {
        putAttribute(TEST_INSTANCE, testInstance);
    }

    public Optional<Object> getTestInstance() {
        return get(TEST_INSTANCE);
    }

    public void setTestMethod(Method testMethod) {
        putAttribute(TEST_METHOD, testMethod);
    }

    public Optional<Method> getTestMethod() {
        return get(TEST_METHOD, Method.class);
    }

    /**
     * 获取CoreTest实例
     *
     * @return CoreTest
     */
    public CoreTest getCoreTest() {
        return get(CoreTest.class.getName(), CoreTest.class).orElseThrow(() -> new IllegalArgumentException("nonexistence CoreTest"));
    }

    /**
     * 获取TestClass
     *
     * @return TestClass
     */
    public Class<?> getTestClass() {
        return get(TEST_CLASS, Class.class).orElseThrow(() -> new IllegalArgumentException("nonexistence TestClass"));
    }

    /**
     * 设置测试阶段
     *
     * @param stage 阶段值
     */
    public void setStage(String stage) {
        this.stage.remove();
        this.stage.set(stage);
    }

    /**
     * 获取测试阶段值
     *
     * @return 阶段值
     */
    public String getStage() {
        return stage.get();
    }

    /**
     * 获取注解{@link RunTest}的属性数据
     *
     * @return RunTestAttributes instance
     */
    public RunTestAttributes getRunTestAttributes() {
        return get(RunTestAttributes.class.getName(), RunTestAttributes.class).orElseThrow(() -> new IllegalArgumentException("nonexistence RunTestAttributes"));
    }
}
