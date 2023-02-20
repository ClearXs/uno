package cc.allio.uno.component.http.metadata;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.util.MultiValueMap;

/**
 * 基于WebFlux的Http元数据
 *
 * @author jw
 * @date 2021/12/6 23:12
 */
public interface HttpRequestMetadata extends HttpMetadata {

    /**
     * 获取请求的参数
     *
     * @return map数据
     */
    Map<String, String> getParameters();


    /**
     * 获取当前请求的cookie
     *
     * @return 返回存储cookie的map集合
     */
    MultiValueMap<String, String> getCookies();

    /**
     * 获取当前请求的字符集
     *
     * @return 字符集list集合
     */
    List<Charset> getCharsets();

    /**
     * 获取请求体数据
     *
     * @return 请求体实例或者null
     */
    Object getBody();

    /**
     * 向Client添加body对象
     *
     * @param dto 在网络中传输的对象
     * @param <T> dto的泛型类型
     * @return 当前对象
     * @deprecated 在1.1.0.RELEASE版本之后该方法标记为删除，原因是不直接从请求元数据中获取{@link DataBuffer}的数据。该为存放Body数据{@link #getBody()}
     */
    @Deprecated
    <T> DataBuffer body(T dto);

    /**
     * Http请求超时时间，默认超时时间为30s
     *
     * @return 返回一个Duration对象
     * @see Duration
     */
    Duration getTimeout();

}
