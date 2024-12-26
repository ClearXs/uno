package cc.allio.uno.test.testcontainers;

import cc.allio.uno.test.CoreTest;
import cc.allio.uno.test.runner.CloseRunner;

/**
 * end test-container
 *
 * @author j.x
 * @since 1.1.7
 */
public class ShutdownContainer implements CloseRunner {

    @Override
    public void onClose(CoreTest coreTest) throws Throwable {
        Container container = coreTest.getContainer();
        if (container != null) {
            container.shutdown();
        }
    }
}
