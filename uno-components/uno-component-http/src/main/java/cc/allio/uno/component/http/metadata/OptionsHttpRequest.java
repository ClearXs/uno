package cc.allio.uno.component.http.metadata;

import org.springframework.http.HttpMethod;

/**
 * 基于WebFlux的Options Http请求
 *
 * @author jw
 * @date 2021/12/8 23:23
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
