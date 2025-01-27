package cc.allio.uno.test;

import cc.allio.uno.core.util.map.OptionalMap;
import com.google.common.collect.Maps;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.*;

/**
 * test-context
 *
 * @author j.x
 * @since 1.1.4
 */
public class TestContext implements OptionalMap<String> {

    /**
     * Key常量
     */
    public static final String TEST_CLASS = "cc.allio.uno.test.TestClass";
    public static final String TEST_INSTANCE = "cc.allio.uno.test.TestInstance";
    public static final String TEST_METHOD = "cc.allio.uno.test.TestMethod";
    // 标识web server的类型，包含有三个值: none、servlet、reactive
    public static final String WEB_SERVER = "cc.allio.uno.test.WebServer";

    // 缓存实例对象
    public final Map<String, Object> store;
    // 记录测试阶段
    private final ThreadLocal<String> stage;

    public TestContext(Class<?> testClass) {
        this.store = Maps.newConcurrentMap();
        put(TEST_CLASS, testClass);
        RunTestAttributes runTestAttributes = new RunTestAttributes(testClass);
        put(RunTestAttributes.class.getName(), runTestAttributes);
        ExecutableCoreTest executableCoreTest = new ExecutableCoreTest(testClass, this, runTestAttributes);
        put(CoreTest.class.getName(), executableCoreTest);
        this.stage = ThreadLocal.withInitial(() -> null);
    }

    /**
     * 根据指定的type获取Object对象
     *
     * @param type class type
     * @return Optional
     */
    public Object get(Class<?> type) {
        Object v = store.get(type.getName());
        if (v == null) {
            try {
                return getCoreTest().getBean(type);
            } catch (Throwable ex) {
                return null;
            }
        }
        return null;
    }

    @Override
    public Optional<Object> get(String key) {
        return Optional.ofNullable(store.get(key));
    }

    @Override
    public void put(String key, Object obj) {
        store.put(key, obj);
    }

    @Override
    public boolean remove(String key) {
        return store.remove(key) != null;
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
        put(TEST_INSTANCE, testInstance);
    }

    public Optional<Object> getTestInstance() {
        return get(TEST_INSTANCE);
    }

    public void setTestMethod(Method testMethod) {
        put(TEST_METHOD, testMethod);
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
        return get(RunTestAttributes.class.getName(), RunTestAttributes.class)
                .orElseThrow(() -> new IllegalArgumentException("nonexistence RunTestAttributes"));
    }
}
