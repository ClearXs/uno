package cc.allio.uno.http.openapi;

import cc.allio.uno.core.serializer.Serializer;
import cc.allio.uno.core.util.Requires;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * 默认解析器上下文
 *
 * @author jw
 * @date 2021/12/5 10:44
 */
public class DefaultParserContext implements ParserContext {

    private final ObjectMapper mapper;

    private final ParserExecution execution;

    private SimpleModule module;

    private Serializer serializer;

    public DefaultParserContext(ObjectMapper mapper, ParserExecution execution) {
        this.mapper = mapper;
        this.execution = execution;
    }

    @Override
    public ObjectMapper mapper() {
        Requires.isNotNull(mapper, "");
        return mapper;
    }

    @Override
    public SimpleModule module() {
        return module;
    }

    public void setModule(SimpleModule module) {
        this.module = module;
    }

    @Override
    public ParserExecution execution() {
        Requires.isNotNull(execution, "");
        return execution;
    }

    @Override
    public Serializer serializer() {
        Requires.isNotNull(serializer, "");
        return serializer;
    }

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }
}
