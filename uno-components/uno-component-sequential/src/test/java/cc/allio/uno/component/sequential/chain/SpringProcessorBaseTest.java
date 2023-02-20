package cc.allio.uno.component.sequential.chain;

import cc.allio.uno.uno.component.sequential.*;
import cc.allio.uno.component.sequential.context.DefaultSequentialContext;
import cc.allio.uno.component.sequential.context.SequentialContext;
import cc.allio.uno.component.sequential.bus.SequentialMessageBus;
import cc.allio.uno.core.bus.SubscriptionProperties;
import cc.allio.uno.component.sequential.process.SpringProcessor;
import cc.allio.uno.test.BaseSpringTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class SpringProcessorBaseTest extends BaseSpringTest {

    @Override
    protected void onInitSpringEnv() throws Throwable {
        registerComponent(SpringProcessor.class, SequentialMessageBus.class, SubscriptionProperties.class);
    }

    @Override
    protected void onRefreshComplete() throws Throwable {

    }

    @Override
    protected void onContextClose() throws Throwable {

    }

    @Test
    void testProcess() {
        SpringProcessor processor = getBean(SpringProcessor.class);
        SequentialMessageBus bus = getBean(SequentialMessageBus.class);
        SequentialContext context = new DefaultSequentialContext(new TestSequential(), null, bus);
        assertDoesNotThrow(() -> {
            processor.process(context);
        });
    }

    @Test
    void testSequentialIsNull() {
        SequentialMessageBus bus = getBean(SequentialMessageBus.class);
        assertThrows(NullPointerException.class, () -> {
            new DefaultSequentialContext(null, null, bus);
        });
    }

    @Test
    void testSequentialTypeIsNull() {
        SpringProcessor processor = getBean(SpringProcessor.class);
        SequentialMessageBus bus = getBean(SequentialMessageBus.class);
        SequentialContext context = new DefaultSequentialContext(new NullTypeSequential(), null, bus);
        assertDoesNotThrow(() -> processor.process(context));
    }

    @Test
    void testAlternateExecute() {
        SpringProcessor processor = getBean(SpringProcessor.class);
        SequentialMessageBus bus = getBean(SequentialMessageBus.class);
        SequentialContext testContext = new DefaultSequentialContext(new TestSequential(), null, bus);
        SequentialContext unknownContext = new DefaultSequentialContext(new UnknownSequential(), null, bus);

        assertDoesNotThrow(() -> {
            processor.process(testContext);
            processor.process(unknownContext);
            processor.process(testContext);
        });
    }

    @Test
    void testComposeHandler() {
        SpringProcessor processor = getBean(SpringProcessor.class);
        SequentialMessageBus bus = getBean(SequentialMessageBus.class);
        SequentialContext composeContext = new DefaultSequentialContext(new TestComposeSequential(TestSequential.class, UnknownSequential.class), null, bus);
        assertDoesNotThrow(() -> processor.process(composeContext));
    }

    @Test
    void testLifecycle() {
        SpringProcessor processor = getBean(SpringProcessor.class);
        SequentialMessageBus bus = getBean(SequentialMessageBus.class);
        SequentialContext lifeCycleContext = new DefaultSequentialContext(new LifeCycleSequential(), null, bus);
        assertDoesNotThrow(() -> processor.process(lifeCycleContext));
    }

    @Override
    protected void onDown() {

    }
}
