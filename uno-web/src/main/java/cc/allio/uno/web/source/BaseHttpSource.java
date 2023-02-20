package cc.allio.uno.web.source;

import cc.allio.uno.core.metadata.endpoint.source.JsonSource;
import cc.allio.uno.core.util.JsonUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Map;

public abstract class BaseHttpSource extends JsonSource {

    protected static final String ENDPOINT = "endpoint";

    /**
     * 请求映射名称，形如/test/demo
     */
    protected final String requestMappingName;

    protected final PathPatternParser parser;

    protected BaseHttpSource(String requestMappingName) {
        this.requestMappingName = requestMappingName;
        this.parser = new PathPatternParser();
    }

    public void endpoint(@RequestBody Map<String, Object> value) {
        next(JsonUtil.toJson(value));
    }
}
