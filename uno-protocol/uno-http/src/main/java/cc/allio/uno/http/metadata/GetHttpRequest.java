package cc.allio.uno.http.metadata;

import org.springframework.http.HttpMethod;

/**
 * 基于WebFlux的Get Http请求
 *
 * @author j.x
 */
public class GetHttpRequest extends BaseHttpRequest {

    public GetHttpRequest(String url) {
        super(url);
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

}
