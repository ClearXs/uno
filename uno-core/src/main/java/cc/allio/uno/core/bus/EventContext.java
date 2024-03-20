package cc.allio.uno.core.bus;

import cc.allio.uno.core.api.OptionalContext;

/**
 * 消息上下文
 *
 * @author j.x
 * @date 2022/12/14 09:17
 * @since 1.1.2
 */
public interface EventContext extends OptionalContext {

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
    Topic<?> getTopic();

    /**
     * 获取事件追踪器
     *
     * @return EventTracer instance
     */
    EventTracer getEventTracer();
}
