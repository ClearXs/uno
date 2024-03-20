package cc.allio.uno.http.metadata.interceptor;

import cc.allio.uno.http.metadata.HttpHeader;
import cc.allio.uno.http.metadata.Parameter;
import cc.allio.uno.core.serializer.JsonNodeEnhancer;
import lombok.Data;
import org.springframework.http.HttpMethod;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 存放Token请求与验证等操作
 *
 * @author j.x
 * @date 2022/8/25 09:23
 * @since 1.0
 */
@Data
public class TokenRequest {

    TokenRequest() {
    }

    /**
     * 获取token链接
     */
    private String tokenUri;

    /**
     * Token请求方法
     */
    private HttpMethod httpMethod;

    /**
     * 授权Header信息
     */
    private HttpHeader[] authorizationHeaders;

    /**
     * 请求密钥
     */
    private Parameter<String> secret;

    /**
     * 请求密钥Key
     */
    private Parameter<String> secretKey;

    /**
     * Token额外参数
     */
    private Parameter<String>[] tokenExtraParam;

    // ------------------ 请求后验证器 ------------------

    /**
     * Token请求验证器，传递请求后的Json数据，验证这个数据是否进行授权
     */
    private Predicate<JsonNodeEnhancer> tokenVerify;

    /**
     * Token获取器，传递请求后的Json数据，获取Token密钥
     */
    private Function<JsonNodeEnhancer, Token> tokenBuild;

    /**
     * 放行Token
     */
    private String freeUpToken;

    /**
     * 验证请求头
     */
    private String authHeaderKey;

    public static TokenRequestBuilder builder() {
        return new TokenRequestBuilder();
    }
}
