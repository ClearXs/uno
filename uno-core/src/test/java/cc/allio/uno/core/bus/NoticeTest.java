package cc.allio.uno.core.bus;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.bus.event.EventNode;
import cc.allio.uno.core.bus.event.LiftEvent;
import cc.allio.uno.core.bus.event.EmitEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
class NoticeTest extends BaseTestCase {

    Notice<TestMessageContext> notice;
    TestMessageContext context;
    Subscription subscription;

    @Override
    protected void onInit() throws Throwable {
        subscription = Subscription.of("test");
        notice = new Notice<>(subscription);
        context = new TestMessageContext();
    }

    @Test
    void testOnNotify() {
        Mono<EventNode<TestMessageContext>> mono = notice.getAsyncNode();
        mono.doOnNext(node ->
                        node.reply(EmitEvent.class, message -> assertEquals("test", message.get("test").get())))
                .then(Mono.just(new TestMessageContext()))
                .doOnNext(messageContext -> notice.notify(() -> messageContext))
                .subscribe();
    }

    @Test
    void testDisappear() {
        Mono<EventNode<TestMessageContext>> mono = notice.getAsyncNode();
        mono.doOnNext(node -> node.reply(LiftEvent.class, o -> log.info("discard this node"))).subscribe();
        assertDoesNotThrow(() -> notice.disappear());
    }

    /**
     * test case：测试批量事件订阅
     */
    @Test
    void testBatchSubscription() {
        Mono<EventNode<TestMessageContext>> mono = notice.getAsyncNode();
        mono.doOnNext(node -> {
                    for (int i = 0; i < 100; i++) {
                        if (i % 2 == 0) {
                            node.reply(EmitEvent.class, context -> log.info("subscript emit event"));
                        }
                        if (i % 2 == 1) {
                            node.reply(LiftEvent.class, context -> log.info("subscript lift event"));
                        }
                    }
                })
                .then(Mono.just(context))
                .doOnNext(messageContext -> {
                    notice.notify(() -> messageContext);
                    notice.disappear();
                })
                .as(StepVerifier::create)
                .expectNextCount(1L)
                .verifyComplete();
    }

    /**
     * test case：测试自定义事件订阅
     */
    @Test
    void testCustomizeEvent() {
        Mono<EventNode<TestMessageContext>> mono = notice.getAsyncNode();
        mono.doOnNext(node -> node.reply(TestTopicEvent.class, context -> log.info("Customize event callback")))
                .then(Mono.just(context))
                .doOnNext(context -> notice.load(TestTopicEvent.class, context))
                .as(StepVerifier::create)
                .expectNextCount(1L)
                .verifyComplete();
    }

    /**
     * Test Case: 测试在多线程多数据量高压情况下耗时时间
     */
    @Test
    void pressureTest() {
        timing();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        for (int i = 0; i < 100000; i++) {
            EventNode<TestMessageContext> node = notice.getNode();
            if (i % 2 == 0) {
                executor.submit(() -> {
                    node.reply(EmitEvent.class, context -> assertEquals("test", context.get("test").get()));
                });
            }
            if (i % 2 == 1) {
                executor.submit(() -> {
                    node.reply(LiftEvent.class, context -> {
                        // TODO
                    });
                });
            }
            // 模拟事件订阅者是持续添加下去
            if (i % 1000 == 0) {
                notice.notify(() -> context);
                notice.disappear();
            }
        }
        stopTiming();
    }

    @Override
    protected void onDown() throws Throwable {

    }
}
