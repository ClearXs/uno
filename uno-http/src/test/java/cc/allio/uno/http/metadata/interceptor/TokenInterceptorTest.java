package cc.allio.uno.http.metadata.interceptor;

import cc.allio.uno.http.metadata.*;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.serializer.JsonNodeEnhancer;
import cc.allio.uno.core.util.JsonUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * 测试{@link TokenInterceptor}
 *
 * @author j.x
 * @since 1.0
 */
class TokenInterceptorTest extends HttpTestCase {
    TokenInterceptor tokenInterceptor;
    TokenManager tokenManager;

    @Override
    protected void onInit() throws Throwable {
        super.onInit();
        TokenRequest tokenRequest = TokenRequest.builder()
                .addTokenUri("/token")
                .addAuthHeaderKey("ACCESS_TOKEN")
                .addSecretKey(new Parameter<>("username", "admin"))
                .addSecret(new Parameter<>("password", "admin"))
                .addTokenVerify(json -> json.asInteger("code") == 0)
                .addTokenBuild(json -> {
                    Token token = new Token();
                    token.setAccessToken(json.asString("token"));
                    return token;
                })
                .addHttpMethod(HttpMethod.GET)
                .build();
        tokenManager = new DefaultTokenManager();
        tokenInterceptor = new TokenInterceptor(tokenRequest, tokenManager);
    }

    /**
     * Test Case: 测试Token认证成功
     */
    @Test
    void testTokenSuccess() throws Throwable {
        Token token = new Token();
        token.setAccessToken("test");
        tokenManager.set(token);
        String body = "{\"code\":0, \"data\":\"test\"}";
        prepareResponse(res ->
                res.addHeader("Content-Type", "application/json")
                        .setBody(body));
        WebClient webClient = buildWebClient();

        HttpRequestMetadata request = HttpRequestMetadataFactory.getMetadata("/test", HttpMethod.GET, null);

        DefaultHttpChainContext chainContext = new DefaultHttpChainContext(request, webClient);
        Chain<HttpRequestMetadata, HttpResponseMetadata> mockChain = Mockito.mock(Chain.class);
        HttpResponseMetadata mockResponse = Mockito.mock(HttpResponseMetadata.class);
        Mockito.when(mockChain.proceed(chainContext)).thenReturn(Mono.create(sink -> sink.success(mockResponse)));
        Mockito.when(mockResponse.expectJson()).thenReturn(Mono.create(sink -> sink.success(new JsonNodeEnhancer(JsonUtils.readTree(body)))));
        Mockito.when(mockResponse.expectString()).thenReturn(Mono.just(body));
        tokenInterceptor.execute(mockChain, chainContext)
                .flatMap(HttpResponseMetadata::expectString)
                .as(StepVerifier::create)
                .expectNext(body)
                .verifyComplete();
    }

    @Test
    void testTokenFailure() throws Throwable {
        String tokenBody = "{\"code\":0,\"token\":\"test\"}";
        // 第一次获取Token
        prepareResponse(res ->
                res.addHeader("Content-Type", "application/json")
                        .setBody(tokenBody));

        // 第二次获取真实请求数据
        String dataBody = "{\"code\":0,\"data\":\"test\"}";
        prepareResponse(res ->
                res.addHeader("Content-Type", "application/json")
                        .setBody(dataBody));
        WebClient webClient = buildWebClient();

        HttpRequestMetadata request = HttpRequestMetadataFactory.getMetadata("/test", HttpMethod.GET, null);
        DefaultHttpChainContext chainContext = new DefaultHttpChainContext(request, webClient);
        Chain<HttpRequestMetadata, HttpResponseMetadata> mockChain = Mockito.mock(Chain.class);
        Mockito.when(mockChain.proceed(chainContext)).thenReturn(Mono.empty());
        tokenInterceptor.execute(mockChain, chainContext)
                .flatMap(HttpResponseMetadata::expectString)
                .as(StepVerifier::create)
                .expectNext(dataBody)
                .verifyComplete();

        // 再次进行请求
        HttpResponseMetadata mockResponse = Mockito.mock(HttpResponseMetadata.class);
        Mockito.when(mockChain.proceed(chainContext)).thenReturn(Mono.create(sink -> sink.success(mockResponse)));
        Mockito.when(mockResponse.expectJson()).thenReturn(Mono.create(sink -> sink.success(new JsonNodeEnhancer(JsonUtils.readTree(dataBody)))));
        Mockito.when(mockResponse.expectString()).thenReturn(Mono.just(dataBody));
        tokenInterceptor.execute(mockChain, chainContext)
                .flatMap(HttpResponseMetadata::expectString)
                .as(StepVerifier::create)
                .expectNext(dataBody)
                .verifyComplete();
    }
}
