package cc.allio.uno.test;

/**
 * 对CoreTest进行注册
 *
 * @author jiangwei
 * @date 2023/3/2 20:37
 * @since 1.1.4
 */
public class CoreTestListener implements TestListener {

    @Override
    public void beforeEntryMethod(TestContext testContext) {
        testContext.getCoreTest().setup();
    }

    @Override
    public void afterEntryMethod(TestContext testContext) {
        testContext.getCoreTest().tearDown();
    }
}
