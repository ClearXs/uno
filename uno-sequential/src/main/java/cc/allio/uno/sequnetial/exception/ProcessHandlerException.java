package cc.allio.uno.sequnetial.exception;

/**
 * 执行处理器异常
 *
 * @author j.x
 * @since 1.0
 */
public class ProcessHandlerException extends RuntimeException {

    public ProcessHandlerException() {
    }

    public ProcessHandlerException(String message) {
        super(message);
    }

    public ProcessHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessHandlerException(Throwable cause) {
        super(cause);
    }

    public ProcessHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
