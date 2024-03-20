package cc.allio.uno.core.exception;

/**
 * 实例化过程中异常
 *
 * @author j.x
 * @date 2023/3/6 10:33
 * @since 1.1.4
 */
public class InstantiationException extends RuntimeException {

    public InstantiationException() {
        super();
    }

    public InstantiationException(String message) {
        super(message);
    }

    public InstantiationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstantiationException(Throwable cause) {
        super(cause);
    }

    protected InstantiationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
