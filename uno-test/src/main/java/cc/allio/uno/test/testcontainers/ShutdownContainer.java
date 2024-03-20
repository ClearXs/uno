package cc.allio.uno.test.testcontainers;

import cc.allio.uno.test.CoreTest;
import cc.allio.uno.test.runner.CloseRunner;

/**
 * stop test-container
 *
 * @author j.x
 * @date 2024/3/18 23:32
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
