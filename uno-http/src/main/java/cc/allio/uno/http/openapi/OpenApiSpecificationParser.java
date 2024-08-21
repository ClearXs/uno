package cc.allio.uno.http.openapi;

import cc.allio.uno.http.openapi.v3.OpenApiParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;

/**
 * Open Api解析器，可以用来解析v2，v3的OpenApi。OpenApi规范可以看<a href="https://github.com/OAI/OpenAPI-Specification">OpenAPI-Specification</a>
 *
 * @author jw
 * @date 2021/12/4 22:51
 */
public class OpenApiSpecificationParser {

    private static OpenApiSpecificationParser holder = new OpenApiSpecificationParser();

    private final ParserContext context;


    private OpenApiSpecificationParser() {
        ObjectMapper mapper = new ObjectMapper();
        ParserExecution execution = new ParserExecution();
        context = new DefaultParserContext(mapper, execution);
        // 注册OpenApi v3版本解析器
        execution.register(new OpenApiParser(), context);
    }

    /**
     * 解析v3版本的OpenApi
     *
     * @param apiJson OpenApi json串
     * @return 解析完成的OpenApi对象
     * @see OpenAPI
     */
    public OpenAPI parseV3(String apiJson) {
        ParserExecution execution = context.execution();
        return execution.execute(OpenApiParser.class, apiJson, context);
    }

    public static OpenApiSpecificationParser holder() {
        return holder;
    }
}
