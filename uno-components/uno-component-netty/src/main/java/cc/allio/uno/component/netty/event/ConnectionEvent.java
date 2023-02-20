package cc.allio.uno.component.netty.event;

/**
 * 连接发生的事件
 *
 * @author jiangw
 * @date 2021/4/16 15:09
 * @since 1.0
 */
public class ConnectionEvent extends BaseEvent {

    /**
     * 连接接收事件
     *
     * @param accept 接收对象
     */
    public void accept(Object accept) {
        notifyObservers(accept);
    }

    /**
     * 连接拒绝事件
     *
     * @param ex 异常对象
     */
    public void reject(Throwable ex) {
        notifyObservers(ex);
    }
}
