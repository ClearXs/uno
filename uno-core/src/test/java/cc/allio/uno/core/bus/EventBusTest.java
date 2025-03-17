package cc.allio.uno.core.bus;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.bus.event.EmitEvent;
import cc.allio.uno.core.bus.event.Node;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class EventBusTest extends BaseTestCase {

    private final EventBus<EventContext> bus = new DefaultEventBus();

    @Test
    void testSubscribe() {
        // 获取订阅的node
        bus.subscribe(Subscription.of("1"))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        // 转换为 event context
        bus.subscribe(Subscription.of("1"))
                .doOnNext(System.out::println)
                .subscribe();

        bus.subscribe(Subscription.of("2"))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        bus.publish(Subscription.of("1"), new DefaultEventContext(Collections.emptyMap())).subscribe();
        bus.publish(Subscription.of("2"), new DefaultEventContext(Collections.emptyMap())).subscribe();
    }

    @Test
    void testNewPublisher() {
        bus.subscribe(Subscription.of("1"))
                .map(o -> "1")
                .doOnNext(System.out::println)
                .subscribe();
        bus.publish("1", new DefaultEventContext()).subscribe();
    }


    @Test
    void testPer() {
        List<String> topics = Lists.newArrayList();
        Flux<String> up = null;
        for (int i = 0; i < 10000; i++) {
            topics.add(String.valueOf(i));
            if (up == null) {
                up = bus.subscribe(Subscription.of(String.valueOf(i)))
                        .map(o -> "1")
                        .doOnNext(System.out::println);
            } else {
                up.thenMany(bus.subscribe(Subscription.of(String.valueOf(i)))
                        .map(o -> "1")
                        .doOnNext(System.out::println));
            }
            bus.subscribe(Subscription.of(String.valueOf(i)))
                    .map(o -> "1")
                    .doOnNext(System.out::println)
                    .subscribe();
        }
        up.subscribe();
        for (String topic : topics) {
            bus.publish(topic, new DefaultEventContext()).subscribe();
        }
    }

    @Test
    void testSubThenPus() {
        EventBusFactory.current()
                .subscribeOnRepeatable("1")
                .delayElements(Duration.ofMillis(1000L))
                .flatMap(s -> EventBusFactory.current().publishOnFlux("1", new DefaultEventContext()))
                .subscribe();
    }

    @Test
    void testError() {
        EventBusFactory.current()
                .subscribeOnRepeatable("1")
                .doOnNext(o -> {
                    int i = 1 / 0;
                })
                .onErrorContinue((e, x) -> System.out.println(x))
                .subscribe(System.out::println);
        EventBusFactory.current().publishOnFlux("1", new DefaultEventContext()).subscribe();

        EventBusFactory.current().publishOnFlux("1", new DefaultEventContext()).subscribe();
    }

    @Test
    void testWildcard() {
        bus.subscribe(Subscription.of("/t1/**"))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        bus.subscribe(Subscription.of("/t2/**"))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        bus.publish(Subscription.of("/t1/t2"), new DefaultEventContext(Collections.emptyMap())).subscribe();
        bus.publish(Subscription.of("/t1/t2/t3"), new DefaultEventContext(Collections.emptyMap())).subscribe();


        bus.publish(Subscription.of("/t2/t1/t3"), new DefaultEventContext(Collections.emptyMap())).subscribe();

        bus.publish(Subscription.of("/t2/t1/t3"), new DefaultEventContext(Collections.emptyMap())).subscribe();
    }

    @Test
    void testConcurrent() throws InterruptedException {
        CountDownLatch counter = new CountDownLatch(10000);
        for (int i = 0; i < 1000; i++) {
            String topic = "t1" + "/" + i;
            bus.subscribe(topic)
                    .doOnNext(c -> counter.countDown())
                    .subscribe();
        }

        for (int i = 0; i < 100000; i++) {
            bus.publish("/t1/**", new DefaultEventContext()).subscribe();
        }
        counter.await();
    }

    @Test
    void testHasTopic() {
        bus.subscribe("/t1").subscribe();
        assertTrue(bus.hasTopic("/t1"));

        bus.subscribe("/t1/t2").subscribe();
        assertTrue(bus.hasTopic("/t1/**"));

        assertFalse(bus.hasTopic("/t2/**"));

    }
}
