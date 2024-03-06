package cc.allio.uno.http.metadata;

import cc.allio.uno.core.StringPool;
import com.google.common.collect.Lists;
import lombok.ToString;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Http Header元数据，提供一些数据的操作，如判断某个请求头是否存在，删除某个请求头等
 *
 * @author jiangwei
 * @date 2022/4/2 10:09
 * @since 1.0.6
 */
@ToString
public class HttpHeaderMetadata {

    /**
     * 请求头缓存: <br/>
     *
     * @key: 请求头名称@{@link org.springframework.http.HttpHeaders}
     * @value {@link HttpHeader}
     */
    private final Map<String, HttpHeader> headers;

    public HttpHeaderMetadata() {
        headers = new HashMap<>();
    }

    /**
     * 判断是否存在某个请求头根据请求头名称
     *
     * @param headerName 请求头名称
     * @return 是否存在boolean
     */
    public boolean containsHeader(String headerName) {
        return headers.containsKey(headerName);
    }

    public void addHeader(String headerName, String... values) {
        HttpHeader httpHeader = new HttpHeader().setName(headerName).setValues(values);
        headers.put(httpHeader.getName(), httpHeader);
    }

    /**
     * 添加某个请求头
     *
     * @param header 请求头实例
     */
    public void addHeader(HttpHeader header) {
        headers.put(header.getName(), header);
    }

    /**
     * 添加请求头集合
     *
     * @param httpHeaders 请求头集合
     */
    public void addHeaders(Collection<HttpHeader> httpHeaders) {
        headers.putAll(httpHeaders.stream().collect(Collectors.toMap(HttpHeader::getName, header -> header)));
    }

    /**
     * 移除某个请求头
     *
     * @param header headerName
     */
    public void removeHeader(HttpHeader header) {
        headers.remove(header.getName());
    }

    /**
     * 移除某个请求头
     *
     * @param headerName 请求头名称
     */
    public void removeHeader(String headerName) {
        headers.remove(headerName);
    }

    /**
     * 根据某个请求头名称获取请求头实例
     *
     * @param headerName 请求头名称
     * @return Optional对象
     */
    public Optional<HttpHeader> getHeader(String headerName) {
        return Optional.ofNullable(headers.get(headerName));
    }

    /**
     * 根据请求头名获取对应头数据，如果不存在则返回默认值
     *
     * @param headerName   请求头名称
     * @param defaultValue 不存在的默认只
     * @return 请求头数据
     */
    public String getOrDefaultValue(String headerName, String defaultValue) {
        Optional<HttpHeader> headerOptional = getHeader(headerName);
        return headerOptional
                .flatMap(header -> Optional.of(String.join(StringPool.COMMA, header.getValues())))
                .orElse(defaultValue);
    }

    /**
     * 返回HttpHeader集合视图
     *
     * @return 集合视图实例
     */
    public Collection<HttpHeader> toCollection() {
        return headers.values();
    }

    /**
     * 返回多Value数据散列表
     *
     * @return MultiValueMap
     */
    public MultiValueMap<String, String> toMultiValueMap() {
        MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        for (HttpHeader header : headers.values()) {
            headerMap.put(header.getName(), Lists.newArrayList(header.getValues()));
        }
        return headerMap;
    }
}
