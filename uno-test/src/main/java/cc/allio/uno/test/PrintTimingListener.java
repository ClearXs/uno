package cc.allio.uno.test;

import cc.allio.uno.test.listener.Listener;

/**
 * 通过Log打印测试花费事件
 *
 * @author j.x
 * @since 1.1.4
 */
public class PrintTimingListener implements Listener {

    @Override
    public void beforeTestExecution(TestContext testContext) {
        testContext.getCoreTest().timing();
    }

    @Override
    public void afterTestExecution(TestContext testContext) {
        testContext.getCoreTest().stopTiming();
    }
}
