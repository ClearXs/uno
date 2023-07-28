package cc.allio.uno.test.annotation;

import cc.allio.uno.test.*;
import cc.allio.uno.test.env.annotation.FeignEnv;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.ServiceInstanceListSuppliers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RunTest(components = {FeignEnvTest.Application.class, FeignEnvTest.LoadBalancerApplication.class, FeignEnvTest.LocalClientConfiguration.class}, webEnvironment = RunTest.WebEnvironment.MOCK,
        properties = {"spring.main.web-application-type=servlet", "server.port=8089"})
@FeignEnv(clients = {FeignEnvTest.TestClient.class, FeignEnvTest.DemoClient1.class, FeignEnvTest.DemoClient2.class})
public class FeignEnvTest extends BaseTestCase {

    @Inject
    private TestClient testClient;

    @Inject
    private DemoClient1 demoClient1;

    @Inject
    private DemoClient2 demoClient2;

    @BeforeAll
    static void init() {
    }


    @Test
    void testTestClient(@Parameter CoreTest coreTest) {
        assertNotNull(testClient);
        // RequestMapping
        String value = testClient.hello();
        assertEquals("hello", value);

        // RequestLine
        String foo = testClient.foo();
        assertEquals("foo", foo);

        User user = testClient.getUser(1);
        assertEquals(new User("test"), user);

        testClient.addUser(new User("test"));
    }

    @Test
    void testLoadBalancer() {
        User user = demoClient1.getUser(1);
        User user1 = demoClient2.getUser(2);
        assertEquals(user, user1);
    }

    @FeignClient(value = "test", url = "http://localhost:8089/")
    public interface TestClient {

        @GetMapping("/value")
        String hello();

        @GetMapping("/foo")
        String foo();

        @GetMapping(value = "/getUser")
        User getUser(@RequestParam("id") long id);

        @PostMapping("/saveUser")
        void addUser(@RequestBody User user);
    }

    @RestController
    public static class Application {

        @GetMapping("/value")
        public String hello() {
            return "hello";
        }

        @GetMapping("/foo")
        public String foo() {
            return "foo";
        }

        @GetMapping("/getUser")
        public User getUser(long id) {
            log.debug(String.valueOf(id));
            return new User("test");
        }

        @PostMapping("saveUser")
        public void saveUser(User user) {
            log.info(user.toString());
        }
    }

    @FeignClient("demo01")
    public interface DemoClient1 extends BaseClient {

    }

    @FeignClient("demo02")
    public interface DemoClient2 extends BaseClient {

    }

    public interface BaseClient {


        @GetMapping("/getUser")
        User getUser(@RequestParam("id") long id);
    }

    @RestController
    @LoadBalancerClients({
            @LoadBalancerClient(value = "demo01", configuration = LocalClientConfiguration.class),
            @LoadBalancerClient(value = "demo02", configuration = LocalClientConfiguration.class)})
    public static class LoadBalancerApplication {

        @GetMapping
        public User getUser(long id) {
            System.out.println(id);
            return new User();
        }
    }

    @Configuration
    public static class LocalClientConfiguration {

        @Bean
        public ServiceInstanceListSupplier staticServiceInstanceListSupplier() {
            return ServiceInstanceListSuppliers.from("demo",
                    new DefaultServiceInstance("demo-1", "demo", "localhost", 8089, false));
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private String name;
    }
}
