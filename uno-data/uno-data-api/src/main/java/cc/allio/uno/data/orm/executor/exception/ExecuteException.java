package cc.allio.uno.data.orm.executor.exception;

import cc.allio.uno.data.orm.executor.CommandExecutor;

/**
 * {@link ExecuteException} is a record execute command error in {@link CommandExecutor}.
 * <p>it is a {@link Throwable}, make sure invoker handle the Exception</p>
 *
 * @author j.x
 * @since 1.1.8
 */
public class ExecuteException extends Throwable {

    public ExecuteException() {
    }

    public ExecuteException(String message) {
        super(message);
    }

    public ExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecuteException(Throwable cause) {
        super(cause);
    }

    public ExecuteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
