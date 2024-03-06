package cc.allio.uno.http.metadata;

import org.springframework.http.HttpMethod;

/**
 * 基于WebFlux的Put Http请求
 *
 * @author jw
 * @date 2021/12/8 0:48
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
