package cc.allio.uno.http.metadata.interceptor;

import cc.allio.uno.http.metadata.HttpRequestMetadata;
import cc.allio.uno.core.chain.ChainContext;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * HTTP链上下文对象
 *
 * @author jiangwei
 * @date 2022/8/25 14:45
 * @since 1.0
 */
public interface HttpChainContext extends ChainContext<HttpRequestMetadata> {

    /**
     * 获取WebClient对象
     *
     * @return WebClient实例
     */
    WebClient getWebClient();
}
