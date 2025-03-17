package cc.allio.uno.http.web.reactive;

import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.http.web.HttpSource;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.pattern.PathPatternParser;

import java.lang.reflect.Method;
import java.util.Map;

public abstract class BaseReactiveSource implements HttpSource {

    protected static final String ENDPOINT = "endpoint";

    /**
     * 请求映射名称，形如/test/demo
     */
    protected final String requestMappingUrl;

    protected final PathPatternParser parser;

    protected BaseReactiveSource(String requestMappingUrl) {
        if (StringUtils.isBlank(requestMappingUrl)) {
            throw new IllegalArgumentException("requestMappingUrl is empty");
        }
        this.requestMappingUrl = requestMappingUrl;
        this.parser = new PathPatternParser();
    }

    /**
     * 接口端点
     *
     * @param value value
     */
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

    /**
     * 子类实现，触发下一个数据流
     *
     * @param nextJson nextJson
     */
    protected abstract void next(String nextJson);

    @Override
    public String getMappingUrl() {
        return requestMappingUrl;
    }
}
