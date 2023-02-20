package cc.allio.uno.component.http.api;

import cc.allio.uno.component.http.openapi.OpenApiConverter;
import cc.allio.uno.component.http.openapi.OpenApiSpecificationParser;
import cc.allio.uno.component.http.openapi.OpenApiV3Assembly;
import cc.allio.uno.component.http.metadata.HttpSwapper;
import cc.allio.uno.component.http.metadata.HttpResponseMetadata;
import cc.allio.uno.test.BaseTestCase;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.*;
import reactor.test.StepVerifier;

/**
 * 测试JetlinksApi
 *
 * @author jw
 * @date 2021/12/9 19:04
 */
class JetlinksApiTest extends BaseTestCase {

    private FluxSink<OpenApiConverter> sink;

    private Flux<OpenApiConverter> converterFlux;

    @Override
    protected void onInit() throws Throwable {
        converterFlux = EmitterProcessor.create();
        sink = ((EmitterProcessor<OpenApiConverter>) converterFlux).sink();
        HttpSwapper apiDocs = HttpSwapper.build("http://localhost:8848/v3/api-docs", HttpMethod.GET);
        apiDocs.swap()
                .flatMap(HttpResponseMetadata::expectString)
                .subscribe(api -> {
                    OpenAPI openAPI = OpenApiSpecificationParser.holder().parseV3(api);
                    sink.next(new OpenApiV3Assembly(openAPI, "localhost:8848", "814c6725d6c74895917e727b85cbd35b"));
                });
    }

    @Test
    void testGetAlarmHistory() {
        StepVerifier.create(
                        converterFlux
                                .flatMap(converter -> converter.find("/device/alarm/_count")
                                        .flatMap(swapper -> swapper.swap()
                                                .flatMap(HttpResponseMetadata::expectString))))
                .expectNext("{\"message\":\"success\",\"result\":7,\"status\":200,\"timestamp\":1639050557712}")
                .verifyComplete();
    }

    @Override
    protected void onDown() throws Throwable {

    }
}
