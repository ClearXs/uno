package cc.allio.uno.core.reactor;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;


class UnreliableChangeableTest extends BaseTestCase {
    @Override
    protected void onInit() throws Throwable {

    }

    @Test
    void testUnreliableChangeable() throws ExecutionException, InterruptedException {
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

    @Override
    protected void onDown() throws Throwable {

    }
}
