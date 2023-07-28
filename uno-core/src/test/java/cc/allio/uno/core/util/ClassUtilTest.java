package cc.allio.uno.core.util;

import cc.allio.uno.core.BaseTestCase;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Priority;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Slf4j
class ClassUtilTest extends BaseTestCase {
    @Override
    protected void onInit() throws Throwable {

    }

    /**
     * Test Case: 测试方法上泛形
     */
    @Test
    void testMethodGeneric() {
        Generic<String> generic = new Generic<>();
        Method returnMap = ClassUtils.getMethod(generic.getClass(), "returnMap");
        Class<?> returnType = returnMap.getReturnType();
        Map<String, String> stringStringMap = generic.returnMap();
        log.info(returnType.getSimpleName());
    }

    @Override
    protected void onDown() throws Throwable {

    }

    static class Generic<T> {

        public <K, V> Map<K, V> returnMap() {
            return Maps.newHashMap();
        }
    }


    // 实例化
    @Test
    void testOneInstance() {
        DemoParent demoParent = ClassUtils.newInstance(DemoParent.class);
        assertNotNull(demoParent);
    }

    @Test
    void testParametersInstance() {
        DemoParent demoParent = ClassUtils.newInstance(DemoParent.class, "123");
        assertNotNull(demoParent);
        assertEquals("123", demoParent.name);
    }

    @Test
    void testParametersInstanceIsNull() {
        DemoParent demoParent = ClassUtils.newInstance(DemoParent.class, "123", 123);
        assertNotNull(demoParent);
        assertNull(demoParent.name);
    }

    @Test
    void testNewInstanceList() {
        List<DemoParent> instanceList = ClassUtils.newInstanceListExcludeNull(DemoParent.class, Demo1.class, Demo2.class);
        assertEquals(3, instanceList.size());
    }

    @Test
    void testAbstractClassInstance() {
        AbstractDemo abstractDemo = ClassUtils.newInstance(AbstractDemo.class);
        assertNull(abstractDemo);
    }

    @Test
    void testAddFeature() {
        ClassUtils.Instantiation<Object> build = ClassUtils.instantiationBuilder()
                .addOneInstanceClass(Demo1.class)
                .build();
        build.addFeature(new ClassUtils.CallbackFeature<>(System.out::println));
        Object one = build.createOne();

    }

    @Test
    void testDeDuplicateInstance() {
        ClassUtils.Instantiation<DemoParent> instantiation = ClassUtils.<DemoParent>instantiationBuilder()
                .addMultiForInstanceClasses(new Class[]{Demo2.class, Demo2.class, Demo2.class})
                .build();
        List<DemoParent> demoParents = instantiation.create();
        assertEquals(3, demoParents.size());
        instantiation.addFeature(new ClassUtils.DeDuplicationFeature<>());
        demoParents = instantiation.create();
        assertEquals(1, demoParents.size());
    }

    @Test
    void testSortInstance() {
        ClassUtils.Instantiation<DemoParent> build = ClassUtils.<DemoParent>instantiationBuilder()
                .addMultiForInstanceClasses(new Class[]{Demo2.class, Demo1.class})
                .build();
        build.addFeature(new ClassUtils.SortFeature<>());
        List<DemoParent> demoParents = build.create();
        DemoParent demoParent = demoParents.get(0);
    }


    @AllArgsConstructor
    @NoArgsConstructor
    public static class DemoParent {
        private String name;
    }

    @Priority(1)
    public static class Demo1 extends DemoParent {

    }

    @Priority(2)
    public static class Demo2 extends DemoParent {

    }

    public abstract static class AbstractDemo extends Demo1 {

    }
}
