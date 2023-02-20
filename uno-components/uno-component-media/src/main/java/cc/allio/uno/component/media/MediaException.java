package cc.allio.uno.component.media;

/**
 * 媒体异常
 *
 * @author jiangwei
 * @date 2022/3/30 09:52
 * @since 1.0
 */
public class MediaException extends RuntimeException {

    public MediaException() {
        super();
    }

    public MediaException(String message) {
        super(message);
    }

    public MediaException(Throwable cause) {
        super(cause);
    }

    public MediaException(String message, Throwable cause) {
        super(message, cause);
    }
}
