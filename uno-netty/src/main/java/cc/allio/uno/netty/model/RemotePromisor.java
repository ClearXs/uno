package cc.allio.uno.netty.model;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 远程调用的promisor
 * @author j.x
 * @since 1.0
 */
public class RemotePromisor {

    /**
     * 一个promise对象
     * 一个调用链：request->wait->response->run->return promise
     */
    private volatile RemoteTransporter transporterPromise;

    /**
     * 请求超时时间
     */
    private final long timeout;

    /**
     * 是否成功发送
     */
    private boolean isSendSuccessful;

    /**
     * 发送不成功的原因
     */
    private Throwable cause;

    /**
     * 实现等待阻塞
     */
    private final CountDownLatch blocked = new CountDownLatch(1);

    public RemotePromisor() {
        this(0L);
    }

    public RemotePromisor(long timeout) {
        this.timeout = timeout;
    }

    public boolean isSendSuccessful() {
        return isSendSuccessful;
    }

    public void setSendSuccessful(boolean sendSuccessful) {
        isSendSuccessful = sendSuccessful;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public RemoteTransporter getTransporter() throws InterruptedException {
        if (timeout == 0L) {
            blocked.await();
        } else {
            boolean await = blocked.await(timeout, TimeUnit.MILLISECONDS);
            if (await) {
                return transporterPromise;
            }
        }
        return transporterPromise;
    }

    public void putTransporter(RemoteTransporter remoteTransporter) {
        this.transporterPromise = remoteTransporter;
        blocked.countDown();
    }
}
