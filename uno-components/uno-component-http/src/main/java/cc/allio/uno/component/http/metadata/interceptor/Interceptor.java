package cc.allio.uno.component.http.metadata.interceptor;

import cc.allio.uno.component.http.metadata.HttpRequestMetadata;
import cc.allio.uno.component.http.metadata.HttpResponseMetadata;
import cc.allio.uno.core.chain.Node;

/**
 * HTTP拦截器
 *
 * @author jiangwei
 * @date 2022/8/24 16:41
 * @since 1.0
 */
public interface Interceptor extends Node<HttpRequestMetadata, HttpResponseMetadata> {
}
