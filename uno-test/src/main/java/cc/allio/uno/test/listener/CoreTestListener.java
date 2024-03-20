package cc.allio.uno.test.listener;

import cc.allio.uno.test.TestContext;

/**
 * 对CoreTest进行注册
 *
 * @author j.x
 * @date 2023/3/2 20:37
 * @since 1.1.4
 */
public class CoreTestListener implements Listener {

    @Override
    public void beforeEntryMethod(TestContext testContext) {
        testContext.getCoreTest().setup();
    }

    @Override
    public void afterEntryMethod(TestContext testContext) {
        testContext.getCoreTest().tearDown();
    }
}
