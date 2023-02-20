package cc.allio.uno.core.exception;

/**
 * 调用Future产生的异常
 *
 * @author jiangw
 * @date 2020/12/8 18:00
 * @since 1.0
 */
public class InvokeFutureException extends Exception {

    public InvokeFutureException() {
    }

    public InvokeFutureException(String message) {
        super(message);
    }
}
