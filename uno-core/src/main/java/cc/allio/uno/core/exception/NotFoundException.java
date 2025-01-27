package cc.allio.uno.core.exception;

/**
 * refer in general all not found exception.
 * <p>
 * like as:
 * <ol>
 *     <li>Type not found</li>
 *     <li>Bean not found</li>
 * </ol>
 *
 * @author j.x
 * @since 1.2.0
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    protected NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
