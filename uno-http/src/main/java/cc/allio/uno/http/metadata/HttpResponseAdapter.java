package cc.allio.uno.http.metadata;

import cc.allio.uno.core.serializer.JsonNodeEnhancer;
import cc.allio.uno.core.util.JsonUtils;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Http响应适配
 *
 * @author j.x
 * @date 2022/6/16 17:03
 * @since 1.0
 */
public abstract class HttpResponseAdapter<Callback> {

    private final Mono<HttpResponseMetadata> response;

    protected HttpResponseAdapter(Mono<HttpResponseMetadata> response) {
        this.response = response;
    }

    /**
     * 获取Http响应元数据
     *
     * @return Http响应数据
     */
    protected Mono<HttpResponseMetadata> getResponse() {
        return this.response;
    }


    /**
     * 响应拦截，由子类实现。建议可以完成以下事情
     * <ul>
     *     <li>测试这次请求是否成功</li>
     *     <li>解析响应中需要的数据</li>
     * </ul>
     *
     * @param fetchCode 接口中用于校验的code，如{'code':200}
     * @param testing   测试返回的数据是否正确，校验返回的数据
     * @return Json对象
     * @see JsonUtils
     */
    protected abstract Mono<JsonNodeEnhancer> repsInterceptor(Supplier<String> fetchCode, Predicate<Callback> testing);
}
