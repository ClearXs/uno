package cc.allio.uno.sequential.dispatch;

import cc.allio.uno.core.util.BeanUtils;
import cc.allio.uno.sequential.TestComposeSequential;
import cc.allio.uno.sequential.TestSequential;
import cc.allio.uno.sequnetial.Sequential;
import cc.allio.uno.sequnetial.SubscriptionProperties;
import cc.allio.uno.sequnetial.SubscriptionPropertiesTypeManager;
import cc.allio.uno.sequnetial.bus.SequentialEventBus;
import cc.allio.uno.sequnetial.dispatch.DispatchDispatcher;
import cc.allio.uno.sequnetial.dispatch.Dispatcher;
import cc.allio.uno.sequnetial.process.DefaultProcessor;
import cc.allio.uno.test.RunTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@RunTest(components = {DefaultProcessor.class, SequentialEventBus.class, BeanUtils.class, SubscriptionProperties.class, SubscriptionPropertiesTypeManager.class})
@Slf4j
class DispatchDispatcherTest {

    DispatchDispatcher dispatcher = new DispatchDispatcher(new Dispatcher() {
        @Override
        public void dispatch(Sequential sequential) throws Throwable {
            log.info("dispatch sequential: {}", sequential);
        }

        @Override
        public Predicate<? extends Sequential> isAssign() {
            return (Predicate<Sequential>) sequential -> true;
        }
    });
    ;

    @Test
    void testDispatch() {
        assertDoesNotThrow(() -> dispatcher.dispatch(new TestSequential()));
    }

    @Test
    void testComposeDispatch() {
        assertDoesNotThrow(() -> dispatcher.dispatch(new TestComposeSequential()));
    }


}
