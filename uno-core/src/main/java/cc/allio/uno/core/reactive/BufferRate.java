package cc.allio.uno.core.reactive;

import lombok.NonNull;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;

/**
 * 缓冲速率控制
 *
 * @author j.x
 * @since 1.1.4
 */
public abstract class BufferRate {

    /**
     * 缓冲区最大阈值
     */
    public static final int MAX_SIZE = 1000;

    /**
     * 缓冲区默认阈值
     */
    public static final int DEFAULT_SIZE = 100;

    /**
     * 流节点之间默认处理速率
     */
    public static final int DEFAULT_RATE = 1000;

    /**
     * 最下处理速率
     */
    public static final int MIN_RATE = 100;

    /**
     * 缓冲区默认超时时间
     */
    public static final Duration DEFAULT_TIMEOUT = Duration.ofMillis(1000L);

    /**
     * 最小超时时间
     */
    public static final Duration MIN_TIMEOUT = Duration.ofMillis(100L);

    public static <T> Flux<List<T>> create(Flux<T> flux) {
        return create(flux, DEFAULT_RATE, DEFAULT_SIZE, DEFAULT_TIMEOUT);
    }

    public static <T> Flux<List<T>> create(Flux<T> flux,
                                           int rate) {
        return create(flux, rate, DEFAULT_SIZE, DEFAULT_TIMEOUT);
    }

    public static <T> Flux<List<T>> create(Flux<T> flux,
                                           int rate,
                                           Duration maxTimeout) {
        return create(flux, rate, DEFAULT_SIZE, maxTimeout);
    }

    public static <T> Flux<List<T>> create(Flux<T> flux,
                                           int rate,
                                           int maxSize,
                                           Duration maxTimeout) {
        return Flux.create(sink -> {
            BufferRateSubscriber<T> subscriber = new BufferRateSubscriber<>(sink, maxSize, rate, maxTimeout, (e, arr) -> arr
                    .size() >= maxSize);

            flux.elapsed().subscribe(subscriber);

            sink.onDispose(subscriber);
        });
    }

    public static <T> Flux<List<T>> create(Flux<T> flux,
                                           int rate,
                                           int maxSize,
                                           Duration maxTimeout,
                                           BiPredicate<T, List<T>> flushCondition) {
        return Flux.create(sink -> {
            BufferRateSubscriber<T> subscriber = new BufferRateSubscriber<>(sink, maxSize, rate, maxTimeout, (e, arr) -> flushCondition
                    .test(e, arr) || arr.size() >= maxSize);

            flux.elapsed().subscribe(subscriber);

            sink.onDispose(subscriber);
        });

    }

    /**
     * 可缓存速率的订阅者，参考自Jetlinks时序数据的保存
     *
     * @param <T> 实时数据范型
     */
    static class BufferRateSubscriber<T> extends BaseSubscriber<Tuple2<Long, T>> {

        /**
         * 缓存区大小
         */
        int bufferSize;
        int rate;

        volatile List<T> bufferArray;
        FluxSink<List<T>> sink;

        Duration timeout;
        Scheduler timer = Schedulers.parallel();
        Disposable timerDispose;

        private final BiPredicate<T, List<T>> flushCondition;

        BufferRateSubscriber(FluxSink<List<T>> sink,
                             int bufferSize,
                             int rate,
                             Duration timeout,
                             BiPredicate<T, List<T>> flushCondition) {
            this.sink = sink;
            this.bufferSize = bufferSize;
            this.rate = rate;
            this.timeout = timeout;
            this.flushCondition = flushCondition;
            newBuffer();
        }

        protected List<T> newBuffer() {
            List<T> buffer = bufferArray;
            bufferArray = new ArrayList<>(bufferSize);
            return buffer;
        }

        @Override
        protected void hookFinally(@NonNull SignalType type) {
            doFlush();
        }

        void doFlush() {
            if (!bufferArray.isEmpty()) {
                sink.next(newBuffer());
            }
            request(bufferSize);
            if (timerDispose != null && !timerDispose.isDisposed()) {
                timerDispose.dispose();
            }
        }

        @Override
        protected void hookOnSubscribe(@NonNull Subscription subscription) {
            request(bufferSize);
        }

        @Override
        protected void hookOnNext(Tuple2<Long, T> value) {
            bufferArray.add(value.getT2());
            if (value.getT1() > rate) {
                doFlush();
            } else {
                if (flushCondition.test(value.getT2(), bufferArray)) {
                    doFlush();
                } else {
                    if (timerDispose == null || timerDispose.isDisposed()) {
                        timerDispose = timer.schedule(this::doFlush, timeout.toMillis(), TimeUnit.MILLISECONDS);
                    }
                }
            }
        }
    }
}
