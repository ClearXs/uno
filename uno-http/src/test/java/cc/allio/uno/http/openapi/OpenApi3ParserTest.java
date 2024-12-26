package cc.allio.uno.http.openapi;

import cc.allio.uno.core.util.FileUtils;
import cc.allio.uno.test.BaseTestCase;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

/**
 * 测试驱动开发 Open Api v3 解析器单测
 * 使用Springfox解析Open Api
 *
 * @author j.x
 */
class OpenApi3ParserTest extends BaseTestCase {

    /**
     * 解析Open Api v3
     */
    @Test
    void openApiV3Parser() {
        FileUtils.readSingleFileForceToString("classpath:openapi/open_api_v3_example.json", apiJson -> {
            OpenAPI api = OpenApiSpecificationParser.holder().parseV3(apiJson);
            assertEquals("3.0.3", api.getOpenapi());
        });
    }

    // ------ jetlinks openapi接口解析

    /**
     * 测试Jetlinks系统管理相关接口_OpenAPI.json
     */
    @Test
    void testJetlinksSystemApi() {
        FileUtils.readSingleFileForceToString("classpath:openapi/jetlinks/系统管理相关接口_OpenAPI.json", apiJson -> {
            OpenAPI api = OpenApiSpecificationParser.holder().parseV3(apiJson);
            assertEquals("3.0.1", api.getOpenapi());
        });
    }


    /**
     * 测试Jetlinks规则引擎相关接口_OpenAPI.json
     */
    @Test
    void testJetlinksRuleEngineApi() {
        FileUtils.readSingleFileForceToString("classpath:openapi/jetlinks/规则引擎相关接口_OpenAPI.json", apiJson -> {
            OpenAPI api = OpenApiSpecificationParser.holder().parseV3(apiJson);
            assertEquals("3.0.1", api.getOpenapi());
        });
    }


    /**
     * 测试Jetlinks设备接入相关接口_OpenAPI.json
     */
    @Test
    void testJetlinksDeviceReceiveApi() {
        FileUtils.readSingleFileForceToString("classpath:openapi/jetlinks/设备接入相关接口_OpenAPI.json", apiJson -> {
            OpenAPI api = OpenApiSpecificationParser.holder().parseV3(apiJson);
            assertEquals("3.0.1", api.getOpenapi());
        });
    }

    /**
     * 测试Jetlinks设备管理相关接口_OpenAPI.json
     */
    @Test
    void testJetlinksDeviceManageApi() {
        FileUtils.readSingleFileForceToString("classpath:openapi/jetlinks/设备管理相关接口_OpenAPI.json", apiJson -> {
            OpenAPI api = OpenApiSpecificationParser.holder().parseV3(apiJson);
            assertEquals("3.0.1", api.getOpenapi());
        });
    }

    /**
     * 测试Jetlinks设备设备管理相关接口_OpenAPI.json
     */
    @Test
    void testJetlinksNotifyManageApi() {
        FileUtils.readSingleFileForceToString("classpath:openapi/jetlinks/通知管理相关接口_OpenAPI.json", apiJson -> {
            OpenAPI api = OpenApiSpecificationParser.holder().parseV3(apiJson);
            assertEquals("3.0.1", api.getOpenapi());
        });
    }

    @Test
    void testJetlinksApi() {
        FileUtils.readSingleFileForceToString("classpath:openapi/jetlinks/Jetlinks.json", apiJson -> {
            OpenAPI api = OpenApiSpecificationParser.holder().parseV3(apiJson);
            assertEquals("3.0.1", api.getOpenapi());
        });
    }
}
