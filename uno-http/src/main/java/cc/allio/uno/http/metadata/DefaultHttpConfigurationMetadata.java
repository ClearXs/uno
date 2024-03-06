package cc.allio.uno.http.metadata;

import java.time.Duration;

/**
 * Http配置默认实现
 *
 * @author jw
 * @date 2021/12/8 10:29
 */
public class DefaultHttpConfigurationMetadata implements HttpConfigurationMetadata {

    private String baseUrl;
    private String urlPath;
    private int bufferSize;
    private Duration timeout = Duration.ofMillis(15000);

    @Override
    public String getBaseUrl() {
        return this.baseUrl;
    }

    @Override
    public String getUrlPath() {
        return this.urlPath;
    }

    @Override
    public int getBufferSize() {
        return bufferSize == 0 ? Integer.MAX_VALUE : this.bufferSize;
    }

    @Override
    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
}
