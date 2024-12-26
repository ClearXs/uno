package cc.allio.uno.netty.exception;

/**
 * screw
 * @author j.x
 * @since 1.0
 */
public class RemoteException extends Exception {

    public RemoteException() {
        super();
    }

    public RemoteException(String message) {
        super(message);
    }

    public RemoteException(Throwable cause) {
        super(cause);
    }
}
