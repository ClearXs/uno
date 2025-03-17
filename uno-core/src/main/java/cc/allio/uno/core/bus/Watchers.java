package cc.allio.uno.core.bus;

import cc.allio.uno.core.function.lambda.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Watchers {

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
     * @param path     the topic path
     * @param <C>      the {@link EventContext} type
     * @return watcher instance
     */
    public static <C extends EventContext> Watcher<C> watch(EventBus<C> eventBus, String path) {
        return new Watcher.InnerWatcher<>(eventBus, path);
    }

}
