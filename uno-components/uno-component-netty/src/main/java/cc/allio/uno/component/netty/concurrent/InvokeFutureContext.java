package cc.allio.uno.component.netty.concurrent;

import cc.allio.uno.component.netty.exception.InvokeFutureException;
import lombok.NonNull;

/**
 * 存储当前异步的上下文。
 *
 * @author jiangw
 * @date 2020/12/8 11:40
 * @since 1.0
 */
public class InvokeFutureContext {

    private static final ThreadLocal<InvokeFuture<?>> LOCAL_FUTURE = new ThreadLocal<>();

    public static <V> InvokeFuture<V> get(@NonNull Class<V> expectedClass) throws InvokeFutureException {
        InvokeFuture<?> invokeFuture = LOCAL_FUTURE.get();
        LOCAL_FUTURE.remove();
        if (invokeFuture == null) {
            throw new InvokeFutureException("future is empty");
        }
        Class<?> realClass = invokeFuture.realClass();
        boolean assignable = realClass.isAssignableFrom(expectedClass);
        if (!assignable) {
            throw new IllegalArgumentException("expected class: " + expectedClass.getName() + " not need class: " + realClass.getName());
        }
        return (InvokeFuture<V>) invokeFuture;
    }

    public static void set(InvokeFuture<?> future) {
        LOCAL_FUTURE.set(future);
    }
}
