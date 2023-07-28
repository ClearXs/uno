package cc.allio.uno.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@RunTest(listeners = LifecycleListener.class)
public class UnoExtensionTest {

    @BeforeAll
    static void testBefore() {

    }

    @BeforeEach
    void testEach() {

    }

    @Test
    void test(@Parameter CoreTest coreTest) {
        Assertions.assertNotNull(coreTest);
    }

}
