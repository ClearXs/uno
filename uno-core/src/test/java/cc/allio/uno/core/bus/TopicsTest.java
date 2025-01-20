package cc.allio.uno.core.bus;

import cc.allio.uno.core.BaseTestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
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
        topics.link(Subscription.of("test"), EventBusFactory.newEventBus())
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
        Subscription subscription = Subscription.of("/test");
        topics.link(subscription, EventBusFactory.current())
                .flatMapMany(Topic::getNode)
                .flatMap(node -> {
                    node.doLift(o -> log.info("node left subscribe id: {}", node.getSubscribeId()));
                    return topics.unlink("/test");
                })
                .as(StepVerifier::create)
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void testLookup() {
        topics.link(Subscription.of("/PP"), EventBusFactory.current())
                .thenMany(topics.lookup("/PP"))
                .map(Topic::getPath)
                .as(StepVerifier::create)
                // 路径化结果
                .expectNext("/PP")
                .verifyComplete();
    }

    /**
     * Test Case: 测试空查找主题
     */
    @Test
    void testEmptyLookupTopic() {
        topics.link(Subscription.of("/test/**"), EventBusFactory.current())
                .thenMany(topics.lookup(""))
                .as(StepVerifier::create)
                .expectNextCount(0L)
                .verifyComplete();
    }

    /**
     * Test Case: 创建三级路径结构，搜索其中两级路径下的topic
     */
    @Test
    void testChildLookupTopic() {
        topics.link(Subscription.of("/p/**"), EventBusFactory.current())
                .then(topics.link(Subscription.of("/p/c1"), EventBusFactory.current()))
                .then(topics.link(Subscription.of("/p/c2"), EventBusFactory.current()))
                .then(topics.link(Subscription.of("/p/c1/c11"), EventBusFactory.current()))
                .thenMany(topics.lookup("/p/c1"))
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
        topics.link(Subscription.of("/p"), EventBusFactory.current())
                .then(topics.link(Subscription.of("/p/c1"), EventBusFactory.current()))
                .then(topics.link(Subscription.of("/p/c2"), EventBusFactory.current()))
                .thenMany(topics.lookup("**"))
                .map(Topic::getPath)
                .as(StepVerifier::create)
                .expectNext("/p/c1")
                .expectNext("/p/c2")
                .verifyComplete();
    }

    @Override
    protected void onDown() throws Throwable {

    }

}
