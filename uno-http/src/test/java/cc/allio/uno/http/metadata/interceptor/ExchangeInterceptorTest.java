package cc.allio.uno.http.metadata.interceptor;

import cc.allio.uno.http.metadata.HttpRequestMetadata;
import cc.allio.uno.http.metadata.HttpRequestMetadataFactory;
import cc.allio.uno.http.metadata.HttpResponseMetadata;
import cc.allio.uno.http.metadata.HttpTestCase;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

/**
 * 测试数据交换拦截器
 *
 * @author jiangwei
 * @date 2022/8/25 14:55
 * @since 1.0
 */
class ExchangeInterceptorTest extends HttpTestCase {

    MockWebServer mockWebServer;
    ExchangeInterceptor exchangeInterceptor;

    @Override
    protected void onInit() throws Throwable {
        super.onInit();
        exchangeInterceptor = new ExchangeInterceptor();
    }

    /**
     * Test Case: 模拟远程数据判断数据结果
     */
    @Test
    void testMockRemoteBody() {
        String body = "{\"key\":\"value\"}";
        prepareResponse(res ->
                res.addHeader("Content-Type", "application/json")
                        .setBody(body));
        WebClient webClient = buildWebClient();

        HttpRequestMetadata request = HttpRequestMetadataFactory.getMetadata("/test", HttpMethod.GET, null);

        DefaultHttpChainContext chainContext = new DefaultHttpChainContext(request, webClient);

        // 检测结果数据是否正确
        exchangeInterceptor.execute(null, chainContext)
                .flatMap(HttpResponseMetadata::expectJson)
                .map(json -> json.asString("key"))
                .as(StepVerifier::create)
                .expectNext("value")
                .verifyComplete();

        prepareResponse(res ->
                res.addHeader("Content-Type", "application/json")
                        .setBody(body));
        exchangeInterceptor.execute(null, chainContext)
                .flatMap(HttpResponseMetadata::expectString)
                .as(StepVerifier::create)
                .expectNext(body)
                .verifyComplete();
    }

}
