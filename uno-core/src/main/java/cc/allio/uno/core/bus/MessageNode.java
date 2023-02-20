package cc.allio.uno.core.bus;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

import cc.allio.uno.core.bus.event.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * 主题中消息节点
 *
 * @author jw
 * @date 2021/12/17 9:40
 */
@Slf4j
public class MessageNode<C> extends AbstractEventNode<C> {

    /**
     * 当前Node唯一订阅id，由{@link Subscription#getSubscribeId()}定义
     */
    private final Long subscriberId;

    /**
     * 当前Node订阅的Topic，由{@link Subscription#getPath()}定义
     */
    private final String topic;

    public MessageNode(Long subscribeId, String topic) {
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
        return registerListen(new Listener<C>() {
            @Override
            public void listen(EventNode<C> event, C obj) {
                if (obj != null) {
                    onNext.accept(obj);
                }
            }

            @Override
            public Class<? extends TopicEvent> getEventType() {
                return EmitEvent.class;
            }
        });
    }

    @Override
    public Long doLift(LongConsumer consumer) {
        return registerListen(new Listener<C>() {
            @Override
            public void listen(EventNode<C> event, C obj) {
                consumer.accept(getSubscribeId());
            }

            @Override
            public Class<? extends TopicEvent> getEventType() {
                return LiftEvent.class;
            }
        });
    }

    @Override
    public Long reply(@NonNull Class<? extends TopicEvent> eventType, @NonNull Consumer<C> consumer) {
        return registerListen(new Listener<C>() {
            @Override
            public void listen(EventNode<C> event, C obj) {
                try {
                    consumer.accept(obj);
                } catch (Throwable e) {
                    log.warn("Trigger event callback error", e);
                }
            }

            @Override
            public Class<? extends TopicEvent> getEventType() {
                return eventType;
            }
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MessageNode<C> that = (MessageNode<C>) o;
        return Objects.equals(subscriberId, that.subscriberId) && Objects.equals(topic, that.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriberId, topic);
    }
}
