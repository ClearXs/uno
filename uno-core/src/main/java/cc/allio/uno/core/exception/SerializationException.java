package cc.allio.uno.core.exception;

/**
 * 序列化时产生异常
 *
 * @author jiangwei
 * @date 2022/8/1 12:36
 * @since 1.0
 */
public class SerializationException extends RuntimeException {

    public SerializationException() {
        super();
    }

    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }

    protected SerializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
