package cc.allio.uno.data.sql;

/**
 * SQL相关操作时抛出的异常，他是一个运行时异常
 *
 * @author jiangwei
 * @date 2022/9/30 10:44
 * @since 1.1.0
 */
public class SQLException extends RuntimeException {

    public SQLException() {
        super();
    }

    public SQLException(String message) {
        super(message);
    }

    public SQLException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLException(Throwable cause) {
        super(cause);
    }

    protected SQLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
