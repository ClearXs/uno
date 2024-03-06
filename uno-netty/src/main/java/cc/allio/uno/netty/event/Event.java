package cc.allio.uno.netty.event;

/**
 * <b>event 定义. 简单实现的观察者模式</b>
 * <pre>
 *     参考自JDK{@link java.util.Observable}
 * </pre>
 *
 * @author jiangwei
 * @date 2023/1/29 13:44
 * @see ConnectionEvent
 * @see Observer
 * @since 1.1.4
 */
public interface Event extends Observable {

    /**
     * 添加观察者
     *
     * @param observer 观察者
     */
    void attach(Observer observer);

    /**
     * 移除观察者
     *
     * @param observer 观察者
     * @return true 移除成功 false 移除失败
     */
    boolean detach(Observer observer);

    /**
     * 通知所有的观察者
     *
     * @param args 被观测参数
     */
    void notifyObservers(Object... args);
}
