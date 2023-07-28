package cc.allio.uno.component.http.metadata;

import org.springframework.http.HttpMethod;

/**
 * 基于WebFlux的Get Http请求
 *
 * @author jw
 * @date 2021/12/7 9:35
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
