package cc.allio.uno.http.openapi;

import cc.allio.uno.core.util.FileUtils;
import cc.allio.uno.test.BaseTestCase;

import java.util.concurrent.atomic.AtomicReference;

class OpenApi2ParserTest extends BaseTestCase {

    private static AtomicReference<String> example;

    @Override
    protected void onInit() throws Throwable {
        example = new AtomicReference<>();
        FileUtils.readSingleFileForceToString("classpath:/openapi/open_api_v2_example.json", example::set);
        assertNotNull(example.get());
    }

}
