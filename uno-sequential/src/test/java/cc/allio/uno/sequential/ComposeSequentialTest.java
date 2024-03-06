package cc.allio.uno.sequential;

import cc.allio.uno.core.proxy.ComposeOrigin;
import cc.allio.uno.sequnetial.Sequential;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class ComposeSequentialTest extends BaseTestCase {

    @Test
    void testProxySequential() {
        TestComposeSequential compose = new TestComposeSequential();
        compose.getCompositeMetadata()
                .forEach(sequential -> assertEquals("test", sequential.getType()));
    }

    @Test
    void testComposeSuperSequential() {
        TestComposeSequential compose = new TestComposeSequential();
        compose.getCompositeMetadata()
                .forEach(sequential -> assertEquals("test-compose", getType(sequential)));
    }

    String getType(@ComposeOrigin Sequential sequential) {
        return sequential.getType().getCode();
    }
}
