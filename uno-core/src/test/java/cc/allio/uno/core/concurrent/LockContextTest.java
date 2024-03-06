package cc.allio.uno.core.concurrent;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;

public class LockContextTest extends BaseTestCase {

    @Test
    void testLockReturn() {
        String numOfOne = LockContext.lock()
                .lockReturn(() -> "1")
                .unchecked();
        assertEquals("1", numOfOne);

        assertThrows(
                ArithmeticException.class,
                () -> LockContext.lock()
                        .lockReturn(() -> 1 / 0)
                        .except(ArithmeticException.class));

        var except = LockContext.lock()
                .lockReturn(() -> 1 / 0)
                .unwrapOrElse(err -> {
                    assertEquals(1, err.size());
                    ArithmeticException checkedException = err.findCheckedException(ArithmeticException.class);
                    assertNotNull(checkedException);
                });
        assertNull(except);
    }

    @Test
    void testMultiOperation() {
        String unchecked = LockContext.lock()
                .then(context -> {
                    System.out.println("1");
                })
                .thenApply(context -> "1")
                .<String>release()
                .unchecked();
        assertEquals("1", unchecked);
    }
}
