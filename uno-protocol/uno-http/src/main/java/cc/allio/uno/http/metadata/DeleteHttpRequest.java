package cc.allio.uno.http.metadata;

import org.springframework.http.HttpMethod;

/**
 * 基于WebFlux的DELETE Http请求
 *
 * @author j.x
 */
public class DeleteHttpRequest extends BaseHttpRequest {

    public DeleteHttpRequest(String url) {
        super(url);
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.DELETE;
    }
}
