package cc.allio.uno.component.http.openapi.v3;

import cc.allio.uno.component.http.openapi.AbstractPrimaryParser;
import cc.allio.uno.component.http.openapi.Parser;
import cc.allio.uno.component.http.openapi.ParserContext;
import cc.allio.uno.core.serializer.JacksonSerializer;
import cc.allio.uno.core.serializer.Serializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.v3.oas.models.OpenAPI;
import java.util.Arrays;
import java.util.List;

/**
 * OpenApi v3解析器
 *
 * @author jw
 * @date 2021/12/5 10:12
 */
public class OpenApiParser extends AbstractPrimaryParser<OpenAPI> {

    @Override
    public void doPreParser(ParserContext context) {
        // TODO
    }

    @Override
    public OpenAPI doParse(String unresolved, ParserContext context) {
        Serializer serializer = context.serializer();
        ((JacksonSerializer) serializer).registerWorker(OpenAPI.class.getName(), context.mapper());
        return serializer.deserialize(unresolved.getBytes(), OpenAPI.class);
    }

    @Override
    public SimpleModule newMapperModule() {
        return new SimpleModule(this.getClass().getName());
    }

    @Override
    protected List<Parser<?>> supportSubParser() {
        return Arrays.asList(
                new SchemaParser(),
                new StyleEnumParser(),
                new SecuritySchemeInParser(),
                new SecuritySchemeTypeParser()
        );
    }

}
