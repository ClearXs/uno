package cc.allio.uno.netty.exception;

/**
 * screw
 * @author jiangw
 * @date 2020/12/10 17:33
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
