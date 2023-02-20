package cc.allio.uno.component.sequential.dispatch;

import cc.allio.uno.component.sequential.TestComposeSequential;
import cc.allio.uno.component.sequential.bus.SequentialMessageBus;
import cc.allio.uno.core.bus.SubscriptionProperties;
import cc.allio.uno.component.sequential.process.SpringProcessor;
import cc.allio.uno.component.sequential.Sequential;
import cc.allio.uno.component.sequential.TestSequential;
import cc.allio.uno.core.util.CoreBeanUtil;
import cc.allio.uno.test.BaseCoreTest;
import cc.allio.uno.test.env.TestSpringEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

@Slf4j
class DispatchDispatcherTest extends BaseCoreTest {

    DispatchDispatcher dispatcher;

    @Override
    protected void onEnvBuild() {
        registerComponent(SpringProcessor.class, SequentialMessageBus.class, CoreBeanUtil.class, SubscriptionProperties.class);
    }

    @Test
    void testDispatch() {
        assertDoesNotThrow(() -> dispatcher.dispatch(new TestSequential()));
    }

    @Test
    void testComposeDispatch() {
        assertDoesNotThrow(() -> dispatcher.dispatch(new TestComposeSequential()));
    }

    @Override
    public TestSpringEnvironment supportEnv() {
        return null;
    }

    @Override
    protected void onRefreshComplete() throws Throwable {
        dispatcher = new DispatchDispatcher(new Dispatcher() {
            @Override
            public void dispatch(Sequential sequential) throws Throwable {
                log.info("dispatch sequential: {}", sequential);
            }

            @Override
            public Predicate<? extends Sequential> isAssign() {
                return (Predicate<Sequential>) sequential -> true;
            }
        });
    }

    @Override
    protected void onContextClose() throws Throwable {

    }
}
