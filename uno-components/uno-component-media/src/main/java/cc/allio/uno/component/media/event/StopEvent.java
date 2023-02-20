package cc.allio.uno.component.media.event;

import org.springframework.context.ApplicationEvent;

/**
 * 客户端停止推流事件
 *
 * @author jiangwei
 * @date 2022/3/30 14:46
 * @since 1.0.6
 */
public class StopEvent extends ApplicationEvent {

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public StopEvent(Stop source) {
        super(source);
    }
}
