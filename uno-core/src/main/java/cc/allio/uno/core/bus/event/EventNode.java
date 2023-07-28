package cc.allio.uno.core.bus.event;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

import cc.allio.uno.core.bus.Subscription;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * 主题中事件节点
 *
 * @author jw
 * @date 2021/12/17 9:40
 */
@Slf4j
public abstract class EventNode<C> extends AbstractEventNode<C> {

    /**
     * 当前Node唯一订阅id，由{@link Subscription#getSubscribeId()}定义
     */
    protected final Long subscriberId;

    /**
     * 当前Node订阅的Topic，由{@link Subscription#getPath()}定义
     */
    protected final String topic;

    protected EventNode(Long subscribeId, String topic) {
        this.subscriberId = subscribeId;
        this.topic = topic;
    }

    @Override
    public Long getSubscribeId() {
        return this.subscriberId;
    }

    @Override
    public String getTopic() {
        return this.topic;
    }

    @Override
    public Long doEmmit(Consumer<C> onNext) {
        Listener<C> emitLis = new Listener<C>() {
            @Override
            public void listen(Node<C> event, C obj) {
                if (obj != null) {
                    onNext.accept(obj);
                }
            }

            @Override
            public Class<? extends BusEvent> getEventType() {
                return EmitEvent.class;
            }
        };
        return registerListen(emitLis);
    }

    @Override
    public Long doLift(LongConsumer consumer) {
        Listener<C> listLis = new Listener<C>() {
            @Override
            public void listen(Node<C> event, C obj) {
                consumer.accept(getSubscribeId());
            }

            @Override
            public Class<? extends BusEvent> getEventType() {
                return LiftEvent.class;
            }
        };
        return registerListen(listLis);
    }

    @Override
    public Long reply(@NonNull Class<? extends BusEvent> eventType, @NonNull Consumer<C> consumer) {
        Listener<C> triggerListener = new Listener<C>() {
            @Override
            public void listen(Node<C> event, C obj) {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("subscribe id {}, subscribe topic {}, then emit obj is {} ", subscriberId, topic, obj);
                    }
                    consumer.accept(obj);
                } catch (Throwable err) {
                    log.warn("Trigger event callback error", err);
                }
            }

            @Override
            public Class<? extends BusEvent> getEventType() {
                return eventType;
            }
        };
        return registerListen(triggerListener);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventNode<C> that = (EventNode<C>) o;
        return Objects.equals(subscriberId, that.subscriberId) && Objects.equals(topic, that.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriberId, topic);
    }
}
