package cc.allio.uno.data.orm.dsl.exception;

/**
 * DML exception 是一个运行时异常
 *
 * @author j.x
 * @since 1.1.7
 */
public class DMLException extends RuntimeException {

    public DMLException() {
        super();
    }

    public DMLException(String message) {
        super(message);
    }

    public DMLException(String message, Throwable cause) {
        super(message, cause);
    }

    public DMLException(Throwable cause) {
        super(cause);
    }

    protected DMLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
