package cc.allio.uno.core.bus;

/**
 * {@link EventBus}Factory。L
 *
 * @author j.x
 * @since 1.1.4
 */
public class EventBusFactory {

    private static EventBus<? extends EventContext> bus;

    private EventBusFactory() {
    }

    /**
     * 获取EventBus实例。公用一个实例对象
     *
     * @return EventBus instance
     */
    public static <T extends EventContext> EventBus<T> get() {
        if (bus == null) {
            synchronized (EventBusFactory.class) {
                reset(newEventBus());
            }
        }
        return (EventBus<T>) bus;
    }

    /**
     * 新创建一个event bus实例
     *
     * @return event bus instance
     */
    public static EventBus<EventContext> newEventBus() {
        return new DefaultEventBus();
    }

    /**
     * 重新设置EventBus
     *
     * @param bus bus
     */
    public static void reset(EventBus<? extends EventContext> bus) {
        EventBusFactory.bus = bus;
    }
}
