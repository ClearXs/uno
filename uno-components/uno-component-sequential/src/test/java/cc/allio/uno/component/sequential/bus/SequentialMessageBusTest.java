package cc.allio.uno.component.sequential.bus;

import cc.allio.uno.component.sequential.TestSequential;
import cc.allio.uno.component.sequential.context.DefaultSequentialContext;
import cc.allio.uno.component.sequential.context.SequentialContext;
import cc.allio.uno.core.bus.Subscription;
import cc.allio.uno.core.bus.SubscriptionProperties;
import cc.allio.uno.core.bus.event.EventNode;
import cc.allio.uno.test.BaseSpringTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * 测试时序消息总线
 *
 * @author jiangwei
 * @date 2022/2/7 11:18
 * @see SequentialMessageBus
 * @since 1.0
 */
class SequentialMessageBusTest extends BaseSpringTest {
    EventNode<SequentialContext> mockNode;
    DefaultSequentialContext context;

    @Override
    protected void onInitSpringEnv() throws Throwable {
        registerComponent(SequentialMessageBus.class);
        registerComponent(SubscriptionProperties.class);
        mockNode = Mockito.mock(EventNode.class);
        Mockito.when(mockNode.getSubscribeId()).thenReturn(1L);
        Mockito.when(mockNode.getTopic()).thenReturn("test");
        context = new DefaultSequentialContext(new TestSequential(), null, null);
    }

    @Override
    protected void onRefreshComplete() throws Throwable {

    }

    @Override
    protected void onContextClose() throws Throwable {

    }

    /**
     * Test Case: 测试订阅的Node数据是否正确
     */
    @Test
    void testSubscribeWhetherRight() {
        SequentialMessageBus messageBus = getContext().getBean(SequentialMessageBus.class);
        Flux<EventNode<SequentialContext>> node = messageBus.subscribe(Subscription.of(1L, "test"));
        node.map(EventNode::getSubscribeId)
                .as(StepVerifier::create)
                .expectNext(mockNode.getSubscribeId())
                .verifyComplete();
        node.map(EventNode::getTopic)
                .as(StepVerifier::create)
                .expectNext(mockNode.getTopic())
                .verifyComplete();
    }

    @Test
    void testPublish() {
        SequentialMessageBus messageBus = getContext().getBean(SequentialMessageBus.class);
        assertDoesNotThrow(() -> messageBus.publish("test", context));
    }
}
