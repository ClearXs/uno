package cc.allio.uno.core.bus.event;

import cc.allio.uno.core.util.id.IdGenerator;

import java.util.Objects;

/**
 * 监听事件
 *
 * @author j.x
 * @since 1.0
 */
public interface Listener<C> {

    /**
     * 监听某个事件的回调
     *
     * @param event 监听的事件对象
     * @param obj   监听参数
     */
    void listen(Node<C> event, C obj) ;

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
    class IdListener<C> implements Listener<C> {

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

        public long getListenerId() {
            return listenerId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IdListener<?> that = (IdListener<?>) o;
            return listenerId == that.listenerId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(listenerId);
        }
    }
}
