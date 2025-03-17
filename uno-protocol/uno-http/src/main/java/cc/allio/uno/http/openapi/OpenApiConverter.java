package cc.allio.uno.http.openapi;

import cc.allio.uno.http.metadata.HttpSwapper;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 解析Open Api规范，转换成能够支撑请求与响应的{@link HttpSwapper}
 *
 * @author j.x
 */
public interface OpenApiConverter {

    /**
     * 根据OpenApi解析Path获取全部的接口数据，返回多Value的Map结构数据流<br/>
     * <b>Map结构:</b><br/>
     * <i>key: api的路径</i><br/>
     * <i>value：{@link HttpSwapper}的List结构</i>
     *
     * @return 多Value的Map结构
     */
    Mono<MultiValueMap<String, HttpSwapper>> all();


    /**
     * 根据请求的api路径，返回当前路径的{@link HttpSwapper}多数据流。
     *
     * @param path api请求路径
     * @return {@link HttpSwapper}多数据流
     */
    Flux<HttpSwapper> find(String path);

    /**
     * 根据请求的api路径与请求方法，返回当前路径的{@link HttpSwapper}单数据流
     *
     * @param path   请求api路径
     * @param method 请求方法
     * @return {@link HttpSwapper}单数据流
     */
    Mono<HttpSwapper> find(String path, HttpMethod method);

    /**
     * 寻找具体api路径的响应类型
     *
     * @param path   api路径
     * @param method 请求方法
     * @return 找到的Class对象，如果没有找到则返回{@code Mono.empty()}。如果它是某个实体类，根据类加载找不到的话，默认就返回String类型
     */
    Mono<Class<?>> findApiPathResponseType(String path, HttpMethod method);

    /**
     * 获取Open Api的规范版本
     *
     * @return 规范版本的字符串
     */
    String apiVersion();

    /**
     * Open Api规范的接口数量
     *
     * @return 找到的接口数量
     */
    int size();
}
