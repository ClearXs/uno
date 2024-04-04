package cc.allio.uno.core.util;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.reflect.ParameterizedFinder;
import cc.allio.uno.core.reflect.ReflectTools;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

class ReflectToolTest extends BaseTestCase {

    @Test
    void testGenericType() {

        // 接口测试
        Class<?> genericType1 = ReflectTools.getGenericType(new AImpl(), A.class);

        assertEquals(String.class, genericType1);

        Class<?> genericType2 = ReflectTools.getGenericType(new Object(), Object.class);
        assertNull(genericType2);


        Class<?> genericType3 = ReflectTools.getGenericType(new BImpl(), B.class, 1);
        assertEquals(Integer.class, genericType3);

        // 类测试
        Class<?> genericType4 = ReflectTools.getGenericType(new CImpl(), C.class);
        assertEquals(String.class, genericType4);

        Class<?> genericType5 = ReflectTools.getGenericType(new AAPenetrateImpl(), A.class);
        assertEquals(String.class, genericType5);
    }

    @Test
    void testDrawnMethod() throws NoSuchMethodException {
        Method getLongList = GenericMethod.class.getDeclaredMethod("getLongList");
        ParameterizedFinder parameterizedFinder = ReflectTools.drawn(getLongList);
        Class<?> first = parameterizedFinder.findFirst();
        assertNotNull(first);
        assertEquals(Long.class, first);

        Method setLongList = GenericMethod.class.getDeclaredMethod("setLongList", List.class);
        parameterizedFinder = ReflectTools.drawn(setLongList);
        first = parameterizedFinder.findFirst();
        assertNotNull(first);
        assertEquals(Long.class, first);

        Method getSimply = GenericMethod.class.getDeclaredMethod("getSimply");
        parameterizedFinder = ReflectTools.drawn(getSimply);
        first = parameterizedFinder.findFirst();
        assertNull(first);
    }

    @Test
    void testDrawnField() throws NoSuchFieldException {
        Field longList = GenericMethod.class.getDeclaredField("longList");
        ParameterizedFinder parameterizedFinder = ReflectTools.drawn(longList);
        Class<?> first = parameterizedFinder.findFirst();
        assertNotNull(first);
        assertEquals(Long.class, first);

        Field simply = GenericMethod.class.getDeclaredField("simply");
        parameterizedFinder = ReflectTools.drawn(simply);
        first = parameterizedFinder.findFirst();
        assertNull(first);
    }


    interface A<T> {
    }

    interface B<T1, T2> {
    }

    static abstract class C<T1> {

    }

    static class AImpl implements A<String> {
    }

    static class BImpl implements B<String, Integer> {
    }

    static class CImpl extends C<String> {
    }


    interface APenetrate<T> extends A<T> {
    }

    abstract class APenetrateImpl<T> implements APenetrate<T> {
    }

    class AAPenetrateImpl extends APenetrateImpl<String> {
    }

    @Data
    static class GenericMethod {
        private List<Long> longList;

        private Long simply;
    }
}
