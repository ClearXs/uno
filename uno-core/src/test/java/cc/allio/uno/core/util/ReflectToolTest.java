package cc.allio.uno.core.util;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;

public class ReflectToolTest extends BaseTestCase {

    @Test
    void testSingleGenericType() {
        Class<?> genericType1 = ReflectTool.getGenericType(new AImpl(), A.class);

        assertEquals(String.class, genericType1);

        Class<?> genericType2 = ReflectTool.getGenericType(new Object(), Object.class);
        assertNull(genericType2);


        Class<?> genericType3 = ReflectTool.getGenericType(new BImpl(), B.class, 1);
        assertEquals(Integer.class, genericType3);
    }


    interface A<T> {
    }

    interface B<T1, T2> {
    }

    static class AImpl implements A<String> {
    }

    static class BImpl implements B<String, Integer> {
    }

}
