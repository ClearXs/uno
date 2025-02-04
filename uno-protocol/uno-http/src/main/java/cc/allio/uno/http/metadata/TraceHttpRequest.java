package cc.allio.uno.http.metadata;

import org.springframework.http.HttpMethod;

/**
 * 基于WebFlux的Trace Http请求
 *
 * @author j.x
 */
public class TraceHttpRequest extends BaseHttpRequest {

    public TraceHttpRequest(String url) {
        super(url);
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.TRACE;
    }
}
