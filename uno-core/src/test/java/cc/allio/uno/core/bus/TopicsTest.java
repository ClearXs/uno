package cc.allio.uno.core.bus;

import cc.allio.uno.core.BaseTestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Slf4j
class TopicsTest extends BaseTestCase {

    TestMessageContext messageContext = null;
    Topics<TestMessageContext> topics = null;

    @Override
    protected void onInit() throws Throwable {
        messageContext = new TestMessageContext();
        topics = new Topics<>();
    }

    @Test
    void testLink() {
        topics.link(TopicKey.of("test"), EventBusFactory.newEventBus())
                .map(Topic::getPath)
                .as(StepVerifier::create)
                .expectNext("/test")
                .verifyComplete();
    }

    @Test
    void testNullLink() {
        assertThrows(NullPointerException.class, () -> topics.link(null, EventBusFactory.current()));
    }

    @Test
    void testUnlink() {
        topics.link(TopicKey.of("/test"), EventBusFactory.current())
                .then(topics.unlink(TopicKey.of("/test")))
                .as(StepVerifier::create)
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void testLookup() {
        topics.link(TopicKey.of("/PP"), EventBusFactory.current())
                .thenMany(topics.lookup(TopicKey.of("/PP")))
                .map(Topic::getPath)
                .as(StepVerifier::create)
                // 路径化结果
                .expectNext("/PP")
                .verifyComplete();
    }

    /**
     * Test Case: 创建三级路径结构，搜索其中两级路径下的topic
     */
    @Test
    void testChildLookupTopic() {
        topics.link(TopicKey.of("/p/**"), EventBusFactory.current())
                .then(topics.link(TopicKey.of("/p/c1"), EventBusFactory.current()))
                .then(topics.link(TopicKey.of(""), EventBusFactory.current()))
                .then(topics.link(TopicKey.of("/p/c1/c11"), EventBusFactory.current()))
                .thenMany(topics.lookup(TopicKey.of("/p/c1")))
                .count()
                .as(StepVerifier::create)
                .expectNext(2L)
                .verifyComplete();
    }

    /**
     * Test Case: 测试**通配符搜索是否准确
     */
    @Test
    void testWildcardLookup() {
        topics.link(TopicKey.of("/p"), EventBusFactory.current())
                .then(topics.link(TopicKey.of("/p/c1"), EventBusFactory.current()))
                .then(topics.link(TopicKey.of("/p/c2"), EventBusFactory.current()))
                .thenMany(topics.lookup(TopicKey.of("**")))
                .map(Topic::getPath)
                .as(StepVerifier::create)
                .expectNext("/p/c2")
                .expectNext("/p/c1")
                .verifyComplete();
    }


    @Test
    void testConcurrentLink() {
        Flux.range(1, 1000)
                .flatMap(i -> topics.link(TopicKey.of("/concurrent" + i), EventBusFactory.current()))
                .as(StepVerifier::create)
                .expectNextCount(1000)
                .verifyComplete();
    }
}
