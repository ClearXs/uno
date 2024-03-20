package cc.allio.uno.http.metadata.interceptor;

import cc.allio.uno.http.metadata.HttpHeader;
import cc.allio.uno.http.metadata.Parameter;
import cc.allio.uno.core.serializer.JsonNodeEnhancer;
import cc.allio.uno.core.util.ObjectUtils;
import com.google.common.collect.Lists;
import com.google.common.net.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * {@link TokenRequest}构建器
 *
 * @author j.x
 * @date 2022/8/25 09:24
 * @since 1.0
 */
public class TokenRequestBuilder {

    private final TokenRequest tokenRequest;

    /**
     * token额外请求参数
     */
    private final List<Parameter<String>> parameters;

    /**
     * 授权Header信息
     */
    private final List<HttpHeader> authHeaders;

    public TokenRequestBuilder() {
        this.tokenRequest = new TokenRequest();
        this.parameters = Lists.newArrayList();
        this.authHeaders = Lists.newArrayList();
    }

    public TokenRequestBuilder addTokenUri(String tokenUri) {
        tokenRequest.setTokenUri(tokenUri);
        return this;
    }

    public TokenRequestBuilder addHttpMethod(HttpMethod httpMethod) {
        tokenRequest.setHttpMethod(httpMethod);
        return this;
    }

    public TokenRequestBuilder addSecret(Parameter<String> secret) {
        tokenRequest.setSecret(secret);
        return this;
    }

    public TokenRequestBuilder addSecretKey(Parameter<String> secretKey) {
        tokenRequest.setSecretKey(secretKey);
        return this;
    }

    public TokenRequestBuilder addTokenVerify(Predicate<JsonNodeEnhancer> tokenVerify) {
        tokenRequest.setTokenVerify(tokenVerify);
        return this;
    }

    public TokenRequestBuilder addTokenBuild(Function<JsonNodeEnhancer, Token> tokenBuild) {
        tokenRequest.setTokenBuild(tokenBuild);
        return this;
    }

    public TokenRequestBuilder addFreeUpToken(String freeUpToken) {
        tokenRequest.setFreeUpToken(freeUpToken);
        return this;
    }

    /**
     * 添加授权Key（用户名）
     *
     * @param authKey key数据
     * @return {@link TokenRequestBuilder}
     */
    public TokenRequestBuilder addAuthHeaderKey(String authKey) {
        tokenRequest.setAuthHeaderKey(authKey);
        return this;
    }


    /**
     * 添加token额外参数
     *
     * @param key   参数key
     * @param value 参数value
     * @return {@link TokenRequestBuilder}
     */
    public TokenRequestBuilder addParameter(String key, String value) {
        parameters.add(new Parameter<>(key, value));
        return this;
    }

    /**
     * 添加Token额外参数
     *
     * @param parameter 参数实例
     * @return {@link TokenRequestBuilder}
     */
    public TokenRequestBuilder addParameter(Parameter<String> parameter) {
        parameters.add(parameter);
        return this;
    }

    /**
     * 批量添加Token额外参数
     *
     * @param parameters 参数数组
     * @return {@link TokenRequestBuilder}
     */
    public TokenRequestBuilder addParameters(Parameter<String>... parameters) {
        this.parameters.addAll(Lists.newArrayList(parameters));
        return this;
    }

    /**
     * 添加授权头部信息
     *
     * @param key    header-name {@link HttpHeaders}
     * @param values header-value
     * @return {@link TokenRequestBuilder}
     */
    public TokenRequestBuilder addAuthHeader(String key, String... values) {
        this.authHeaders.add(new HttpHeader(key, values));
        return this;
    }

    /**
     * 添加授权头部信息
     *
     * @param header HttpHeader实例
     * @return {@link TokenRequestBuilder}
     */
    public TokenRequestBuilder addAuthHeader(HttpHeader header) {
        this.authHeaders.add(header);
        return this;
    }

    /**
     * 批量添加授权头部信息
     *
     * @param headers Headers实例
     * @return {@link TokenRequestBuilder}
     */
    public TokenRequestBuilder addAuthHeaders(HttpHeader... headers) {
        this.authHeaders.addAll(Arrays.asList(headers));
        return this;
    }

    /**
     * 构建TokenRequest对象
     *
     * @return {@link TokenRequest}
     */
    public TokenRequest build() {
        // 验证参数
        if (ObjectUtils.isEmpty(tokenRequest.getTokenUri())) {
            throw new IllegalArgumentException("Token Request uri is empty");
        }
        if (ObjectUtils.isEmpty(tokenRequest.getHttpMethod())) {
            throw new IllegalArgumentException("Token Request method is empty");
        }
        if (ObjectUtils.isEmpty(tokenRequest.getSecretKey())) {
            throw new IllegalArgumentException("Token Request secret key is empty");
        }
        if (ObjectUtils.isEmpty(tokenRequest.getSecret())) {
            throw new IllegalArgumentException("Token Request secret is empty");
        }
        if (ObjectUtils.isEmpty(tokenRequest.getTokenVerify())) {
            throw new IllegalArgumentException("Token Request token verify is empty");
        }
        if (ObjectUtils.isEmpty(tokenRequest.getTokenBuild())) {
            throw new IllegalArgumentException("Token Request token fetch is empty");
        }
        if (ObjectUtils.isEmpty(tokenRequest.getAuthHeaderKey())) {
            throw new IllegalArgumentException("Token Request auth header is empty");
        }
        tokenRequest.setTokenExtraParam(parameters.toArray(new Parameter[]{}));
        tokenRequest.setAuthorizationHeaders(authHeaders.toArray(new HttpHeader[]{}));
        return tokenRequest;
    }
}
