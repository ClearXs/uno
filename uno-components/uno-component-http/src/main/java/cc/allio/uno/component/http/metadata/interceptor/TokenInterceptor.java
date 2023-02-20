package cc.allio.uno.component.http.metadata.interceptor;

import cc.allio.uno.component.http.metadata.*;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.ChainContext;
import cc.allio.uno.core.util.StringUtils;
import com.google.common.collect.Maps;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.annotation.Priority;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Token认证拦截器，当请求没有Token认证时，此时会经过此验证
 *
 * @author jiangwei
 * @date 2022/8/24 17:11
 * @since 1.0
 */
@Priority(Integer.MIN_VALUE)
public class TokenInterceptor implements Interceptor {

    private final TokenRequest tokenRequest;

    private final TokenManager tokenManager;

    public TokenInterceptor(TokenRequest tokenRequest) {
        this(tokenRequest, new DefaultTokenManager());
    }

    public TokenInterceptor(TokenRequest tokenRequest, TokenManager tokenManager) {
        this.tokenRequest = tokenRequest;
        this.tokenManager = tokenManager;
        String freeUpToken = tokenRequest.getFreeUpToken();
        if (StringUtils.isNotBlank(freeUpToken)) {
            Token token = new Token();
            token.setAccessToken(freeUpToken);
            token.setFreeUpToken(freeUpToken);
            tokenManager.set(token);
        }
    }

    @Override
    public Mono<HttpResponseMetadata> execute(Chain<HttpRequestMetadata, HttpResponseMetadata> chain, ChainContext<HttpRequestMetadata> context) throws Throwable {
        // 请求头中增加token
        context.getIN().getHttpHeaderMetadata().addHeader(tokenRequest.getAuthHeaderKey(), tokenManager.get() == null ? "" : tokenManager.get().getAccessToken());
        Mono<HttpResponseMetadata> proceed = chain.proceed(context);
        return proceed.flatMap(HttpResponseMetadata::expectJson)
                .filter(tokenRequest.getTokenVerify())
                .then(proceed)
                .onErrorResume(WebClientResponseException.class, error -> {
                    HttpStatus status = error.getStatusCode();
                    // 捕捉Token异常错误
                    if (HttpStatus.UNAUTHORIZED == status) {
                        // 构建Token请求
                        HttpMethod httpMethod = tokenRequest.getHttpMethod();
                        HttpSwapper swapper = HttpSwapper.build(tokenRequest.getTokenUri(), httpMethod);
                        if (httpMethod == HttpMethod.GET) {
                            swapper.addParameter(tokenRequest.getSecretKey());
                            swapper.addParameter(tokenRequest.getSecret());
                            swapper.addParameters(Arrays.asList(tokenRequest.getTokenExtraParam()));
                        } else if (httpMethod == HttpMethod.POST) {
                            // 构建form表单请求
                            Map<String, String> body = Maps.newHashMap();
                            body.put(tokenRequest.getSecretKey().getKey(), tokenRequest.getSecretKey().getValue());
                            body.put(tokenRequest.getSecret().getKey(), tokenRequest.getSecret().getValue());
                            body.putAll(Arrays.stream(tokenRequest.getTokenExtraParam()).collect(Collectors.toMap(Parameter::getKey, Parameter::getValue)));
                            swapper.setMediaType(MediaType.APPLICATION_FORM_URLENCODED);
                            swapper.addFromBody(body);
                        }
                        // Header信息
                        HttpHeader[] authorizationHeaders = tokenRequest.getAuthorizationHeaders();
                        if (!ObjectUtils.isEmpty(authorizationHeaders)) {
                            swapper.addHeaders(Arrays.asList(authorizationHeaders));
                        }
                        Mono<Token> tokenMono = swapper
                                .setWebClient(((HttpChainContext) context).getWebClient())
                                .swap()
                                .flatMap(HttpResponseMetadata::expectJson)
                                .map(tokenRequest.getTokenBuild())
                                .doOnNext(tokenManager::set);
                        // 重新构建原始请求
                        return Mono.zip(Mono.just(context.getIN()), tokenMono)
                                .flatMap(requestAndToken -> {
                                    HttpRequestMetadata retryRequest = requestAndToken.getT1();
                                    Token newToken = requestAndToken.getT2();
                                    retryRequest.getHttpHeaderMetadata().addHeader(tokenRequest.getAuthHeaderKey(), newToken.getAccessToken());
                                    return HttpSwapper.build(retryRequest).setWebClient(((HttpChainContext) context).getWebClient()).swap();
                                });
                    } else {
                        return Mono.error(error);
                    }
                });
    }
}
