package cc.allio.uno.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LifecycleListener implements TestListener {

    @Override
    public void beforeEntryClass(TestContext testContext) {
        log.info("beforeEntryClass");
    }

    @Override
    public void afterEntryClass(TestContext testContext) {
        log.info("afterEntryClass");
    }

    @Override
    public void prepareTestInstance(TestContext testContext) {
        log.info("prepareTestInstance");
    }

    @Override
    public void beforeEntryMethod(TestContext testContext) {
        log.info("beforeEntryMethod");
    }

    @Override
    public void afterEntryMethod(TestContext testContext) {
        log.info("afterEntryMethod");
    }

    @Override
    public void beforeTestExecution(TestContext testContext) {
        log.info("beforeTestExecution");
    }

    @Override
    public void afterTestExecution(TestContext testContext) {
        log.info("afterTestExecution");
    }
}
