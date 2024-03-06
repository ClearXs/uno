package cc.allio.uno.sequential.bus;

import cc.allio.uno.core.bus.Subscription;
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
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * 测试时序事件总线
 *
 * @author jiangwei
 * @date 2022/2/7 11:18
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
        mockNode = Mockito.mock(Node.class);
        Mockito.when(mockNode.getSubscribeId()).thenReturn(1L);
        Mockito.when(mockNode.getTopic()).thenReturn("test");
        context = new DefaultSequentialContext(new TestSequential(), Collections.emptyMap());
    }

    /**
     * Test Case: 测试订阅的Node数据是否正确
     */
    @Test
    void testSubscribeWhetherRight() {
        Flux<Node<SequentialContext>> node = eventBus.subscribe(Subscription.of(1L, "test"));
        node.map(Node::getSubscribeId)
                .as(StepVerifier::create)
                .expectNext(mockNode.getSubscribeId())
                .verifyComplete();
        node.map(Node::getTopic)
                .as(StepVerifier::create)
                .expectNext(mockNode.getTopic())
                .verifyComplete();
    }

    @Test
    void testPublish() {
        assertDoesNotThrow(() -> eventBus.publish("test", context));
    }
}
