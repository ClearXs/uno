package cc.allio.uno.core.task;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * 默认实现
 *
 * @author j.x
 * @since 1.1.4
 */
public class DefaultBufferRateTask<T> extends AbstractBufferRateTask<T> {

    private final Flux<T> upstream;
    private final int rate;
    private final int maxSize;
    private final Duration maxTimeout;
    private final BiPredicate<T, List<T>> flushCondition;
    private Disposable disposable;

    public DefaultBufferRateTask(Flux<T> upstream, int rate, Duration maxTimeout) {
        this(upstream, rate, 100, maxTimeout);
    }

    public DefaultBufferRateTask(Flux<T> upstream, int rate, int maxSize, Duration maxTimeout) {
        this(upstream, rate, maxSize, maxTimeout, (e, arr) -> arr.size() >= maxSize);
    }

    public DefaultBufferRateTask(Flux<T> upstream, int rate, int maxSize, Duration maxTimeout, BiPredicate<T, List<T>> flushCondition) {
        this.upstream = upstream;
        this.rate = rate;
        this.maxSize = maxSize;
        this.maxTimeout = maxTimeout;
        this.flushCondition = flushCondition;
    }

    @Override
    public void run() {
        disposable = bufferRate(upstream, rate, maxSize, maxTimeout, flushCondition).subscribe();
    }

    @Override
    public void complete() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
