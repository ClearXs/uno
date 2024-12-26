package cc.allio.uno.http.metadata;

import org.springframework.http.HttpMethod;

/**
 * 基于WebFlux的Options Http请求
 *
 * @author j.x
 */
public class OptionsHttpRequest extends BaseHttpRequest {

    public OptionsHttpRequest(String url) {
        super(url);
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.OPTIONS;
    }
}
