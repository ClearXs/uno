package cc.allio.uno.netty.event;

/**
 * 观察者
 * @author j.x
 * @since 1.0
 */
public interface Observer {

    /**
     * 当被观察者发生改变时，调用方法
     * @param observable 被观察者
     * @param args 传递的参数
     */
    void update(Observable observable, Object... args);
}
