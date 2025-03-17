package cc.allio.uno.http.metadata;

import java.time.Duration;

/**
 * Http配置元数据
 *
 * @author j.x
 */
public interface HttpConfigurationMetadata {

    /**
     * 获取请求的基本链接<br/>
     * example：http://localhost:8080/api/user<br/>
     * return：http://localhost:8080
     *
     * @return 链接字符串数据
     */
    String getBaseUrl();

    /**
     * 获取请求的路径
     * example：http://localhost:8080/api/user<br/>
     * return：/api/user
     *
     * @return 路径字符串
     */
    String getUrlPath();


    /**
     * 获取字节缓冲区大小，请求与响应都使用该缓冲区大小
     *
     * @return int数据
     */
    int getBufferSize();

    /**
     * 获取请求响应超时时间
     *
     * @return 返回Duration对象
     */
    Duration getTimeout();
}
