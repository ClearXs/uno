package cc.allio.uno.web.source;

import cc.allio.uno.core.metadata.endpoint.source.JsonSource;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.core.util.StringUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Method;
import java.util.Map;

public abstract class BaseHttpSource extends JsonSource implements HttpSource {

    protected static final String ENDPOINT = "endpoint";

    /**
     * 请求映射名称，形如/test/demo
     */
    protected final String requestMappingUrl;

    protected BaseHttpSource(String requestMappingUrl) {
        if (StringUtils.isBlank(requestMappingUrl)) {
            throw new IllegalArgumentException("requestMappingUrl is empty");
        }
        this.requestMappingUrl = requestMappingUrl;
    }

    public void endpoint(@RequestBody Map<String, Object> value) {
        next(JsonUtils.toJson(value));
    }

    /**
     * 获取HTTP 请求的端点方法
     *
     * @return Method实例
     */
    protected Method getEndpointMethod() {
        return ReflectionUtils.findMethod(getClass(), ENDPOINT, Map.class);
    }

    @Override
    public String getMappingUrl() {
        return requestMappingUrl;
    }
}
