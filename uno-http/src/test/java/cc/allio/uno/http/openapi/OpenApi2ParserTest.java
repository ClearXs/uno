package cc.allio.uno.http.openapi;

import cc.allio.uno.core.util.FileUtils;
import cc.allio.uno.test.BaseTestCase;
import io.swagger.models.Swagger;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

class OpenApi2ParserTest extends BaseTestCase {

    private static AtomicReference<String> example;

    @Override
    protected void onInit() throws Throwable {
        example = new AtomicReference<>();
        FileUtils.readSingleFileForceToString("classpath:/openapi/open_api_v2_example.json", example::set);
        assertNotNull(example.get());
    }

    @Test
    void parserV2Test() {
        Swagger swagger = OpenApiSpecificationParser.holder().parseV2(example.get());
        System.out.println(swagger);
    }
}
