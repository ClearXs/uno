package cc.allio.uno.core.bus;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.bus.event.Node;
import cc.allio.uno.core.function.lambda.*;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

/**
 * {@link EventBus} watcher. it is available help handle async code verification.
 *
 * <ul>
 *     <li>{@link #untilComplete(Runnable)}: it's help all subscribe finish in relevant topic</li>
 * </ul>
 *
 * @author j.x
 * @since 1.2.0
 */
@Slf4j
public class Watcher<C extends EventContext> {

    private final EventBus<C> eventBus;
    private final String path;
    private final Lock lock;

    public Watcher(EventBus<C> eventBus, String path) {
        this.eventBus = eventBus;
        this.path = path;
        this.lock = new ReentrantLock();
    }

    /**
     * @see #watch(EventBus, String)
     */
    public static <C extends EventContext, T> Watcher<C> watch(ThrowingMethodConsumer<T> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(path);
    }

    /**
     * @see #watch(EventBus, String)
     */
    public static <C extends EventContext, T> Watcher<C> watch(ThrowingMethodSupplier<T> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(path);
    }

    /**
     * @see #watch(EventBus, String)
     */
    public static <C extends EventContext, T, R> Watcher<C> watch(ThrowingMethodFunction<T, R> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(path);
    }

    /**
     * @see #watch(EventBus, String)
     */
    public static <C extends EventContext, T1, T2> Watcher<C> watch(ThrowingMethodBiConsumer<T1, T2> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(path);
    }

    /**
     * @see #watch(EventBus, String)
     */
    public static <C extends EventContext, T1, T2, R> Watcher<C> watch(ThrowingMethodBiFunction<T1, T2, R> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(path);
    }

    /**
     * @see #watch(EventBus, String)
     */
    public static <C extends EventContext, T1, T2, T3> Watcher<C> watch(ThrowingMethodTerConsumer<T1, T2, T3> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(path);
    }

    /**
     * @see #watch(EventBus, String)
     */
    public static <C extends EventContext, T1, T2, T3, R> Watcher<C> watch(ThrowingMethodTerFunction<T1, T2, T3, R> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(path);
    }

    /**
     * @see #watch(EventBus, String)
     */
    public static <C extends EventContext, T1, T2, T3, T4> Watcher<C> watch(ThrowingMethodQueConsumer<T1, T2, T3, T4> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(path);
    }

    /**
     * @see #watch(EventBus, String)
     */
    public static <C extends EventContext, T1, T2, T3, T4, R> Watcher<C> watch(ThrowingMethodQueFunction<T1, T2, T3, T4, R> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(path);
    }

    /**
     * @see #watch(EventBus, String)
     */
    public static <C extends EventContext> Watcher<C> watch(String path) {
        return watch(EventBusFactory.current(), path);
    }

    /**
     * @see #watch(EventBus, String)
     */
    public static <C extends EventContext, T> Watcher<C> watch(EventBus<C> eventBus, ThrowingMethodConsumer<T> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(eventBus, path);
    }

    /**
     * @see #watch(EventBus, String)
     */
    public static <C extends EventContext, T> Watcher<C> watch(EventBus<C> eventBus, ThrowingMethodSupplier<T> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(eventBus, path);
    }

    /**
     * @see #watch(EventBus, String)
     */
    public static <C extends EventContext, T, R> Watcher<C> watch(EventBus<C> eventBus, ThrowingMethodFunction<T, R> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(eventBus, path);
    }

    /**
     * @see #watch(EventBus, String)
     */
    public static <C extends EventContext, T1, T2> Watcher<C> watch(EventBus<C> eventBus, ThrowingMethodBiConsumer<T1, T2> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(eventBus, path);
    }

    /**
     * @see #watch(EventBus, String)
     */
    public static <C extends EventContext, T1, T2, R> Watcher<C> watch(EventBus<C> eventBus, ThrowingMethodBiFunction<T1, T2, R> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(eventBus, path);
    }

    /**
     * @see #watch(EventBus, String)
     */
    public static <C extends EventContext, T1, T2, T3> Watcher<C> watch(EventBus<C> eventBus, ThrowingMethodTerConsumer<T1, T2, T3> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(eventBus, path);
    }

    /**
     * @see #watch(EventBus, String)
     */
    public static <C extends EventContext, T1, T2, T3, R> Watcher<C> watch(EventBus<C> eventBus, ThrowingMethodTerFunction<T1, T2, T3, R> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(eventBus, path);
    }

    /**
     * @see #watch(EventBus, String)
     */
    public static <C extends EventContext, T1, T2, T3, T4> Watcher<C> watch(EventBus<C> eventBus, ThrowingMethodQueConsumer<T1, T2, T3, T4> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(eventBus, path);
    }

    /**
     * @see #watch(EventBus, String)
     */
    public static <C extends EventContext, T1, T2, T3, T4, R> Watcher<C> watch(EventBus<C> eventBus, ThrowingMethodQueFunction<T1, T2, T3, T4, R> eventMethod) {
        String path = eventMethod.getMethodName();
        return watch(eventBus, path);
    }

    /**
     * watch specify topic path
     *
     * @param eventBus the event bus
     * @param path the topic path
     * @return watcher instance
     * @param <C> the {@link EventContext} type
     */
    public static <C extends EventContext> Watcher<C> watch(EventBus<C> eventBus, String path) {
        return new Watcher<>(eventBus, path);
    }

    /**
     * subscribe specify topic wither completion
     */
    public void untilComplete() {
        untilComplete(() -> {
        });
    }

    /**
     * subscribe specify topic until completion. the parameter {@link Runnable} is executable code.
     * allowing user code inside publish event to event bus (prerequisite is publishing relevant topic).
     *
     * @param executable the executable code
     * @see Sentinel
     */
    public void untilComplete(Runnable executable) {
        // execute user code
        try {
            executable.run();
        } catch (Throwable ex) {
            log.error("execute user code error, now immediate return", ex);
            return;
        }

        TopicKey signal = TopicKey.create(path + StringPool.SLASH + Sentinel.SIGNAL_STRING);
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
                        .thenMany(eventBus.subscribe(signal))
                        .flatMap(Node::onNext)
                        // either signal is complete.
                        .then(Mono.fromRunnable(unlock))
                        .subscribe();

        try {
            lock.lock();
            condition.await();
        } catch (Throwable ex) {
            log.error("watcher completion await error", ex);
            // enforce finish subscribe
            disposable.dispose();
        } finally {
            lock.unlock();
            // cancel subscribe signal topic
            eventBus.unSubscribe(signal).subscribe();
        }

        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
