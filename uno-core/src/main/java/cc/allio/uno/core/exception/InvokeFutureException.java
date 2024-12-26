package cc.allio.uno.core.exception;

/**
 * 调用Future产生的异常
 *
 * @author j.x
 * @since 1.0
 */
public class InvokeFutureException extends Exception {

    public InvokeFutureException() {
    }

    public InvokeFutureException(String message) {
        super(message);
    }
}
