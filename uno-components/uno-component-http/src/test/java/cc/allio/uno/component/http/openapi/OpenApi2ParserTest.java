package cc.allio.uno.component.http.openapi;

import cc.allio.uno.core.util.FileUtil;
import cc.allio.uno.test.BaseTestCase;
import io.swagger.models.Swagger;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

class OpenApi2ParserTest extends BaseTestCase {

    private static AtomicReference<String> example;

    @Override
    protected void onInit() throws Throwable {
        example = new AtomicReference<>();
        FileUtil.readFileToString("classpath:/openapi/open_api_v2_example.json", example::set);
        assertNotNull(example.get());
    }

    @Test
    void parserV2Test() {
        Swagger swagger = OpenApiSpecificationParser.holder().parseV2(example.get());
        System.out.println(swagger);
    }


    @Override
    protected void onDown() throws Throwable {

    }
}
