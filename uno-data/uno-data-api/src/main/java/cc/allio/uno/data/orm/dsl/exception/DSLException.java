package cc.allio.uno.data.orm.dsl.exception;

/**
 * SQL相关操作时抛出的异常，他是一个运行时异常
 *
 * @author jiangwei
 * @date 2022/9/30 10:44
 * @since 1.1.0
 */
public class DSLException extends RuntimeException {

    public DSLException() {
        super();
    }

    public DSLException(String message) {
        super(message);
    }

    public DSLException(String message, Throwable cause) {
        super(message, cause);
    }

    public DSLException(Throwable cause) {
        super(cause);
    }

    protected DSLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
