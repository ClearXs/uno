package cc.allio.uno.http.metadata.exception;

/**
 * HTTP Swapper过程中发生错误抛出该异常
 *
 * @author j.x
 * @date 2022/8/25 09:54
 * @since 1.0
 */
public class SwapperException extends RuntimeException {

    public SwapperException() {
        super();
    }

    public SwapperException(String message) {
        super(message);
    }

    public SwapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public SwapperException(Throwable cause) {
        super(cause);
    }

    protected SwapperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
