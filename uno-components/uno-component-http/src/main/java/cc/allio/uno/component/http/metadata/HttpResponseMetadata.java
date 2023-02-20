package cc.allio.uno.component.http.metadata;

import cc.allio.uno.core.serializer.JsonNodeEnhancer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * 基于WebFlux的响应
 *
 * @author jw
 * @date 2021/12/6 23:14
 */
public interface HttpResponseMetadata extends HttpMetadata {

    /**
     * 获取响应的状态
     *
     * @return 响应的状态码
     * @see HttpStatus
     */
    HttpStatus getStatus();

    /**
     * 获取期望类型的结果的流数据
     *
     * @param expect 期望类型的Class对象
     * @param <T>    期望的泛型对象
     * @return 实例对象
     * @throws WebClientResponseException 当响应不是200时抛出异常
     */
    <T> Mono<T> toExpect(Class<T> expect);

    /**
     * 获取请求后的数据，
     *
     * @return 实例对象
     */
    default Mono<?> expect() {
        return toExpect(expectType());
    }

    /**
     * 获取字符串的结果流数据
     *
     * @return 字符串
     * @throws WebClientResponseException 当响应不是200时抛出异常
     */
    default Mono<String> expectString() {
        Mono<JsonNode> expectResult = toExpect(JsonNode.class);
        return expectResult
                .flatMap(expect -> Mono.just(expect.toString()));
    }

    default Mono<JsonNodeEnhancer> expectJson() {
        return toExpect(JsonNode.class)
                .map(JsonNodeEnhancer::new);
    }

    /**
     * 获取bool类型的结果数据流
     *
     * @return bool
     * @throws WebClientResponseException 当响应不是200时抛出异常
     */
    default Mono<Boolean> expectBoolean() {
        return toExpect(Boolean.class);
    }

    /**
     * 获取int类型的结果数据流
     *
     * @return int
     * @throws WebClientResponseException 当响应不是200时抛出异常
     */
    default Mono<Integer> expectInteger() {
        return toExpect(Integer.class);
    }

    /**
     * 获取float类型的结果数据流
     *
     * @return float
     * @throws WebClientResponseException 当响应不是200时抛出异常
     */
    default Mono<Float> expectFloat() {
        return toExpect(Float.class);
    }

    /**
     * 获取double类型的结果数据流
     *
     * @return double
     * @throws WebClientResponseException 当响应不是200时抛出异常
     */
    default Mono<Double> expectDouble() {
        return toExpect(Double.class);
    }

    /**
     * 获取byte类型的结果数据流
     *
     * @return byte
     * @throws WebClientResponseException 当响应不是200时抛出异常
     */
    default Mono<Byte> expectByte() {
        return toExpect(Byte.class);
    }

    /**
     * 获取char类型的结果数据流
     *
     * @return char
     * @throws WebClientResponseException 当响应不是200时抛出异常
     */
    default Mono<Character> expectCharacter() {
        return toExpect(Character.class);
    }
}
