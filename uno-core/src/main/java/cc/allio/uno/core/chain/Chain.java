package cc.allio.uno.core.chain;

import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 定义链
 *
 * @param <IN>  进入链中元素的范型
 * @param <OUT> 出链元素的范型
 * @author jiangwei
 * @date 2022/8/22 18:47
 * @since 1.0
 */
public interface Chain<IN, OUT> {

    /**
     * 继续当前链数据
     *
     * @param context 入链元素
     * @return 出链数据
     * @throws Throwable 链中发生异常时抛出
     */
    Mono<OUT> proceed(ChainContext<IN> context) throws Throwable;

    /**
     * 获取链中结点数据
     *
     * @return 结点数据流
     */
    List<? extends Node<IN, OUT>> getNodes();
}
