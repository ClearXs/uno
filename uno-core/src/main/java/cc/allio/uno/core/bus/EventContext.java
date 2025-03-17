package cc.allio.uno.core.bus;

import cc.allio.uno.core.util.map.OptionalMap;

/**
 * 消息上下文
 *
 * @author j.x
 * @since 1.1.2
 */
public interface EventContext extends OptionalMap<String> {

    /**
     * 事件主题定义Key
     */
    String TOPIC_PATH_KEY = "TOPIC_PATH";
    String TOPIC_KEY = "TOPIC";

    /**
     * 获取事件主题路径
     *
     * @return topic path or empty
     */
    String getTopicPath();

    /**
     * 获取事件主题
     *
     * @return Topic instance or null
     */
    Topic<? extends EventContext> getTopic();

    /**
     * 获取事件追踪器
     *
     * @return EventTracer instance
     */
    EventTracer getEventTracer();

    static EventContext defaultEventContext() {
        return new DefaultEventContext();
    }
}
