package cc.allio.uno.http.metadata;

import cc.allio.uno.http.openapi.OpenApiSpecificationParser;
import cc.allio.uno.core.util.JsonUtils;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.models.OpenAPI;

import java.time.Duration;
import java.util.Map;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import reactor.test.StepVerifier;

/**
 * Http交换对象测试
 *
 * @author jw
 * @date 2021/12/7 14:48
 */
class HttpSwapperTest extends HttpTestCase {

    /**
     * 测试Get方法
     */
    @Test
    void testGetSwap() {
        String body = "{\"id\":1,\"name\":\"\"}";
        prepareResponse(res ->
                res.addHeader("Content-Type", "application/json")
                        .setBody(body));
        String url = "/api/v1/clients/";
        StepVerifier.create(
                        HttpSwapper
                                .build(url, HttpMethod.GET)
                                .setWebClient(buildWebClient())
                                .addParameter("start", String.valueOf(0))
                                .addParameter("count", String.valueOf(100))
                                .swap()
                                .flatMap(HttpResponseMetadata::expectString))
                .expectNext(body)
                .verifyComplete();
    }

    /**
     * 测试Post方法，发送json数据
     */
    @Test
    void testPostSwap() {
        String url = "/user/add";
        String message = "{\"id\":1,\"name\":\"\"}";
        prepareResponse(res ->
                res.addHeader("Content-Type", "application/json")
                        .setBody(message));
        StepVerifier.create(
                        HttpSwapper
                                .build(url)
                                .addBody(message)
                                .setWebClient(buildWebClient())
                                .swap()
                                .flatMap(HttpResponseMetadata::expectString))
                .expectNext(message)
                .verifyComplete();
    }

    // --- 测试Post发送Java类型

