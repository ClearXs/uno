package cc.allio.uno.component.http.metadata;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cc.allio.uno.component.http.metadata.interceptor.DefaultHttpChainContext;
import cc.allio.uno.component.http.metadata.interceptor.Interceptor;
import cc.allio.uno.component.http.metadata.exception.SwapperException;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.chain.DefaultChain;
import cc.allio.uno.core.spi.AutoTypeLoader;
import cc.allio.uno.core.util.StringUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;
import reactor.core.publisher.Mono;

/**
 * Http请求的交换者
 *
 * @author jw
 * @date 2021/12/7 11:06
 */
@Slf4j
public class HttpSwapper {

    /**
     * 请求的元数据
     */
    private final HttpRequestMetadata requestMetadata;

    private final List<Interceptor> interceptors = Lists.newArrayList();

    private WebClient webClient;

    public HttpSwapper(HttpRequestMetadata requestMetadata) {
        this.requestMetadata = requestMetadata;
        init();
    }

    /**
     * Http交换默认构造器，解析请求链接，如果包含了? 则认证它为Get请求，否则全部都为Post请求
     *
     * @param url 请求的链接
     */
    private HttpSwapper(String url) {
        HttpMethod method;
        int question = url.indexOf(StringPool.QUESTION_MARK);
        if (question > 0) {
            method = HttpMethod.GET;
        } else {
            method = HttpMethod.POST;
        }
        this.requestMetadata = HttpRequestMetadataFactory.getMetadata(url, method, null);
        init();
    }

    /**
     * Http交换默认构造器
     *
     * @param url    请求链接
     * @param method 请求的方法
     */
    private HttpSwapper(String url, HttpMethod method) {
        this.requestMetadata = HttpRequestMetadataFactory.getMetadata(url, method, null);
        init();
    }

    /**
     * Http交换默认构造器
     *
     * @param url    请求链接
     * @param method 请求的方法
     * @param expect 期望返回的类型
     */
    private HttpSwapper(String url, HttpMethod method, Class<?> expect) {
        this.requestMetadata = HttpRequestMetadataFactory.getMetadata(url, method, expect);
        init();
    }

