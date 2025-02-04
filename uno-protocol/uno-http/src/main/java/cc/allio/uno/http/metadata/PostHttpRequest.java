package cc.allio.uno.http.metadata;

import org.springframework.http.HttpMethod;

/**
 * 基于WebFlux的Post Http请求
 *
 * @author j.x
 */
public class PostHttpRequest extends BaseHttpRequest {

    public PostHttpRequest(String url) {
        super(url);
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

}
