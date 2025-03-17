package cc.allio.uno.core.bus.event;

import cc.allio.uno.core.bus.EventContext;
import cc.allio.uno.core.util.id.IdGenerator;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 监听事件
 *
 * @author j.x
 * @since 1.0
 */
public interface Listener<C extends EventContext> {

    /**
     * 监听某个事件的回调
     *
     * @param event 监听的事件对象
     * @param obj   监听参数
     */
    void listen(Node<C> event, C obj);

    /**
     * 获取事件类型
     *
     * @return {@link Event}Class对象
     */
    Class<? extends Event> getEventType();

    /**
     * Listener装饰器，存放ListenerId
     *
     * @param <C>
     */
    @EqualsAndHashCode(of = "listenerId")
    class IdListener<C extends EventContext> implements Listener<C> {

        @Getter
        private final long listenerId;

        private final Listener<C> listener;

        IdListener(Listener<C> listener) {
            this.listener = listener;
            this.listenerId = IdGenerator.defaultGenerator().getNextId();
        }

        @Override
        public void listen(Node<C> event, C obj) {
            listener.listen(event, obj);
        }

        @Override
        public Class<? extends Event> getEventType() {
            return listener.getEventType();
        }
    }
}
