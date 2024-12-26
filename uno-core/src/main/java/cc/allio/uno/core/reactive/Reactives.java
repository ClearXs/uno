package cc.allio.uno.core.reactive;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * {@link Mono}、{@link Flux}使用方法
 *
 * @author j.x
 * @since 1.1.4
 */
@Slf4j
public final class Reactives {

    /**
     * 通用的错误处理
     *
     * @param upstream upstream
     * @param <C>      类型
     * @return flux
     */
    public static <C> Flux<C> onErrorContinue(Flux<C> upstream) {
        return upstream.onErrorContinue((ex, o) -> log.error("The object stream has error, target object is {}", o, ex));
    }

    /**
     * 通用的错误处理
     *
     * @param upstream upstream
     * @param <C>      类型
     * @return mono
     */
    public static <C> Mono<C> onErrorContinue(Mono<C> upstream) {
        return upstream.onErrorContinue((ex, o) -> log.error("The object stream has error, target object is {}", o, ex));
    }

}
