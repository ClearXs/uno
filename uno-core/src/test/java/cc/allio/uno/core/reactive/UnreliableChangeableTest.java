package cc.allio.uno.core.reactive;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;


class UnreliableChangeableTest extends BaseTestCase {
    @Test
    void testUnreliableChangeable() {
        UnreliableChangeable<String> changeable = UnreliableChangeablePool.request("test");
        changeable.publish("test");
        changeable = UnreliableChangeablePool.request("test");
        assertFalse(changeable.get());
        changeable.publish("test");
        changeable = UnreliableChangeablePool.request("test");
        assertFalse(changeable.get());
        changeable.publish("test1");
        assertTrue(changeable.get());
    }
}
