package cc.allio.uno.test;

/**
 * 通过Log打印测试花费事件
 *
 * @author jiangwei
 * @date 2023/3/2 20:40
 * @since 1.1.4
 */
public class PrintTimingListener implements TestListener {

    @Override
    public void beforeTestExecution(TestContext testContext) {
        testContext.getCoreTest().timing();
    }

    @Override
    public void afterTestExecution(TestContext testContext) {
        testContext.getCoreTest().stopTiming();
    }
}
