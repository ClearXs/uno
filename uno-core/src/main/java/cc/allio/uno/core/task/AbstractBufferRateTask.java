package cc.allio.uno.core.task;

import cc.allio.uno.core.reactive.BufferRate;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * 基于Reactor背压特性的任务
 *
 * @author jiangwei
 * @date 2021/12/22 16:41
 * @since 1.0
 */
public abstract class AbstractBufferRateTask<T> implements ReactiveTask<List<T>> {


    public Flux<List<T>> bufferRate(Flux<T> flux,
                                    int rate,
                                    Duration maxTimeout) {
        return BufferRate.create(flux, rate, maxTimeout);
    }

    public Flux<List<T>> bufferRate(Flux<T> flux,
                                    int rate,
                                    int maxSize,
                                    Duration maxTimeout) {
        return BufferRate.create(flux, rate, maxSize, maxTimeout);

    }

    public Flux<List<T>> bufferRate(Flux<T> flux,
                                    int rate,
                                    int maxSize,
                                    Duration maxTimeout,
                                    BiPredicate<T, List<T>> flushCondition) {
        return BufferRate.create(flux, rate, maxSize, maxTimeout, flushCondition);
    }

}
