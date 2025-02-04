package cc.allio.uno.http.metadata;

import org.springframework.http.HttpMethod;

/**
 * 基于WebFlux的Patch Http请求
 *
 * @author j.x
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
