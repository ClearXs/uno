package cc.allio.uno.core.util;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;

public class ReflectToolTest extends BaseTestCase {

    @Test
    void testGenericType() {

        // 接口测试
        Class<?> genericType1 = ReflectTool.getGenericType(new AImpl(), A.class);

        assertEquals(String.class, genericType1);

        Class<?> genericType2 = ReflectTool.getGenericType(new Object(), Object.class);
        assertNull(genericType2);


        Class<?> genericType3 = ReflectTool.getGenericType(new BImpl(), B.class, 1);
        assertEquals(Integer.class, genericType3);

        // 类测试
        Class<?> genericType4 = ReflectTool.getGenericType(new CImpl(), C.class);
        assertEquals(String.class, genericType4);

        Class<?> genericType5 = ReflectTool.getGenericType(new AAPenetrateImpl(), A.class);
        assertEquals(String.class, genericType5);
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
}
