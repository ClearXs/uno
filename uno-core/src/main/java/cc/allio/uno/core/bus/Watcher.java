package cc.allio.uno.core.bus;

import cc.allio.uno.core.api.Self;
import cc.allio.uno.core.function.EmptyRunnable;
import cc.allio.uno.core.function.lambda.*;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {@link EventBus} watcher. it is available help handle async code verification.
 *
 * <ul>
 *     <li>{@link #untilComplete(Runnable)}: it's help all subscribe finish in relevant topic</li>
 * </ul>
 *
 * @param <C> the {@link EventContext} type
 * @author j.x
 * @since 1.2.1
 */
public interface Watcher<C extends EventContext> {

    /**
     * @see #watch(String)
     */
    default <T> Watcher<C> watch(ThrowingMethodConsumer<T> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(path);
    }

    /**
     * @see #watch(String)
     */
    default <T> Watcher<C> watch(ThrowingMethodSupplier<T> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(path);
    }

    /**
     * @see #watch(String)
     */
    default <T, R> Watcher<C> watch(ThrowingMethodFunction<T, R> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(path);
    }

    /**
     * @see #watch(String)
     */
    default <T1, T2> Watcher<C> watch(ThrowingMethodBiConsumer<T1, T2> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(path);
    }

    /**
     * @see #watch(String)
     */
    default <T1, T2, R> Watcher<C> watch(ThrowingMethodBiFunction<T1, T2, R> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(path);
    }

    /**
     * @see #watch(String)
     */
    default <T1, T2, T3> Watcher<C> watch(ThrowingMethodTerConsumer<T1, T2, T3> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(path);
    }

    /**
     * @see #watch(String)
     */
    default <T1, T2, T3, R> Watcher<C> watch(ThrowingMethodTerFunction<T1, T2, T3, R> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(path);
    }

    /**
     * @see #watch(String)
     */
    default <T1, T2, T3, T4> Watcher<C> watch(ThrowingMethodQueConsumer<T1, T2, T3, T4> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(path);
    }

    /**
     * @see #watch(String)
     */
    default <T1, T2, T3, T4, R> Watcher<C> watch(ThrowingMethodQueFunction<T1, T2, T3, T4, R> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(path);
    }

    /**
     * watch specify topic path
     *
     * @param path the topic path
     * @return watcher instance
     */
    Watcher<C> watch(String path);

    /**
     * @see #untilComplete(Runnable, Runnable)
     */
    default void doUntilComplete() {
        untilComplete(null, null).doOn();
    }

    /**
     * @see #untilComplete(Runnable, Runnable)
     */
    default void doUntilComplete(Runnable executable) {
        untilComplete(null, executable).doOn();
    }

    /**
     * @see #untilComplete(Runnable, Runnable)
     */
    default void doUntilComplete(Runnable trigger, Runnable executable) {
        untilComplete(trigger, executable).doOn();
    }

    /**
     * @see #untilComplete(Runnable, Runnable)
     */
    default UntilComplete<C> untilComplete() {
        return untilComplete(null, null);
    }

    /**
     * @see #untilComplete(Runnable, Runnable)
     */
    default UntilComplete<C> untilComplete(Runnable executable) {
        return untilComplete(null, executable);
    }

    /**
     * lock context until existing toward specific {@code topic} publish, the context wakeup.
     *
     * @param trigger    when lock context invoke. you maybe toward event bus publishing same topic context and will be wakeup context.
     * @param executable when wakeup will be invoking.
     * @see UntilComplete
     */
    UntilComplete<C> untilComplete(Runnable trigger, Runnable executable);

    /**
     * the default implementation {@link Watcher}
     *
     * @param <C>
     */
    class InnerWatcher<C extends EventContext> implements Watcher<C> {

        private final EventBus<C> eventBus;
        private final String path;

        public InnerWatcher(EventBus<C> eventBus, String path) {
            this.eventBus = eventBus;
            this.path = path;
        }

        @Override
        public Watcher<C> watch(String path) {
            return new InnerWatcher<>(eventBus, path);
        }

        @Override
        public UntilComplete<C> untilComplete(Runnable trigger, Runnable executable) {
            return new UntilComplete<>(path, eventBus).whenTrigger(trigger).executable(executable);
        }
    }

    /**
     * subscribe specify topic until completion. the parameter {@link Runnable} is user executable code.
     *
     * <p>
     * allowing trigger code inside publish event to event bus (prerequisite is publishing relevant topic).
     *
     * @see Sentinel
     */
    @Slf4j
    class UntilComplete<C extends EventContext> implements Self<UntilComplete<C>> {

        final Lock lock;
        final Subscription subscription;
        final String path;
        final EventBus<C> eventBus;

        private Runnable executable;
        private Runnable trigger;

        public UntilComplete(String path, EventBus<C> eventBus) {
            this.path = path;
            this.subscription = Subscription.of(Sentinel.SignalWorker.createTopicKey(path));
            this.eventBus = eventBus;
            this.lock = new ReentrantLock();
            this.executable = new EmptyRunnable();
            this.trigger = new EmptyRunnable();
        }

        /**
         * when event bus notify. the executable will be executed.
         *
         * @param executable the user executable code.
         * @return self
         */
        public UntilComplete<C> executable(Runnable executable) {
            this.executable = Optional.ofNullable(executable).orElse(new EmptyRunnable());
            return self();
        }

        /**
         * announce when trigger publish
         *
         * @param trigger the trigger
         * @return self
         */
        public UntilComplete<C> whenTrigger(Runnable trigger) {
            this.trigger = Optional.ofNullable(trigger).orElse(new EmptyRunnable());
            return self();
        }

        public void doOn() {
            Condition condition = lock.newCondition();
            Runnable unlock =
                    () -> {
                        lock.lock();
                        try {
                            condition.signalAll();
                        } finally {
                            lock.unlock();
                        }
                    };

            Disposable disposable =
                    eventBus.findTopic(path)
                            .flatMap(topic -> {
                                // ensure existing topic and subscriber
                                if (topic.size() > 0) {
                                    return Mono.just(topic.size());
                                } else {
                                    return Mono.empty();
                                }
                            })
                            // if topic is empty or subscriber is empty. the watch is complete.
                            .switchIfEmpty(Mono.fromRunnable(unlock))
                            // subscribe signal of topic.
                            .thenMany(eventBus.subscribe(subscription))
                            // either signal is complete.
                            .flatMap(c -> Mono.fromRunnable(unlock))
                            .subscribe();

            AtomicBoolean wakeup = new AtomicBoolean(true);
            try {
                lock.lock();
                // try to use trigger event bus publish signal before locking
                CompletableFuture.runAsync(trigger, Executors.newVirtualThreadPerTaskExecutor());
                condition.await();
            } catch (Throwable ex) {
                log.error("watcher completion await error", ex);
                // enforce finish subscribe
                disposable.dispose();
                wakeup.compareAndSet(true, false);
            } finally {
                lock.unlock();
                // cancel subscribe signal topic
                eventBus.unSubscribe(subscription).subscribe();
            }

            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
            // execute user code if correct wakeup
            if (wakeup.get()) {
                try {
                    executable.run();
                } catch (Throwable ex) {
                    log.error("execute user code error, now immediate return", ex);
                }
            }
        }
    }
}
