package cc.allio.uno.http.metadata;

import org.springframework.http.HttpMethod;

/**
 * 基于WebFlux的Put Http请求
 *
 * @author j.x
 */
public class PutHttpRequest extends BaseHttpRequest {

    public PutHttpRequest(String url) {
        super(url);
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.PUT;
    }
}
