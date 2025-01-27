package cc.allio.uno.core.chain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 定义链
 *
 * @param <IN>  进入链中元素的范型
 * @param <OUT> 出链元素的范型
 * @author j.x
 * @since 1.0
 */
public interface Chain<IN, OUT> {

    /**
     * 继续当前链数据
     *
     * @param context 入链元素
     * @return 出链数据
     */
    Mono<OUT> proceed(ChainContext<IN> context);

    /**
     * process chain and output {@link Flux}
     *
     * @param context the context
     * @return
     */
    Flux<OUT> processMany(ChainContext<IN> context);

    /**
     * 获取链中结点数据
     *
     * @return 结点数据流
     */
    List<? extends Node<IN, OUT>> getNodes();
}
