package cc.allio.uno.starter.redis;

import cc.allio.uno.test.CoreTest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.test.StepVerifier;

class ReactiveRedisTemplateTest extends CoreTest {

    @Test
    void testValueOperation() {
        ReactiveRedisTemplateFactory factory = getBean(ReactiveRedisTemplateFactory.class);
        ReactiveRedisTemplate<String, String> template = factory.genericTemplate(String.class);
        template.opsForValue()
                .set("test", "test")
                .then(template.opsForValue().get("test"))
                .as(StepVerifier::create)
                .expectNext("test")
                .verifyComplete();

    }

    @Test
    void testListOperation() {
        ReactiveRedisTemplateFactory factory = getBean(ReactiveRedisTemplateFactory.class);
        ReactiveRedisTemplate<String, User> template = factory.genericTemplate(User.class);
        template.opsForList()
                .leftPush("users", new User("name"))
                .then(template.opsForList().leftPop("users"))
                .as(StepVerifier::create)
                .expectNext(new User("name"))
                .verifyComplete();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private String name;
    }
}
