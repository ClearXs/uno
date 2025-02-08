package cc.allio.uno.http.metadata.interceptor;

import cc.allio.uno.core.chain.DefaultChainContext;
import cc.allio.uno.http.metadata.HttpRequestMetadata;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * HTTP链上下文对象
 *
 * @author j.x
 * @since 1.0
 */
@Getter
public class DefaultHttpChainContext extends DefaultChainContext<HttpRequestMetadata> implements HttpChainContext {

    private final WebClient webClient;

    public DefaultHttpChainContext(HttpRequestMetadata requestMetadata, WebClient webClient) {
        super(requestMetadata);
        this.webClient = webClient;
    }
}
