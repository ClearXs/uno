package cc.allio.uno.core.bus;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.bus.event.EmitEvent;
import cc.allio.uno.core.bus.event.Node;
import com.google.common.collect.Lists;
import net.jodah.concurrentunit.Waiter;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EventBusTest extends BaseTestCase {

    private final EventBus<EventContext> bus = new DefaultEventBus();

    @Test
    void testSubscribe() {
        // 获取订阅的node
        bus.subscribe(Subscription.of("1"))
                .map(node -> node.reply(EmitEvent.class, eventContext -> assertEquals("1", eventContext.getTopicPath())))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        // 转换为 event context
        bus.subscribe(Subscription.of("1"))
                .flatMap(Node::onNext)
                .doOnNext(System.out::println)
                .subscribe();

        bus.subscribe(Subscription.of("2"))
                .map(node -> node.reply(EmitEvent.class, eventContext -> assertEquals("2", eventContext.getTopicPath())))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        bus.publish(Subscription.of("1"), new DefaultEventContext(Collections.emptyMap()));
        bus.publish(Subscription.of("2"), new DefaultEventContext(Collections.emptyMap()));
    }

    @Test
    void testNewPublisher() {
        bus.subscribe(Subscription.of("1"))
                .flatMap(Node::onNext)
                .map(o -> "1")
                .doOnNext(System.out::println)
                .subscribe();
        bus.publish("1", new DefaultEventContext());
    }


    @Test
    void testPer() {
        List<String> topics = Lists.newArrayList();
        Flux<String> up = null;
        for (int i = 0; i < 10000; i++) {
            topics.add(String.valueOf(i));
            if (up == null) {
                up = bus.subscribe(Subscription.of(String.valueOf(i)))
                        .flatMap(Node::onNext)
                        .map(o -> "1")
                        .doOnNext(System.out::println);
            } else {
                up.thenMany(bus.subscribe(Subscription.of(String.valueOf(i)))
                        .flatMap(Node::onNext)
                        .map(o -> "1")
                        .doOnNext(System.out::println));
            }
            bus.subscribe(Subscription.of(String.valueOf(i)))
                    .flatMap(Node::onNext)
                    .map(o -> "1")
                    .doOnNext(System.out::println)
                    .subscribe();
        }
        up.subscribe();
        for (String topic : topics) {
            bus.publish(topic, new DefaultEventContext());
        }
    }

    @Test
    void testSubThenPus() {
        EventBusFactory.get().subscribeOnRepeatable("1")
                .flatMap(Node::onNext)
                .delayElements(Duration.ofMillis(1000L))
                .flatMap(s -> EventBusFactory.get().publishOnFlux("1", new DefaultEventContext()))
                .subscribe();
    }

    @Test
    void testError() {
        EventBusFactory.get().subscribeOnRepeatable("1")
                .flatMap(Node::onNext)
                .doOnNext(o -> {
                    int i = 1 / 0;
                })
                .onErrorContinue((e, x) -> System.out.println(x))
                .subscribe(System.out::println);
        EventBusFactory.get().publishOnFlux("1", new DefaultEventContext())
                .subscribe();

        EventBusFactory.get().publishOnFlux("1", new DefaultEventContext())
                .subscribe();
    }

    @Test
    void testWildcard() {
        bus.subscribe(Subscription.of("/t1/**"))
                .map(node -> node.reply(EmitEvent.class, eventContext -> System.out.println(eventContext.getTopicPath())))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        bus.subscribe(Subscription.of("/t2/**"))
                .map(node -> node.reply(EmitEvent.class, eventContext -> System.out.println(eventContext.getTopicPath())))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        bus.publish(Subscription.of("/t1/t2"), new DefaultEventContext(Collections.emptyMap()));
        bus.publish(Subscription.of("/t1/t2/t3"), new DefaultEventContext(Collections.emptyMap()));


        bus.publish(Subscription.of("/t2/t1/t3"), new DefaultEventContext(Collections.emptyMap()));

        bus.publish(Subscription.of("/t2/t1/t3"), new DefaultEventContext(Collections.emptyMap()));
    }

    @Test
    void testNewSubscribe() {
        AtomicReference<Node<?>> atOne = new AtomicReference<>();
        bus.subscribe("1").last().subscribe(atOne::set);

        AtomicReference<Node<?>> atTwo = new AtomicReference<>();
        bus.subscribe(Subscription.of("1")).subscribe(atTwo::set);
    }

    @Test
    void testRepeatable() {
        AtomicReference<Node<?>> atOne = new AtomicReference<>();
        bus.subscribeOnRepeatable("1").last().subscribe(atOne::set);
        AtomicReference<Node<?>> atTwo = new AtomicReference<>();
        bus.subscribeOnRepeatable(Subscription.of("1")).subscribe(atTwo::set);
        atOne.get().equals(atTwo.get());
    }

    @Test
    void testConcurrent() throws InterruptedException {
        CountDownLatch counter = new CountDownLatch(100000000);
        for (int i = 0; i < 1000; i++) {
            String topic = "t1" + "/" + i;
            bus.subscribe(topic)
                    .flatMap(Node::onNext)
                    .doOnNext(c -> counter.countDown())
                    .subscribe();
        }

        for (int i = 0; i < 100000; i++) {
            bus.publish("/t1/**", new DefaultEventContext());
        }
        counter.await();
    }
}
