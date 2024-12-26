package cc.allio.uno.netty.exception;

/**
 * screw
 * @author j.x
 * @since 1.0
 */
public class RemoteSendException extends RemoteException {

    public RemoteSendException() {
        super();
    }

    public RemoteSendException(String message) {
        super(message);
    }

    public RemoteSendException(String address, Throwable cause) {
        super("send request failed, address：" + address + " cause：" + cause.getMessage());
    }
}
