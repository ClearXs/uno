package cc.allio.uno.netty.event;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 基本事件
 *
 * @author jiangw
 * @date 2021/4/16 14:59
 * @since 1.0
 */
public abstract class BaseEvent implements Event {

    private final CopyOnWriteArraySet<Observer> observers;

    protected BaseEvent() {
        observers = new CopyOnWriteArraySet<>();
    }

    @Override
    public void attach(Observer observer) {
        if (observer == null) {
            return;
        }
        observers.add(observer);
    }

    @Override
    public boolean detach(Observer observer) {
        if (observer == null) {
            return false;
        }
        return observers.remove(observer);
    }

    @Override
    public void notifyObservers(Object... args) {
        for (Observer observer : observers) {
            observer.update(this, args);
        }
    }

    @Override
    public String toString() {
        return "ScrewEvent{" +
                "observers=" + observers +
                '}';
    }
}
