package cc.allio.uno.http.metadata;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 * 每个请求的元数据信息
 *
 * @author j.x
 */
public interface HttpMetadata {


    /**
     * 获取当前请求Http方法
     *
     * @return 请求方法的实例
     * @see HttpMethod
     */
    HttpMethod getMethod();

    /**
     * 请求请求的路径
     *
     * @return 路径的字符串
     */
    String getUrl();

    /**
     * 获取Http请求头元数据
     *
     * @return 请求的头元数据
     * @see HttpHeader
     */
    HttpHeaderMetadata getHttpHeaderMetadata();

    /**
     * Http传输的媒体类型
     *
     * @return 媒体类型实例
     * @see MediaType
     */
    MediaType getMediaType();

    /**
     * 获取配置的元数据
     *
     * @return 配置元数据实例
     * @see HttpConfigurationMetadata
     */
    HttpConfigurationMetadata getConfiguration();

    /**
     * 当前请求期望返回的类型
     *
     * @return 期望返回类型的实例
     */
    Class<?> expectType();
}