    /**
     * 测试Post发送Boolean基础数据
     */
    @Test
    void testPostSwapBoolean() {
        String url = "/type/bool";
        prepareResponse(res ->
                res.addHeader("Content-Type", "application/json")
                        .setBody(Boolean.TRUE.toString()));
        StepVerifier.create(
                        HttpSwapper
                                .build(url)
                                .addBody(Boolean.TRUE)
                                .setWebClient(buildWebClient())
                                .swap()
                                .flatMap(HttpResponseMetadata::expectBoolean))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    /**
     * 测试Post发哦是那个Integer类型的数据
     */
    @Test
    void testPostSwapInteger() {
        String url = "/type/int";
        prepareResponse(res ->
                res.addHeader("Content-Type", "application/json")
                        .setBody(String.valueOf(1)));
        StepVerifier.create(
                        HttpSwapper
                                .build(url)
                                .addBody(1)
                                .setWebClient(buildWebClient())
                                .swap()
                                .flatMap(HttpResponseMetadata::expectInteger))
                .expectNext(1)
                .verifyComplete();
    }

    /**
     * 测试Post发哦是那个Float类型的数据
     */
    @Test
    void testPostSwapFloat() {
        String url = "/type/float";
        prepareResponse(res ->
                res.addHeader("Content-Type", "application/json")
                        .setBody(Float.toString(1f)));
        StepVerifier.create(
                        HttpSwapper
                                .build(url)
                                .addBody(1f)
                                .setWebClient(buildWebClient())
                                .swap()
                                .flatMap(HttpResponseMetadata::expectFloat))
                .expectNext(1f)
                .verifyComplete();
    }

    /**
     * 测试Post发哦是那个Double类型的数据
     */
    @Test
    void testPostSwapDouble() {
        String url = "/type/double";
        prepareResponse(res ->
                res.addHeader("Content-Type", "application/json")
                        .setBody(Double.toString(1d)));
        StepVerifier.create(
                        HttpSwapper
                                .build(url)
                                .addBody(1d)
                                .setWebClient(buildWebClient())
                                .swap()
                                .flatMap(HttpResponseMetadata::expectDouble))
                .expectNext(1d)
                .verifyComplete();
    }

    /**
     * 测试Post发哦是那个String类型的数据
     */
    @Test
    void testPostSwapString() {
        String url = "/type/string";
        prepareResponse(res ->
                res.addHeader("Content-Type", "application/json")
                        .setBody("message"));
        StepVerifier.create(
                        HttpSwapper
                                .build(url)
                                .addBody("message")
                                .setWebClient(buildWebClient())
                                .swap()
                                .flatMap(HttpResponseMetadata::expectString))
                .expectNext("message")
                .verifyComplete();
    }


    /**
     * 测试Post发哦是那个Byte类型的数据
     */
    @Test
    void testPostSwapByte() {
        String url = "/type/byte";
        prepareResponse(res ->
                res.addHeader("Content-Type", "application/json")
                        .setBody(Byte.valueOf("1").toString()));
        StepVerifier.create(
                        HttpSwapper
                                .build(url)
                                .addBody(Byte.valueOf("1"))
                                .setWebClient(buildWebClient())
                                .swap()
                                .flatMap(HttpResponseMetadata::expectByte))
                .expectNext(Byte.valueOf("1"))
                .verifyComplete();
    }

    /**
     * 测试Post发哦是那个Byte类型的数据
     */
    @Test
    void testPostSwapCharacter() {
        String url = "/type/char";
        prepareResponse(res ->
                res.addHeader("Content-Type", "application/json")
                        .setBody(String.valueOf('c')));
        StepVerifier.create(
                        HttpSwapper
                                .build(url)
                                .addBody('c')
                                .setWebClient(buildWebClient())
                                .swap()
                                .flatMap(HttpResponseMetadata::expectCharacter))
                .expectNext('c')
                .verifyComplete();
    }

    /**
     * 测试Post发送POJO对象
     */
    @Test
    void testPostSwapEntity() {
        String url = "/user/add";
        User user = new User();
        user.setId(1);
        user.setName("");
        prepareResponse(res ->
                res.addHeader("Content-Type", "application/json")
                        .setBody(JsonUtils.toJson(user)));
        StepVerifier.create(
                        HttpSwapper
                                .build(url)
                                .addBody(user)
                                .setWebClient(buildWebClient())
                                .swap()
                                .flatMap(res -> res.toExpect(User.class)))
                .expectNext(user)
                .verifyComplete();
    }

    /**
     * 测试接口的性能
     */
    @Test
    void testPerformance() {
        // 10s内完成3000次简单接口调用
        assertTimeout(Duration.ofMillis(10000), () -> {
            for (int i = 0; i < 3000; i++) {
                testPostSwapEntity();
            }
        });
    }

    /**
     * 测试接口超时
     */
    @Test
    void testTimeout() {
        String url = "http://localhost:8080/test/timeout";
        assertTimeout(Duration.ofMillis(6000), () -> {
            HttpSwapper
                    .build(url)
                    .timeout(6000)
                    .swap()
                    .flatMap(HttpResponseMetadata::expectString);
        });
    }

    @Test
    void testGetPlaceholder() {
        String url = "/user/{id}/{name}";
        User user = new User();
        user.setId(1);
        user.setName("jw");
        prepareResponse(res ->
                res.addHeader("Content-Type", "application/json")
                        .setBody(JsonUtils.toJson(user)));
        StepVerifier.create(
                        HttpSwapper
                                .build(url, HttpMethod.GET)
                                .addParameter("id", String.valueOf(1))
                                .addParameter("name", "jw")
                                .setWebClient(buildWebClient())
                                .swap()
                                .flatMap(res -> res.toExpect(User.class)))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void testPostPlaceholder() {
        String url = "/user/{id}/{name}";
        User user = new User();
        user.setId(1);
        user.setName("jw");
        prepareResponse(res ->
                res.addHeader("Content-Type", "application/json")
                        .setBody(JsonUtils.toJson(user)));
        StepVerifier.create(
                        HttpSwapper
                                .build(url)
                                .addParameter("id", String.valueOf(1))
                                .addParameter("name", "jw")
                                .setWebClient(buildWebClient())
                                .swap()
                                .flatMap(res -> res.toExpect(User.class)))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void testAddHeader() {
        String url = "http://localhost:8080/test/timeout";
        HttpSwapper.build(url)
                .addHeader("Content-Type", "text/plain");
    }

    @Test
    void testFromData() {
        String url = "http://localhost:8080/test/timeout";
        Map<String, Object> var = Maps.newHashMap();
        var.put("t1", "t1");

        HttpSwapper.build(url, HttpMethod.POST)
                .setMediaType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    @Test
    void testInterceptor() {
        String url = "http://localhost:8080/test/timeout";
        HttpSwapper.build(url)
                .addInterceptor((chain, context) -> null);
    }

    /**
     * 测试获取jetlinks api文档
     */
    @Test
    void testJetlinksApiDoc() {
        String url = "http://demo.jetlinks.cn:9010/v3/api-docs/";
        StepVerifier.create(HttpSwapper
                        .build(url, HttpMethod.GET)
                        .swap()
                        .flatMap(HttpResponseMetadata::expectString)
                        .map(OpenApiSpecificationParser.holder()::parseV3))
                .expectNext(new OpenAPI())
                .verifyComplete();
    }

    @Data
    static class User {
        long id;
        String name;
    }
}
