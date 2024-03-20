package cc.allio.uno.core.bus;

/**
 * 主题事件
 *
 * @author j.x
 * @date 2023/4/25 14:30
 * @since 1.1.4
 */
public interface TopicEvent {

    /**
     * 获取topic key
     *
     * @return path
     */
    TopicKey getTopicKey();

    /**
     * 获取事件上下文
     *
     * @return ex
     */
    EventContext getEventContext();
}
