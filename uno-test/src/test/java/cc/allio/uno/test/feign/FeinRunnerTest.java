package cc.allio.uno.test.feign;

import cc.allio.uno.test.BaseCoreTest;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunFeignTest(clients = {FeinRunnerTest.TestClient.class, FeinRunnerTest.ReactiveTestClient.class})
class FeinRunnerTest extends BaseCoreTest {

    @Test
    void testClientNotnull() {
        TestClient client = getBean(TestClient.class);
        assertNotNull(client);
    }

    @Test
    void testReactiveClientNotNull() {
        ReactiveTestClient client = getBean(ReactiveTestClient.class);
        client.get()
                .as(StepVerifier::create)
                .expectError()
                .verify();
    }

    @FeignClient(value = "test")
    interface TestClient {

        @GetMapping("/get")
        String get();
    }

    @ReactiveFeignClient(value = "rTest")
    interface ReactiveTestClient {
        @GetMapping("/get")
        Mono<String> get();
    }
}