    private void init() {
        webClient = WebClient.builder()
                .uriBuilderFactory(SwapperUrlBuilderFactory.factory())
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(requestMetadata.getConfiguration().getBufferSize()))
                .build();
    }

    /**
     * 向请求头中添加信息
     *
     * @param name    头名称
     * @param content 内容
     * @return 当前对象
     */
    public HttpSwapper addHeader(String name, String... content) {
        ((BaseHttpRequest) requestMetadata).addGlobeHeader(name, content);
        return this;
    }

    /**
     * 向请求中添加头部信息
     *
     * @param header 请求头实例
     * @return {@link HttpSwapper}
     */
    public HttpSwapper addHeader(HttpHeader header) {
        ((BaseHttpRequest) requestMetadata).addGlobeHeader(header.getName(), header.getValues());
        return this;
    }

    /**
     * 向请求批量添加头部信息
     *
     * @param headers 请求头
     * @return {@link HttpSwapper}
     */
    public HttpSwapper addHeaders(List<HttpHeader> headers) {
        ((BaseHttpRequest) requestMetadata).addHeaders(headers);
        return this;
    }

    /**
     * 向请求头中添加auth信息
     *
     * @param auth       认证头名称
     * @param ciphertext 密文
     * @return 当前对象
     */
    public HttpSwapper addAuth(String auth, String ciphertext) {
        ((BaseHttpRequest) requestMetadata).addAuthHeader(auth, ciphertext);
        return this;
    }

    /**
     * 向请Map中添加新的参数，如果Map中存在同参数名，那么将会替换掉
     *
     * @param name  参数名称
     * @param value 参数值
     */
    public HttpSwapper addParameter(String name, Integer value) {
        ((BaseHttpRequest) requestMetadata).addParameter(name, String.valueOf(value));
        return this;
    }

    /**
     * 向请Map中添加新的参数，如果Map中存在同参数名，那么将会替换掉
     *
     * @param name  参数名称
     * @param value 参数值
     */
    public HttpSwapper addParameter(String name, Boolean value) {
        ((BaseHttpRequest) requestMetadata).addParameter(name, String.valueOf(value));
        return this;
    }

    /**
     * 向请Map中添加新的参数，如果Map中存在同参数名，那么将会替换掉
     *
     * @param name  参数名称
     * @param value 参数值
     */
    public HttpSwapper addParameter(String name, Double value) {
        ((BaseHttpRequest) requestMetadata).addParameter(name, String.valueOf(value));
        return this;
    }

    /**
     * 向请Map中添加新的参数，如果Map中存在同参数名，那么将会替换掉
     *
     * @param name  参数名称
     * @param value 参数值
     */
    public HttpSwapper addParameter(String name, Float value) {
        ((BaseHttpRequest) requestMetadata).addParameter(name, String.valueOf(value));
        return this;
    }

    /**
     * 向请Map中添加新的参数，如果Map中存在同参数名，那么将会替换掉
     *
     * @param name  参数名称
     * @param value 参数值
     */
    public HttpSwapper addParameter(String name, Long value) {
        ((BaseHttpRequest) requestMetadata).addParameter(name, String.valueOf(value));
        return this;
    }

    /**
     * 向请Map中添加新的参数，如果Map中存在同参数名，那么将会替换掉
     *
     * @param name  参数名称
     * @param value 参数值
     */
    public HttpSwapper addParameter(String name, BigDecimal value) {
        ((BaseHttpRequest) requestMetadata).addParameter(name, String.valueOf(value));
        return this;
    }

    /**
     * 向请Map中添加新的参数，如果Map中存在同参数名，那么将会替换掉
     *
     * @param name  参数名称
     * @param value 参数值
     */
    public HttpSwapper addParameter(String name, String value) {
        ((BaseHttpRequest) requestMetadata).addParameter(name, value);
        return this;
    }

    /**
     * 向请Map中添加新的参数，如果Map中存在同参数名，那么将会替换掉
     *
     * @param name   参数名称
     * @param values 参数值，数组形式。以|进行拼接
     */
    public HttpSwapper addParameter(String name, String... values) {
        ((BaseHttpRequest) requestMetadata).addParameter(name, values);
        return this;
    }

    /**
     * 向请Map中添加新的参数，如果Map中存在同参数名，那么将会替换掉
     *
     * @param parameter 参数实例
     * @param <T>       参数值范型
     */
    public <T> HttpSwapper addParameter(Parameter<T> parameter) {
        ((BaseHttpRequest) requestMetadata).addParameter(parameter.getKey(), String.valueOf(parameter.getValue()));
        return this;
    }

    /**
     * 向请求参数Map中添加参数，如果Map中存在同参数名，那么将会替换掉。
     *
     * @param parameters 请求参数Map集合
     * @see BaseHttpRequest#addParameters(Map)
     */
    public HttpSwapper addParameters(Map<String, String> parameters) {
        ((BaseHttpRequest) requestMetadata).addParameters(parameters);
        return this;
    }

    /**
     * 向请求参数Map中添加参数，如果Map中存在同参数名，那么将会替换掉。
     *
     * @param parameters 请求参数list集合
     * @return {@link HttpSwapper}
     * @see BaseHttpRequest#addParameters(List)
     */
    public HttpSwapper addParameters(List<Parameter<String>> parameters) {
        ((BaseHttpRequest) requestMetadata).addParameters(parameters);
        return this;
    }

    /**
     * 向Client添加Cookie
     *
     * @param cookie 客户端的数据
     */
    public HttpSwapper addCookie(String name, String cookie) {
        ((BaseHttpRequest) requestMetadata).addCookie(name, cookie);
        return this;
    }

    /**
     * 向Client中批量添加cookie
     *
     * @param cookies cookie的集合
     */
    public HttpSwapper addCookies(MultiValueMap<String, String> cookies) {
        ((BaseHttpRequest) requestMetadata).addCookies(cookies);
        return this;
    }

    /**
     * 向请求中添加charset
     *
     * @param charsets charset字符集
     * @see Charset
     */
    public HttpSwapper addCharset(Charset... charsets) {
        ((BaseHttpRequest) requestMetadata).addCharset(charsets);
        return this;
    }

    /**
     * 向请求添加消息体
     *
     * @param body 消息体数据
     */
    public HttpSwapper addBody(Object body) {
        ((BaseHttpRequest) requestMetadata).addBody(body);
        return this;
    }

    /**
     * 添加表单Body
     *
     * @param formBody 表单Body
     */
    public HttpSwapper addFromBody(Map<String, String> formBody) {
        ((BaseHttpRequest) requestMetadata).addBody(formBody);
        return this;
    }

    /**
     * 设置Media-Type
     *
     * @param mediaType Media-Type实例
     */
    public HttpSwapper setMediaType(MediaType mediaType) {
        ((BaseHttpRequest) requestMetadata).setMediaType(mediaType);
        return this;
    }

    public HttpSwapper addInterceptor(Interceptor... interceptor) {
        this.interceptors.addAll(Arrays.asList(interceptor));
        return this;
    }

    /**
     * 设置请求的超时时间
     *
     * @param millis 毫秒
     */
    public HttpSwapper timeout(long millis) {
        ((BaseHttpRequest) requestMetadata).timeout(millis);
        return this;
    }

    /**
     * 设置请求的超时时间
     *
     * @param timeout 传入的Duration
     */
    public HttpSwapper timeout(Duration timeout) {
        if (timeout != null) {
            ((BaseHttpRequest) requestMetadata).timeout(timeout);
        }
        return this;
    }

    public HttpSwapper setWebClient(WebClient webClient) {
        this.webClient = webClient;
        return this;
    }

    /**
     * 发送网络请求，交换数据
     *
     * @return 响应的元数据流
     * @throws SwapperException 执行过程中发生错误时抛出
     */
    public Mono<HttpResponseMetadata> swap() {
        List<Interceptor> internalInterceptor = AutoTypeLoader.loadToList(Interceptor.class);
        interceptors.addAll(internalInterceptor);
        DefaultHttpChainContext chainContext = new DefaultHttpChainContext(requestMetadata, webClient);
        try {
            return new DefaultChain<>(interceptors).proceed(chainContext);
        } catch (Throwable e) {
            throw new SwapperException(e);
        }
    }

    /**
     * 获取请求方法
     *
     * @return {@link HttpMethod}对象实例
     */
    public HttpMethod getMethod() {
        return requestMetadata.getMethod();
    }

    /**
     * 发送网络请求，交换数据
     *
     * @param body 向请求体中发送的Java对象
     * @return 响应的元数据流
     * @throws NullPointerException 当请求体为空时抛出异常
     * @deprecated
     */
    @Deprecated
    public Mono<HttpResponseMetadata> swap(Object body) {
        return Mono.empty();
    }

    /**
     * @param url 请求的地址
     * @return HttpSwapper实例对象
     * @see #build(String, HttpMethod)
     */
    public static HttpSwapper build(String url) {
        return new HttpSwapper(url);
    }

    /**
     * 构建当前对象
     *
     * @param url    请求的地址
     * @param method 请求的方法
     * @return HttpSwapper实例对象
     */
    public static HttpSwapper build(String url, HttpMethod method) {
        return new HttpSwapper(url, method);
    }

    /**
     * 构建Swapper对象
     *
     * @param url    请求的地址
     * @param method 请求的方法
     * @param expect 期望返回的请求类型
     * @return HttpSwapper实例对象
     */
    public static HttpSwapper build(String url, HttpMethod method, Class<?> expect) {
        return new HttpSwapper(url, method, expect);
    }

    /**
     * 构建Swapper对象
     *
     * @param requestMetadata HttpRequestMetadata元数据对象
     * @return HttpSwapper实例对象
     */
    public static HttpSwapper build(HttpRequestMetadata requestMetadata) {
        return new HttpSwapper(requestMetadata);
    }

    private static class SwapperUrlBuilderFactory extends DefaultUriBuilderFactory {

        private static final UriBuilderFactory BUILDER_FACTORY = new SwapperUrlBuilderFactory();

        @Override
        public URI expand(String uriTemplate, Map<String, ?> uriVars) {
            URI uri = null;
            String expandUri = StringUtils.joinUrl(uriTemplate, uriVars);
            try {
                uri = new URI(expandUri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return uri;
        }

        static UriBuilderFactory factory() {
            return BUILDER_FACTORY;
        }
    }
}
