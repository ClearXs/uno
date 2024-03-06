package cc.allio.uno.core.exception;

import cc.allio.uno.core.util.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 异常处理工具类
 *
 * @author jiangwei
 */
public abstract class Exceptions {

    private Exceptions() {
    }

    /**
     * 将CheckedException转换为UncheckedException.
     *
     * @param e Throwable
     * @return {RuntimeException}
     */
    public static RuntimeException unchecked(Throwable e) {
        if (e instanceof Error) {
            throw (Error) e;
        } else if (e instanceof IllegalAccessException ||
                e instanceof IllegalArgumentException ||
                e instanceof NoSuchMethodException) {
            return new IllegalArgumentException(e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException(((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else if (e instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }
        return Exceptions.runtime(e);
    }

    /**
     * 根据指定抛出的异常类型来进行实例化返回
     *
     * @param message 异常消息
     * @param exClass 抛出异常类型
     * @param <T>     异常类型
     * @return throwable instance
     */
    public static <T extends Throwable> T eee(String message, Class<T> exClass) {
        return ClassUtils.newInstance(exClass, message);
    }

    /**
     * 根据指定抛出的异常类型来进行实例化返回
     *
     * @param cause   异常原因
     * @param exClass 抛出异常类型
     * @param <T>     异常类型
     * @return throwable instance
     */
    public static <T extends Throwable> T eee(Throwable cause, Class<T> exClass) {
        return ClassUtils.newInstance(exClass, cause);
    }

    /**
     * 根据指定抛出的异常类型来进行实例化返回
     *
     * @param message 异常消息
     * @param cause   异常原因
     * @param exClass 抛出异常类型
     * @param <T>     异常类型
     * @return throwable instance
     */
    public static <T extends Throwable> T eee(String message, Throwable cause, Class<T> exClass) {
        return ClassUtils.newInstance(exClass, message, cause);
    }

    /**
     * 不采用 RuntimeException 包装，直接抛出，使异常更加精准
     *
     * @param throwable Throwable
     * @param <T>       泛型标记
     * @return Throwable
     * @throws T 泛型
     */
    @SuppressWarnings("unchecked")
    private static <T extends Throwable> T runtime(Throwable throwable) throws T {
        throw (T) throwable;
    }

    /**
     * 代理异常解包
     *
     * @param wrapped 包装过得异常
     * @return 解包后的异常
     */
    public static Throwable unwrap(Throwable wrapped) {
        Throwable unwrapped = wrapped;
        while (true) {
            if (unwrapped instanceof InvocationTargetException) {
                unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
            } else if (unwrapped instanceof UndeclaredThrowableException) {
                unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
            } else {
                return unwrapped;
            }
        }
    }

    /**
     * 创建{@link NullPointerException}异常
     *
     * @param message 异常消息
     * @return message
     */
    public static NullPointerException unNull(String message) {
        return new NullPointerException(message);
    }

    /**
     * 创建{@link UnsupportedOperationException}异常
     *
     * @param message 异常消息
     * @return message
     */
    public static UnsupportedOperationException unOperate(String message) {
        return new UnsupportedOperationException(message);
    }

    /**
     * 判断给定的异常类型是否是未受检查的类型
     *
     * @return true if exception is unchecked
     */
    public static boolean isUnchecked(Class<? extends Throwable> exClass) {
        return exClass != null && RuntimeException.class.isAssignableFrom(exClass);
    }
}
