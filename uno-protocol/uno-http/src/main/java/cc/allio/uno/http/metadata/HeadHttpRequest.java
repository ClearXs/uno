package cc.allio.uno.http.metadata;

import org.springframework.http.HttpMethod;

/**
 * 基于WebFlux的Head Http请求
 *
 * @author j.x
 */
public class HeadHttpRequest extends BaseHttpRequest {

    public HeadHttpRequest(String url) {
        super(url);
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.HEAD;
    }
}
