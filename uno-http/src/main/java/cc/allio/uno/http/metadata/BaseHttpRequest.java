package cc.allio.uno.http.metadata;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.serializer.Serializer;
import cc.allio.uno.core.serializer.SerializerHolder;
import cc.allio.uno.core.util.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.buffer.UnpooledByteBufAllocator;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.util.function.Tuple2;

/**
 * 基本的Http请求对象
 *
 * @author jw
 * @date 2021/12/6 23:36
 */
public abstract class BaseHttpRequest implements HttpRequestMetadata {

    protected static DataBufferFactory bufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));

    /**
     * HttpHeader请求头元数据
     */
    private final HttpHeaderMetadata headerMetadata = new HttpHeaderMetadata();

    /**
     * 请求的链接，只含由请求的最原始信息，如果请求中存在请求参数，那么进行裁剪，放入parameters中。
     */
    private final String url;

    /**
     * 当前请求的字符集
     */
    private final List<Charset> charsets = Lists.newCopyOnWriteArrayList();

    /**
     * 当前请求的字符即
     */
    private final MultiValueMap<String, String> cookies = new LinkedMultiValueMap<>();

    /**
     * 默认超时时间为30s
     */
    private Duration timeout;

    /**
     * 当前请求的参数
     */
    private final Map<String, String> parameters = Maps.newHashMap();

    /**
     * HTTP-Media-type
     */
    private MediaType mediaType = MediaType.APPLICATION_JSON;

    /**
     * 一次Http请求响应的配置数据
     */
    private final HttpConfigurationMetadata configuration = new DefaultHttpConfigurationMetadata();

    /**
     * 当前响应期望返回的类型
     */
    private Class<?> expectType = null;

    /**
     * 当前请求请求体数据
     */
    private Object body;

    protected BaseHttpRequest(String url) {
        AtomicReference<FluxSink<Tuple2<String, List<String>>>> emitter = new AtomicReference<>();
        Flux.create(emitter::set)
                .subscribe(tuple -> parameters.put(tuple.getT1(), String.join(StringPool.PIPE, tuple.getT2())));
        // 裁剪url
        this.url = StringUtils.cropUrl(url, emitter.get());
        StringUtils.getBaseUrl(this.url)
                .subscribe(baseUrl -> ((DefaultHttpConfigurationMetadata) configuration).setBaseUrl(baseUrl));
        StringUtils.getApiUrl(this.url)
                .subscribe(apiUrl -> ((DefaultHttpConfigurationMetadata) configuration).setUrlPath(apiUrl));
        this.timeout = configuration.getTimeout();
    }

    @Override
    public <T> DataBuffer body(T dto) {
        if (dto instanceof String) {
            return bufferFactory.wrap(((String) dto).getBytes());
        }
        Serializer serializer = SerializerHolder.holder().get();
        return bufferFactory.wrap(serializer.serialize(dto));
    }

    @Override
    public Object getBody() {
        return body;
    }

    /**
     * 添加消息体，多个调用只会记录最后一个
     *
     * @param body 消息体实例
     */
    public void addBody(Object body) {
        this.body = body;
    }

    @Override
    public Map<String, String> getParameters() {
        return this.parameters;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public HttpHeaderMetadata getHttpHeaderMetadata() {
        return this.headerMetadata;
    }

    /**
     * 设置Media-Type
     *
     * @param mediaType media-type实例
     * @see MediaType
     */
    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public MediaType getMediaType() {
        return mediaType;
    }

    @Override
    public MultiValueMap<String, String> getCookies() {
        return this.cookies;
    }

    @Override
    public List<Charset> getCharsets() {
        return this.charsets;
    }

    @Override
    public Duration getTimeout() {
        return this.timeout;
    }

    @Override
    public HttpConfigurationMetadata getConfiguration() {
        return this.configuration;
    }

    @Override
    public Class<?> expectType() {
        return this.expectType;
    }

    /**
     * 向当前Client添加全局的请求头
     *
     * @param name    请求头名称
     * @param content 请求头内容
     */
    public void addGlobeHeader(String name, String... content) {
        HttpHeader header = new HttpHeader();
        header.setName(name);
        header.setValues(content);
        this.headerMetadata.addHeader(header);
    }

    /**
     * 向当前Client批量添加请求头
     *
     * @param headers 请求头List
     */
    public void addHeaders(List<HttpHeader> headers) {
        this.headerMetadata.addHeaders(headers);
    }

    /**
     * 向Client添加Auth请求头
     *
     * @param auth       认证请求头
     * @param ciphertext 认证的密文
     */
    public void addAuthHeader(String auth, String ciphertext) {
        addGlobeHeader(auth, ciphertext);
    }

    /**
     * 向Client添加Cookie
     *
     * @param cookie 客户端的数据
     */
    public void addCookie(String name, String cookie) {
        this.cookies.put(name, Collections.singletonList(cookie));
    }

    /**
     * 向Client中批量添加cookie
     *
     * @param cookies cookie的集合
     */
    public void addCookies(MultiValueMap<String, String> cookies) {
        this.cookies.putAll(cookies);
    }


    /**
     * 向请求中添加charset
     *
     * @param charsets charset字符集
     * @see StandardCharsets
     */
    public void addCharset(Charset... charsets) {
        this.charsets.addAll(Arrays.asList(charsets));
    }

    /**
     * 向请Map中添加新的参数，如果Map中存在同参数名，那么将会替换掉
     *
     * @param name  参数名称
     * @param value 参数值
     */
    public void addParameter(String name, String value) {
        this.parameters.put(name, value);
    }

    /**
     * 向请求参数Map中添加参数，如果Map中存在同参数名，那么将会替换掉
     *
     * @param name   参数名称
     * @param values 参数值，数组形式。以|进行拼接
     */
    public void addParameter(String name, String... values) {
        this.parameters.put(name, String.join(StringPool.PIPE, values));
    }

    /**
     * 向请求参数Map中添加参数，如果Map中存在同参数名，那么将会替换掉
     *
     * @param parameters 请求参数Map集合
     */
    public void addParameters(Map<String, String> parameters) {
        this.parameters.putAll(parameters);
    }

    /**
     * 向请求参数Map中添加参数，如果Map中存在同参数名，那么将会替换掉
     *
     * @param parameters 请求参数list集合
     */
    public void addParameters(List<Parameter<String>> parameters) {
        this.parameters.putAll(
                parameters.stream().collect(Collectors.toMap(Parameter::getKey, Parameter::getValue))
        );
    }

    /**
     * 设置请求的超时时间
     *
     * @param millis 毫秒
     */
    public void timeout(long millis) {
        this.timeout = Duration.ofMillis(millis);
    }

    /**
     * 设置请求的超时时间
     *
     * @param timeout 传入的Duration
     */
    public void timeout(Duration timeout) {
        if (timeout != null) {
            this.timeout = timeout;
        }
    }

    public void setExpectType(Class<?> expectType) {
        this.expectType = expectType;
    }

}
