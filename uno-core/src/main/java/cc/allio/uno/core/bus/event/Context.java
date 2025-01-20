package cc.allio.uno.core.bus.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 事件上下文
 *
 * @author j.x
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class Context<C> {

    /**
     * 事件传递数据源
     */
    private C source;

    /**
     * 事件类型
     */
    private Class<? extends Event> topicEvent;

    /**
     * 事件观察者对象
     */
    private Node<C> node;

    @Override
    public String toString() {
        return "EventContext{" +
                "source=" + source +
                ", topicEvent=" + topicEvent +
                '}';
    }
}
