package cc.allio.uno.component.netty.event;

/**
 * 观察者
 * @author jiangw
 * @date 2021/4/16 14:56
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
