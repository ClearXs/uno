package cc.allio.uno.data.orm.dsl.exception;

/**
 * DDL exception 是一个运行时异常
 *
 * @author jiangwei
 * @date 2024/2/8 13:57
 * @since 1.1.7
 */
public class DDLException extends RuntimeException {

    public DDLException() {
        super();
    }

    public DDLException(String message) {
        super(message);
    }

    public DDLException(String message, Throwable cause) {
        super(message, cause);
    }

    public DDLException(Throwable cause) {
        super(cause);
    }

    protected DDLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
