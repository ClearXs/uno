package cc.allio.uno.http.openapi;

import cc.allio.uno.http.metadata.HttpSwapper;
import cc.allio.uno.core.util.Requires;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.*;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * 组装Open Api V3，转换成对应的{@link HttpSwapper}
 *
 * @author jw
 * @date 2021/12/6 19:36
 */
public class OpenApiV3Assembly implements OpenApiConverter {

    private final OpenAPI openApi;

    private final String host;

    private final String ciphertext;

    /**
     * 实例化Open Api V3组装器
     *
     * @param openApi {@link OpenAPI}实例对象
     * @param host    远程调用的主机名称, example: localhost:8080
     * @throws NullPointerException {@link OpenAPI}与host为空时抛出的异常
     */
    public OpenApiV3Assembly(OpenAPI openApi, String host) {
        this(openApi, host, "");
    }

    /**
     * 实例化Open Api V3组装器
     *
     * @param openApi    {@link OpenAPI}实例对象
     * @param host       远程调用的主机名称, example: localhost:8080
     * @param ciphertext 远程调用的服务端token，在组装时拼接到请求头中，根据Open Api找到需要token的话
     * @throws NullPointerException {@link OpenAPI}与host为空时抛出的异常
     */
    public OpenApiV3Assembly(OpenAPI openApi, String host, String ciphertext) {
        Requires.isNotNulls(openApi, host);
        this.openApi = openApi;
        this.host = host;
        this.ciphertext = ciphertext;
    }

    @Override
    public Mono<MultiValueMap<String, HttpSwapper>> all() {
        MultiValueMap<String, HttpSwapper> multiSwapper = new LinkedMultiValueMap<>();
        return Flux.fromIterable(openApi.getPaths().entrySet())
                .publishOn(Schedulers.boundedElastic())
                .flatMap(this::buildMultiValueSwapper)
                .map(tuple2 -> multiSwapper.put(tuple2.getT1(), tuple2.getT2()))
                .then(Mono.just(multiSwapper));
    }

    @Override
    public Flux<HttpSwapper> find(String path) {
        return Mono.justOrEmpty(openApi.getPaths().get(path))
                .flatMapMany(pathItem -> composedSecurityName(path, pathItem));
    }

    @Override
    public Mono<HttpSwapper> find(String path, HttpMethod method) {
        return find(path)
                .filter(swapper -> swapper.getMethod().name().equals(method.name()))
                .single();
    }

    @Override
    public Mono<Class<?>> findApiPathResponseType(String path, HttpMethod method) {
        return null;
    }

    /**
     * 在Open Api中找到认证相关的信息，如Token的名称。
     *
     * @return 寻找到的名称多数据流，没有找到则是空的数据流
     */
    public Flux<String> findSecurityName() {
        return Mono.justOrEmpty(openApi.getComponents().getSecuritySchemes())
                .flatMapMany(security -> Flux.fromIterable(security.values()))
                .switchIfEmpty(Flux.empty())
                .map(SecurityScheme::getName);
    }

    @Override
    public String apiVersion() {
        return openApi.getOpenapi();
    }

    @Override
    public int size() {
        return openApi.getPaths().keySet().stream()
                .map(key -> openApi.getPaths().get(key))
                .mapToInt(pathItem -> pathItem.readOperations().size())
                .sum();
    }

    /**
     * 根据Map.Entry构建多数据源的{@link HttpSwapper}结构，其中Key是api路径，value是{@link PathItem}
     *
     * @param pathEntry api路径与路径的Item结构
     * @return 单多ValueMap的数据流
     */
    private Mono<Tuple2<String, List<HttpSwapper>>> buildMultiValueSwapper(Map.Entry<String, PathItem> pathEntry) {
        return composedSecurityName(pathEntry.getKey(), pathEntry.getValue())
                .filter(Objects::nonNull)
                .collectList()
                .map(swappers -> Tuples.of(pathEntry.getKey(), swappers));
    }


    /**
     * 构建HttpSwapper对象
     *
     * @param path     请求api路径
     * @param pathItem 请求路径的对象
     * @return 构建完的对象
     */
    private Flux<HttpSwapper> buildSwappers(String path, PathItem pathItem) {
        return Flux.fromIterable(pathItem.readOperationsMap().entrySet())
                .map(operation -> HttpSwapper.build(host.concat(path), HttpMethod.valueOf(operation.getKey().name())));
    }

    /**
     * 构建HttpSwapper对象， 并组合认证
     *
     * @param path     请求api路径
     * @param pathItem 请求路径的对象
     * @return compose后新的HttpSwapper
     */
    private Flux<HttpSwapper> composedSecurityName(String path, PathItem pathItem) {
        Flux<HttpSwapper> origin = buildSwappers(path, pathItem);
        return findSecurityName()
                .concatMap(security -> origin.map(swapper -> swapper.addAuth(security, ciphertext)))
                .switchIfEmpty(origin);
    }
}
