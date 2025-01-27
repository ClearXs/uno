package cc.allio.uno.sequential.bus;

import cc.allio.uno.core.bus.Topic;
import cc.allio.uno.core.bus.TopicKey;
import cc.allio.uno.core.bus.event.Node;
import cc.allio.uno.sequential.TestSequential;
import cc.allio.uno.sequnetial.SubscriptionProperties;
import cc.allio.uno.sequnetial.bus.SequentialEventBus;
import cc.allio.uno.sequnetial.context.DefaultSequentialContext;
import cc.allio.uno.sequnetial.context.SequentialContext;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * 测试时序事件总线
 *
 * @author j.x
 * @see SequentialEventBus
 * @since 1.0
 */
@RunTest(components = {SequentialEventBus.class, SubscriptionProperties.class})
class SequentialEventBusTest {
    Node<SequentialContext> mockNode;
    DefaultSequentialContext context;

    @Inject
    private SequentialEventBus eventBus;

    @BeforeEach
    protected void onInitSpringEnv() throws Throwable {
        Topic<SequentialContext> topic = new Topic<>(TopicKey.of("test"), eventBus, null);
        mockNode = Mockito.mock(Node.class);
        Mockito.when(mockNode.getSubscribeId()).thenReturn(1L);
        Mockito.when(mockNode.getTopic()).thenReturn(topic);
        context = new DefaultSequentialContext(new TestSequential(), Collections.emptyMap());
    }

    @Test
    void testPublish() {
        assertDoesNotThrow(() -> eventBus.publish("test", context));
    }
}
