package cc.allio.uno.http.metadata;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cc.allio.uno.core.proxy.CglibInvocationInterceptor;
import cc.allio.uno.core.proxy.InvocationInterceptor;
import cc.allio.uno.core.proxy.ProxyFactory;
import cc.allio.uno.core.util.Requires;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.http.HttpMethod;

/**
 * Http请求元数据生成工厂
 *
 * @author jw
 * @date 2021/12/7 11:17
 */
public class HttpRequestMetadataFactory {

    private HttpRequestMetadataFactory() {

    }

    /**
     * 请求方法与请求的元数据Class对象映射
     */
    private static final Map<String, Class<? extends HttpRequestMetadata>> METHOD_MAPPINGS = new ConcurrentHashMap<>();

    public static final InvocationInterceptor DEFAULT_INTERCEPTOR = new DefaultInvocationInterceptor();

    static {
        METHOD_MAPPINGS.put(HttpMethod.GET.name(), GetHttpRequest.class);
        METHOD_MAPPINGS.put(HttpMethod.POST.name(), PostHttpRequest.class);
        METHOD_MAPPINGS.put(HttpMethod.PATCH.name(), PutHttpRequest.class);
        METHOD_MAPPINGS.put(HttpMethod.DELETE.name(), DeleteHttpRequest.class);
        METHOD_MAPPINGS.put(HttpMethod.PATCH.name(), PatchHttpRequest.class);
        METHOD_MAPPINGS.put(HttpMethod.TRACE.name(), TraceHttpRequest.class);
        METHOD_MAPPINGS.put(HttpMethod.HEAD.name(), HeadHttpRequest.class);
        METHOD_MAPPINGS.put(HttpMethod.OPTIONS.name(), OptionsHttpRequest.class);
    }

    /**
     * 获取HttpRequest元数据
     *
     * @param url    请求的链接
     * @param method 请求方法
     * @param expect 期望结果返回的类型
     * @return 代理的HttpRequestMetadata对象
     * @see #getMetadata(String, HttpMethod, Class, InvocationInterceptor)
     */
    public static HttpRequestMetadata getMetadata(String url, HttpMethod method, Class<?> expect) {
        return getMetadata(url, method, expect, DEFAULT_INTERCEPTOR);
    }

    /**
     * 获取HttpRequest元数据<br/>
     * 当请求方法与拦截为空时抛出{@link IllegalArgumentException}异常。如果在Map缓存中没有存放HttpRequestMetadata对象。
     * 那么会默认使用{@link GetHttpRequest}Class对象。<br/>
     *
     * @param url         请求链接
     * @param method      请求的Http方法
     * @param expect      期望请求结果返回的类型
     * @param interceptor 方法拦截器，可以默认使用{@link HttpRequestMetadataFactory#DEFAULT_INTERCEPTOR}
     * @return 代理的HttpRequestMetadata对象
     * @throws IllegalArgumentException 当method参数或者interceptor为空时抛出
     * @see HttpMethod
     * @see InvocationInterceptor
     */
    public static HttpRequestMetadata getMetadata(String url, HttpMethod method, Class<?> expect, InvocationInterceptor interceptor) {
        Requires.isNotNulls(method, interceptor);
        boolean isPresent = METHOD_MAPPINGS.keySet()
                .stream()
                .anyMatch(method.name()::equals);
        HttpRequestMetadata request;
        if (isPresent) {
            request = ProxyFactory
                    .proxy()
                    .newProxyInstance(METHOD_MAPPINGS.get(method.name()), interceptor, new Object[]{url});
        } else {
            request = ProxyFactory
                    .proxy()
                    .newProxyInstance(GetHttpRequest.class, interceptor, new Object[]{url});
        }
        ((BaseHttpRequest) request).setExpectType(expect);
        return request;
    }

    static class DefaultInvocationInterceptor implements CglibInvocationInterceptor {

        @Override
        public Object invoke(Object object, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            return proxy.invokeSuper(object, args);
        }
    }
}
