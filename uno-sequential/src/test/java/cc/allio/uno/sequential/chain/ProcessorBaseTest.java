package cc.allio.uno.sequential.chain;

import cc.allio.uno.core.type.DefaultType;
import cc.allio.uno.core.type.TypeManager;
import cc.allio.uno.sequential.*;
import cc.allio.uno.sequnetial.SubscriptionProperties;
import cc.allio.uno.sequnetial.SubscriptionPropertiesTypeManager;
import cc.allio.uno.sequnetial.bus.SequentialEventBus;
import cc.allio.uno.sequnetial.context.DefaultSequentialContext;
import cc.allio.uno.sequnetial.context.SequentialContext;
import cc.allio.uno.sequnetial.process.DefaultProcessor;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Slf4j
@RunTest(components = {DefaultProcessor.class, SequentialEventBus.class, SubscriptionProperties.class, SubscriptionPropertiesTypeManager.class})
class ProcessorBaseTest {

    @Inject
    private DefaultProcessor processor;
    @Inject
    private SequentialEventBus eventBus;
    @Inject
    private TypeManager typeManager;

    @Test
    void testProcess() {
        SequentialContext context = new DefaultSequentialContext(new TestSequential(), Collections.emptyMap());
        assertDoesNotThrow(() -> {
            processor.process(context);
        });
    }

    @Test
    void testSequentialTypeIsNull() {
        SequentialContext context = new DefaultSequentialContext(new NullTypeSequential(), Collections.emptyMap());
        assertDoesNotThrow(() -> processor.process(context));
    }

    @Test
    void testAlternateExecute() {
        SequentialContext testContext = new DefaultSequentialContext(new TestSequential(), Collections.emptyMap());
        SequentialContext unknownContext = new DefaultSequentialContext(new UnknownSequential(), Collections.emptyMap());
        typeManager.add(DefaultType.of("test"));

        assertDoesNotThrow(() -> {
            processor.process(testContext);
            processor.process(unknownContext);
            processor.process(testContext);
            Thread.sleep(1000);
        });
    }

    @Test
    void testWildcardTypeHandler() {
        SequentialContext testContext = new DefaultSequentialContext(new TestSequential(), Collections.emptyMap());
        typeManager.add(DefaultType.of("test"));
        assertDoesNotThrow(() -> {
            processor.process(testContext);
            Thread.sleep(1000);
        });
    }

    @Test
    void testComposeHandler() {
        SequentialContext composeContext = new DefaultSequentialContext(new TestComposeSequential(TestSequential.class, UnknownSequential.class), Collections.emptyMap());
        assertDoesNotThrow(() -> processor.process(composeContext));
    }

    @Test
    void testLifecycle() {
        SequentialContext lifeCycleContext = new DefaultSequentialContext(new LifeCycleSequential(), Collections.emptyMap());
        assertDoesNotThrow(() -> processor.process(lifeCycleContext));
    }

}
