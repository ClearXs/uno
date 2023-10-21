package cc.allio.uno.component.http;

import cc.allio.uno.core.serializer.SerializerHolder;
import cc.allio.uno.test.BaseTestCase;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class HttpInvokeTest extends BaseTestCase {

    private static DataBufferFactory bufferFactory = null;

    @Override
    protected void onInit() throws Throwable {
        bufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
        // 1.依据api文档建立对象
    }

    /**
     * 尝试使用WebClient调用Get接口
     * 1.拼接url
     * 2.拼接header
     * 3.拼接contentType
     * 4.建造者模式
     */
    @Test
    void tryToGet() throws InterruptedException {
        // 访问这个接口，返回结果为true
        String url = "http://localhost:8081/http/get?unknown=123";
        WebClient.RequestBodySpec spec = WebClient.create(url).method(HttpMethod.GET).contentType(MediaType.APPLICATION_JSON);
        StepVerifier.create(spec.exchange().flatMap(res -> {
                    HttpStatusCode httpStatus = res.statusCode();
                    if (httpStatus.value() != HttpStatus.OK.value()) {
                        return Mono.error(new UnknownError());
                    }
                    return res.bodyToMono(ByteBuffer.class).map(Unpooled::wrappedBuffer).defaultIfEmpty(Unpooled.EMPTY_BUFFER).map(payload -> {
                        byte[] bytes = new byte[payload.readableBytes()];
                        payload.readBytes(bytes);
                        try {
                            return new String(bytes, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return "";
                    });
                }))
                .expectNext("true")
                .verifyComplete();
    }

    /**
     * 尝试调用post接口
     */
    @Test
    void tryToPost() {
        // 接口返回
        String url = "http://localhost:8081/http/history";
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.put("body", new ArrayList<>());

        WebClient.RequestHeadersSpec<?> spec = WebClient
                .create(url)
                .method(HttpMethod.POST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromDataBuffers(Mono.just(bufferFactory.wrap(SerializerHolder.holder().get().serialize(body)))));
        StepVerifier.create(
                        spec
                                .exchange()
                                .flatMap(res -> {
                                    HttpStatusCode httpStatus = res.statusCode();
                                    if (httpStatus.value() != HttpStatus.OK.value()) {
                                        return Mono.error(new UnknownError());
                                    }
                                    return res.bodyToMono(ByteBuffer.class)
                                            .map(Unpooled::wrappedBuffer).defaultIfEmpty(Unpooled.EMPTY_BUFFER)
                                            .map(payload -> {
                                                byte[] bytes = new byte[payload.readableBytes()];
                                                payload.readBytes(bytes);
                                                return SerializerHolder.holder().get().deserialize(bytes, Boolean.class);
                                            });
                                }))
                .expectNext(true)
                .verifyComplete();
    }

    /**
     * 测试带有Auth的头
     */
    @Test
    void testAuthHeader() {
        String url = "http://localhost:8848/device/alarm/history/_query";
        WebClient.RequestBodySpec spec = WebClient
                .create(url)
                .method(HttpMethod.GET)
                .contentType(MediaType.APPLICATION_JSON);
        spec.headers(header -> {
            header.add("X-Access-Token", "814c6725d6c74895917e727b85cbd35b");
        });
        StepVerifier.create(
                        spec.exchange()
                                .flatMap(res -> {
                                    HttpStatusCode status = res.statusCode();
                                    if (status.value() != HttpStatus.OK.value()) {
                                        return Mono.error(new UnknownError());
                                    }
                                    return res
                                            .bodyToMono(ByteBuffer.class)
                                            .map(Unpooled::wrappedBuffer)
                                            .defaultIfEmpty(Unpooled.EMPTY_BUFFER)
                                            .map(payload -> {
                                                byte[] bytes = new byte[payload.readableBytes()];
                                                payload.readBytes(bytes);
                                                Map<String, Object> deserialize = SerializerHolder.holder().get().deserializeForMap(bytes, String.class, Object.class);
                                                deserialize.forEach((key, value) -> {
                                                    System.out.println(key);
                                                    System.out.println(value);
                                                });
                                                return Boolean.TRUE;
                                            });
                                }))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Override
    protected void onDown() throws Throwable {

    }
}
