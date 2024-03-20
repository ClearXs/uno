package cc.allio.uno.core.util;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.reflect.Instantiation;
import cc.allio.uno.core.reflect.InstantiationBuilder;
import cc.allio.uno.core.reflect.InstantiationFeature;
import com.google.common.collect.Maps;
import jakarta.annotation.Priority;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Slf4j
class ClassUtilTest extends BaseTestCase {

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
        Instantiation<Object> build = InstantiationBuilder.builder().addOneInstanceClass(Demo1.class).build();
        build.addFeature(InstantiationFeature.callback(System.out::println));
        Object one = build.createOne();
        assertNotNull(one);
    }

    @Test
    void testDeDuplicateInstance() {
        Instantiation<DemoParent> instantiation = InstantiationBuilder.<DemoParent>builder()
                .addMultiForInstanceClasses(new Class[]{Demo2.class, Demo2.class, Demo2.class})
                .build();
        List<DemoParent> demoParents = instantiation.create();
        assertEquals(3, demoParents.size());
        instantiation.addFeature(InstantiationFeature.deduplicate());
        demoParents = instantiation.create();
        assertEquals(1, demoParents.size());
    }

    @Test
    void testSortInstance() {
        Instantiation<DemoParent> build = InstantiationBuilder.<DemoParent>builder()
                .addMultiForInstanceClasses(new Class[]{Demo2.class, Demo1.class})
                .build();
        build.addFeature(InstantiationFeature.sort());
        List<DemoParent> demoParents = build.create();
        DemoParent demoParent = demoParents.get(0);
        assertNotNull(demoParent);
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
