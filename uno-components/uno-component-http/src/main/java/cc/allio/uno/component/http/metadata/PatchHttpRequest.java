package cc.allio.uno.component.http.metadata;

import org.springframework.http.HttpMethod;

/**
 * 基于WebFlux的Patch Http请求
 *
 * @author jw
 * @date 2021/12/8 23:20
 */
public class PatchHttpRequest extends PutHttpRequest {

    public PatchHttpRequest(String url) {
        super(url);
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.PATCH;
    }
}
