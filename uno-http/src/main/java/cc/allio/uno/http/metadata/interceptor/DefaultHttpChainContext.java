package cc.allio.uno.http.metadata.interceptor;

import cc.allio.uno.http.metadata.HttpRequestMetadata;
import com.google.common.collect.Maps;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

/**
 * HTTP链上下文对象
 *
 * @author jiangwei
 * @date 2022/8/24 16:42
 * @since 1.0
 */
public class DefaultHttpChainContext implements HttpChainContext {

    private final HttpRequestMetadata requestMetadata;

    private final Map<String, Object> attribute;
    private final WebClient webClient;

    public DefaultHttpChainContext(HttpRequestMetadata requestMetadata, WebClient webClient) {
        this.requestMetadata = requestMetadata;
        this.webClient = webClient;
        this.attribute = Maps.newHashMap();
    }

    @Override
    public HttpRequestMetadata getIN() {
        return requestMetadata;
    }

    @Override
    public Map<String, Object> getAttribute() {
        return attribute;
    }

    @Override
    public WebClient getWebClient() {
        return webClient;
    }
}
