package cc.allio.uno.http.metadata.interceptor;

import cc.allio.uno.http.metadata.ClientResponseWrapper;
import cc.allio.uno.http.metadata.HttpHeaderMetadata;
import cc.allio.uno.http.metadata.HttpRequestMetadata;
import cc.allio.uno.http.metadata.HttpResponseMetadata;
import cc.allio.uno.http.metadata.body.HttpRequestBody;
import cc.allio.uno.http.metadata.body.HttpRequestBodyRegistry;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.ChainContext;
import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.Charset;

/**
 * 数据交换Interceptor
 *
 * @author j.x
 * @since 1.0
 */
@Slf4j
@AutoService(Interceptor.class)
public class ExchangeInterceptor implements Interceptor {

    @Override
    public Mono<HttpResponseMetadata> execute(Chain<HttpRequestMetadata, HttpResponseMetadata> chain, ChainContext<HttpRequestMetadata> context) {
        HttpRequestMetadata requestMetadata = context.getIN();
        WebClient.RequestBodySpec requestBody = ((HttpChainContext) context).getWebClient()
                .method(requestMetadata.getMethod())
                .uri(requestMetadata.getUrl(), requestMetadata.getParameters());
        // 添加默认header与存在的header
        requestBody.headers(httpHeaders -> {
            HttpHeaderMetadata httpHeaderMetadata = requestMetadata.getHttpHeaderMetadata();
            // 添加所有的header
            httpHeaders.addAll(httpHeaderMetadata.toMultiValueMap());
            // Host
            httpHeaders.set(HttpHeaders.HOST, httpHeaderMetadata.getOrDefaultValue(HttpHeaders.HOST, requestMetadata.getConfiguration().getBaseUrl()));
            // User-Agent
            httpHeaders.set(HttpHeaders.USER_AGENT, httpHeaderMetadata.getOrDefaultValue(HttpHeaders.USER_AGENT, "UNO/1.0.6"));
            // Accept
            httpHeaders.set(HttpHeaders.ACCEPT, httpHeaderMetadata.getOrDefaultValue(HttpHeaders.ACCEPT, "*/*"));
            // Accept-Encoding
            httpHeaders.set(HttpHeaders.ACCEPT_ENCODING, httpHeaderMetadata.getOrDefaultValue(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br"));
            // Connection
            httpHeaders.set(HttpHeaders.CONNECTION, httpHeaderMetadata.getOrDefaultValue(HttpHeaders.CONNECTION, "keep-alive"));
        });
        // contentType
        requestBody.contentType(requestMetadata.getMediaType());
        // charset
        requestBody.acceptCharset(requestMetadata.getCharsets().toArray(new Charset[]{}));
        // cookie
        requestBody.cookies(cookies -> cookies.putAll(requestMetadata.getCookies()));
        // request-body
        HttpRequestBody decorationBody = HttpRequestBodyRegistry.getSharedInstance().getHttpRequestBody(requestMetadata);
        if (decorationBody != null) {
            requestBody.body(decorationBody.getBody(requestMetadata));
        }
        return requestBody
                .exchange()
                .timeout(requestMetadata.getTimeout())
                .publishOn(Schedulers.boundedElastic())
                .onErrorContinue((error, obj) -> log.error("Execute url {} remote invoke failed", requestMetadata.getUrl(), error))
                .flatMap(res -> Mono.just(new ClientResponseWrapper(res, () -> requestMetadata)));
    }

}
