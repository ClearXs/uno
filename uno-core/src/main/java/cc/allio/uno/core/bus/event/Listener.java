package cc.allio.uno.core.bus.event;

import cc.allio.uno.core.util.id.IdGenerator;

import java.util.Objects;

/**
 * 监听事件
 *
 * @author jiangwei
 * @date 2021/12/19 12:27 PM
 * @since 1.0
 */
public interface Listener<C> {

    /**
     * 监听某个事件的回调
     *
     * @param event 监听的事件对象
     * @param obj   监听参数
     * @throws Throwable 监听回调过程发生异常抛出
     */
    void listen(Node<C> event, C obj) ;

    /**
     * 获取事件类型
     *
     * @return {@link BusEvent}Class对象
     */
    Class<? extends BusEvent> getEventType();

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
        public Class<? extends BusEvent> getEventType() {
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
