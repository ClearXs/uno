package cc.allio.uno.http.metadata.interceptor;

import cc.allio.uno.http.metadata.HttpRequestMetadata;
import cc.allio.uno.http.metadata.HttpResponseMetadata;
import cc.allio.uno.core.chain.Node;

/**
 * HTTP拦截器
 *
 * @author j.x
 * @date 2022/8/24 16:41
 * @since 1.0
 */
public interface Interceptor extends Node<HttpRequestMetadata, HttpResponseMetadata> {
}
