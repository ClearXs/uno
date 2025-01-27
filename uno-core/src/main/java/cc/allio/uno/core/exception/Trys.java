package cc.allio.uno.core.exception;

import cc.allio.uno.core.function.lambda.ThrowingMethodConsumer;
import cc.allio.uno.core.function.lambda.ThrowingMethodFunction;
import cc.allio.uno.core.function.lambda.ThrowingMethodSupplier;
import cc.allio.uno.core.function.lambda.ThrowingRunnable;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * help for handle exception operation.
 *
 * @author j.x
 * @since 0.2.0
 */
@Slf4j
public final class Trys {

    /**
     * try to invoke specific function, if exist error resolve it. if handle failed to it. then throwing runtime exception.
     *
     * @param func    the func handle
     * @param onError the error handle
     * @throws RuntimeException
     */
    public static void onCapture(ThrowingRunnable func, ThrowingMethodConsumer<Throwable> onError) {
        try {
            func.run();
        } catch (Throwable ex) {
            try {
                onError.accept(ex);
            } catch (Throwable ex2) {
                // convert runtime exception
                throw Exceptions.unchecked(ex2);
            }
        }
    }

    /**
     * try to invoke specific function, if exist error resolve it. if handle failed to it. then throwing runtime exception.
     *
     * @param func    the func handle
     * @param onError the error handle
     * @param <V>     the return type
     * @return
     */
    public static <V> V onCapture(ThrowingMethodSupplier<V> func, ThrowingMethodConsumer<Throwable> onError) {
        try {
            return func.get();
        } catch (Throwable ex) {
            try {
                onError.accept(ex);
            } catch (Throwable ex2) {
                // convert runtime exception
                throw Exceptions.unchecked(ex2);
            }
        }
        return null;
    }

    /**
     * try to invoke specific function, if exist error resolve it. if handle failed to it. then throwing runtime exception.
     *
     * @param func    the func handle
     * @param onError the error handle
     * @param <V>
     * @return
     */
    public static <V> V onCapture(ThrowingMethodSupplier<V> func, ThrowingMethodFunction<Throwable, V> onError) {
        try {
            return func.get();
        } catch (Throwable ex) {
            try {
                return onError.apply(ex);
            } catch (Throwable ex2) {
                // convert runtime exception
                throw Exceptions.unchecked(ex2);
            }
        }
    }

    /**
     * try to invoke specific function, if exist error then throwing.
     *
     * @param func the func handle
     * @throws Throwable if exist error
     */
    public static void onError(ThrowingRunnable func) throws Throwable {
        func.run();
    }

    /**
     * try to invoke specific function, if exist error then throwing.
     *
     * @param func the func handle
     * @param <V>  the return type
     * @return the result
     * @throws Throwable if exist error
     */
    public static <V> V onError(ThrowingMethodSupplier<V> func) throws Throwable {
        return func.get();
    }

    /**
     * try to invoke specific function, if exist error then log it. (ignore this error)
     *
     * @param func the func handle
     */
    public static void onContinue(ThrowingRunnable func) {
        onContinue(func, Trys::logError);
    }

    /**
     * try to invoke specific function, if exist error resolve from user.
     *
     * @param func    the func handle
     * @param onError the error handle
     */
    public static void onContinue(ThrowingRunnable func, Consumer<Throwable> onError) {
        try {
            func.run();
        } catch (Throwable ex) {
            onError.accept(ex);
        }
    }

    /**
     * try to invoke specific function, if exist error then log it. (ignore this error)
     *
     * @param func the func handle
     * @param <V>  the return type
     * @return the result
     */
    public static <V> V onContinue(ThrowingMethodSupplier<V> func) {
        return onContinue(func, Trys::logError);
    }

    /**
     * try to invoke specific function, if exist error resolve from user.
     *
     * @param func    the func handle
     * @param onError the error handle
     * @param <V>     the return type
     * @return
     */
    public static <V> V onContinue(ThrowingMethodSupplier<V> func, Consumer<Throwable> onError) {
        try {
            return func.get();
        } catch (Throwable ex) {
            onError.accept(ex);
            return null;
        }
    }

    private static void logError(Throwable err) {
        log.error("failed to operate", err);
    }
}
