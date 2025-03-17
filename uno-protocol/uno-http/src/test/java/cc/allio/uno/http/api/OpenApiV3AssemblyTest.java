package cc.allio.uno.http.api;

import cc.allio.uno.http.metadata.HttpResponseMetadata;
import cc.allio.uno.http.metadata.HttpSwapper;
import cc.allio.uno.http.openapi.OpenApiConverter;
import cc.allio.uno.http.openapi.OpenApiSpecificationParser;
import cc.allio.uno.http.openapi.OpenApiV3Assembly;
import cc.allio.uno.core.util.FileUtils;
import cc.allio.uno.test.BaseTestCase;
import io.swagger.v3.oas.models.OpenAPI;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * OpenApiV3Assembly测试
 *
 * @author j.x
 */
class OpenApiV3AssemblyTest extends BaseTestCase {

    private OpenAPI openAPI;

    private OpenApiConverter converter;

    @Override
    protected void onInit() throws Throwable {
        FileUtils.readSingleFileForceToString("classpath:openapi/open_api_v3_example.json", apiJson -> {
            openAPI = OpenApiSpecificationParser.holder().parseV3(apiJson);
            converter = new OpenApiV3Assembly(openAPI, "localhost:8080", "token");
        });
    }

    @Test
    void testFindByPath() {
        StepVerifier.create(converter.find("/user/getValue"))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void testApiSize() {
        assertEquals(4, converter.size());
    }

    @Test
    void testFindByPathAndMethod() {
        StepVerifier.create(converter.find("/user/{id}/{name}", HttpMethod.GET))
                .expectNextMatches(swapper -> swapper.getMethod().equals(HttpMethod.GET))
                .verifyComplete();
    }

    @Test
    void testAll() {
        StepVerifier.create(
                        converter.all()
                                .flatMapMany(swappers -> Flux.fromIterable(swappers.values()))
                                .reduce(new ArrayList<>(), (init, next) -> {
                                    init.addAll(next);
                                    return init;
                                })
                                .flatMapMany(Flux::fromIterable)
                                .cast(HttpSwapper.class)
                                .map(HttpSwapper::swap))
                .expectNextCount(converter.size())
                .verifyComplete();
    }

    @Test
    void testSwap() {
        StepVerifier.create(
                        converter.find("/user/getValue")
                                .flatMap(swapper -> swapper
                                        .addParameter("id", "1")
                                        .swap()
                                        .flatMap(HttpResponseMetadata::expectString))
                )
                .expectNext("{\"id\":1,\"name\":\"\"}")
                .verifyError(Throwable.class);
    }

    @Test
    void testAddToken() {
        StepVerifier.create(
                        converter.find("/user/getValue")
                                .flatMap(swapper -> swapper
                                        .addParameter("id", "1")
                                        .swap()
                                        .flatMap(HttpResponseMetadata::expectString)))
                .expectNext("{\"id\":1,\"name\":\"\"}")
                .verifyComplete();
    }
}
