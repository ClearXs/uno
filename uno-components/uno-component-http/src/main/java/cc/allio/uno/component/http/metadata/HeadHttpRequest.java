package cc.allio.uno.component.http.metadata;

import org.springframework.http.HttpMethod;

/**
 * 基于WebFlux的Head Http请求
 *
 * @author jw
 * @date 2021/12/8 23:21
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
