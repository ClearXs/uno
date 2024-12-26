package cc.allio.uno.core.bus;

import lombok.Getter;

/**
 * 上下文{@link EventContext}的抽象类
 *
 * @author j.x
 * @since 1.1.4
 */
public abstract class ContextTopicEvent implements TopicEvent {

    @Getter
    private final EventContext eventContext;

    protected ContextTopicEvent(EventContext eventContext) {
        this.eventContext = eventContext;
    }
}
