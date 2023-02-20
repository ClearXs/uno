package cc.allio.uno.component.netty.exception;

/**
 * screw
 * @author jiangw
 * @date 2020/12/10 17:33
 * @since 1.0
 */
public class RemoteTimeoutException extends RemoteException {

    public RemoteTimeoutException() {
        super();
    }

    public RemoteTimeoutException(String message) {
        super(message);
    }

    public RemoteTimeoutException(String address, long timeoutMill, Throwable cause) {
        super("wait response timeout: " + timeoutMill + " remote address：" + address + " cause：" + cause == null ? "" : cause.getMessage());
    }
}
