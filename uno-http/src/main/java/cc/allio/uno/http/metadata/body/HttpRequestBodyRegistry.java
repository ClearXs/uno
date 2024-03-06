package cc.allio.uno.http.metadata.body;

import cc.allio.uno.http.metadata.HttpRequestMetadata;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Http-Request注册表，设计参考于{@link ReactiveAdapterRegistry}
 *
 * @author jiangwei
 * @date 2022/10/19 16:26
 * @since 1.1.0
 */
public class HttpRequestBodyRegistry {

    @Nullable
    private static volatile HttpRequestBodyRegistry sharedInstance;

    /**
     * Http-Body注册表
     */
    public final List<HttpRequestBody> registry = new ArrayList<>();

    public HttpRequestBodyRegistry() {
        registerRequestBody(new JsonHttpRequestBody());
        registerRequestBody(new FormUrlencodedHttpRequestBody());
        registerRequestBody(new FormDataHttpRequestBody());
    }

    /**
     * 给定的request-body向registry里面添加数据
     *
     * @param requestBody requestBody实例
     */
    public void registerRequestBody(HttpRequestBody requestBody) {
        registry.add(requestBody);
    }

    /**
     * 获取HttpRequestBody实例
     *
     * @param requestMetadata 请求元数据
     * @return Body实例或者null
     */
    public HttpRequestBody getHttpRequestBody(HttpRequestMetadata requestMetadata) {
        if (CollectionUtils.isEmpty(registry)) {
            return null;
        }
        Object body = requestMetadata.getBody();
        if (ObjectUtils.isEmpty(body)) {
            return null;
        }
        MediaType mediaType = requestMetadata.getMediaType();
        for (HttpRequestBody requestBody : registry) {
            if (mediaType.equals(requestBody.getMediaType())) {
                return requestBody;
            }
        }
        return null;
    }

    /**
     * 返回一个单实例对象，是一个懒加载进行创建，双重判定锁进行创建。
     *
     * @return 单实例对象
     */
    public static HttpRequestBodyRegistry getSharedInstance() {
        HttpRequestBodyRegistry registry = sharedInstance;
        if (registry == null) {
            synchronized (HttpRequestBodyRegistry.class) {
                registry = sharedInstance;
                if (registry == null) {
                    registry = new HttpRequestBodyRegistry();
                    sharedInstance = registry;
                }
            }
        }
        return registry;
    }
}
