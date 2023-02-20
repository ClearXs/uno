package cc.allio.uno.component.http.metadata;

import org.springframework.http.HttpMethod;

/**
 * 基于WebFlux的Post Http请求
 *
 * @author jw
 * @date 2021/12/7 10:49
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
