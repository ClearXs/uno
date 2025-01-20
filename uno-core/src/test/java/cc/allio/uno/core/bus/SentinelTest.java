package cc.allio.uno.core.bus;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.bus.event.ReactiveNode;
import org.junit.jupiter.api.Test;

public class SentinelTest extends BaseTestCase {

    String path = "1";
    Topic<EventContext> topic = new Topic<>(path, EventBusFactory.current());

    @Test
    void testFindNode() {
        Sentinel<EventContext> sentinel = topic.getSentinel();
        ReactiveNode<EventContext> node1 = new ReactiveNode<>(1L, topic);
        sentinel.attach(node1);

        boolean present = sentinel.findNode(1L).isPresent();
        assertTrue(present);
    }

    @Test
    void testDetach() {
        Sentinel<EventContext> sentinel = topic.getSentinel();
        ReactiveNode<EventContext> node1 = new ReactiveNode<>(1L, topic);
        sentinel.attach(node1);

        sentinel.detach(1L);

        boolean empty = sentinel.findNode(1L).isEmpty();
        assertTrue(empty);
    }

    @Test
    void testCompletion() {
        Sentinel<EventContext> sentinel = topic.getSentinel();
        ReactiveNode<EventContext> node1 = new ReactiveNode<>(1L, topic);
        sentinel.attach(node1);

        // trigger

        sentinel.trigger(new TestMessageContext());

        boolean complete = sentinel.complete(1L);

        assertTrue(complete);
    }

    @Test
    void testMultiWorkCompletion() {
        Sentinel<EventContext> sentinel = topic.getSentinel();
        ReactiveNode<EventContext> node1 = new ReactiveNode<>(1L, topic);
        sentinel.attach(node1);

        // add work 1
        sentinel.trigger(new TestMessageContext());
        // add work 2
        sentinel.trigger(new TestMessageContext());

        boolean complete = sentinel.complete(1L);

        assertTrue(complete);
    }

}
