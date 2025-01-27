package cc.allio.uno.core.bus;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.bus.event.ReactiveNode;
import org.junit.jupiter.api.Test;

public class SentinelTest extends BaseTestCase {

    String path = "1";
    Topic<EventContext> topic = new Topic<>(TopicKey.of(path), EventBusFactory.current(), null);
    Sentinel<EventContext> sentinel = topic.getSentinel();

    @Test
    void testFindNode() {
        ReactiveNode<EventContext> node1 = new ReactiveNode<>(1L, topic);
        sentinel.attach(node1);

        boolean present = sentinel.findNode(1L).isPresent();
        assertTrue(present);
    }

    @Test
    void testAttach() {
        ReactiveNode<EventContext> node1 = new ReactiveNode<>(1L, topic);

        assertTrue(() -> sentinel.attach(node1));

        assertFalse(() -> sentinel.attach(node1));
    }

    @Test
    void testDetach() {
        ReactiveNode<EventContext> node1 = new ReactiveNode<>(1L, topic);
        sentinel.attach(node1);

        assertTrue(() -> sentinel.detach(1L));

        boolean empty = sentinel.findNode(1L).isEmpty();
        assertTrue(empty);
    }

    @Test
    void testCompletion() {
        ReactiveNode<EventContext> node1 = new ReactiveNode<>(1L, topic);
        sentinel.attach(node1);

        // trigger

        sentinel.trigger(new TestMessageContext());

        boolean complete = sentinel.complete(1L);

        assertTrue(complete);

    }

    @Test
    void testMultiWorkCompletion() {
        ReactiveNode<EventContext> node1 = new ReactiveNode<>(1L, topic);
        sentinel.attach(node1);

        // add work 1
        sentinel.trigger(new TestMessageContext());
        // add work 2
        sentinel.trigger(new TestMessageContext());

        boolean complete = sentinel.complete(1L);

        assertTrue(complete);
    }

    @Test
    void testMultiNodeSingleWorker() {
        ReactiveNode<EventContext> node1 = new ReactiveNode<>(1L, topic);
        ReactiveNode<EventContext> node2 = new ReactiveNode<>(2L, topic);
        ReactiveNode<EventContext> node3 = new ReactiveNode<>(3L, topic);


        sentinel.attach(node1);
        sentinel.attach(node2);
        sentinel.attach(node3);

        sentinel.trigger(new TestMessageContext());

        assertFalse(() -> sentinel.complete(1L));
        assertFalse(() -> sentinel.complete(2L));
        assertTrue(() -> sentinel.complete(3L));

        assertFalse(() -> sentinel.complete(1L));

    }

    @Test
    void testMultiNodeAndMultiWorker() {
        ReactiveNode<EventContext> node1 = new ReactiveNode<>(1L, topic);
        ReactiveNode<EventContext> node2 = new ReactiveNode<>(2L, topic);
        ReactiveNode<EventContext> node3 = new ReactiveNode<>(3L, topic);

        sentinel.attach(node1);
        sentinel.attach(node2);
        sentinel.attach(node3);


        sentinel.trigger(new TestMessageContext());
        sentinel.trigger(new TestMessageContext());
        sentinel.trigger(new TestMessageContext());


        assertFalse(() -> sentinel.complete(1L));
        assertFalse(() -> sentinel.complete(2L));
        assertTrue(() -> sentinel.complete(3L));

        assertFalse(() -> sentinel.complete(3L));
    }

    @Test
    void testMultiNodesDetach() {
        ReactiveNode<EventContext> node1 = new ReactiveNode<>(1L, topic);
        ReactiveNode<EventContext> node2 = new ReactiveNode<>(2L, topic);
        ReactiveNode<EventContext> node3 = new ReactiveNode<>(3L, topic);

        sentinel.attach(node1);
        sentinel.attach(node2);
        sentinel.attach(node3);

        sentinel.trigger(new TestMessageContext());
        sentinel.trigger(new TestMessageContext());

        assertFalse(() -> sentinel.complete(1L));
        assertFalse(() -> sentinel.complete(2L));

        sentinel.detach(node3.getSubscribeId());
        assertTrue(() -> sentinel.complete(2L));

    }

    @Test
    void testIdlingWorker() {
        sentinel.trigger(new TestMessageContext());
        assertTrue(() -> sentinel.complete(null));

    }
}
