package cc.allio.uno.core.function;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.function.lambda.ThrowingMethodBiConsumer;
import cc.allio.uno.core.function.lambda.ThrowingMethodVoid;
import org.junit.jupiter.api.Test;

public class ThrowingFunctionTest extends BaseTestCase {

    @Test
    void testDemoThrowing() {
        Demo demo = new Demo() {
            @Override
            public void f1(String s1, String s2) throws Throwable {

            }

            @Override
            public void f2() {

            }
        };

        ThrowingMethodBiConsumer<String, String> throwing = demo::f1;
        String m1 = throwing.getMethodName();
        assertEquals("f1", m1);

        ThrowingMethodVoid throwing2 = demo::f2;
        String m2 = throwing2.getMethodName();
        assertEquals("f2", m2);
    }

    public interface Demo {

        void f1(String s1, String s2) throws Throwable;

        void f2();
    }
}
