package cc.allio.uno.component.media.event;

import org.springframework.context.ApplicationEvent;

/**
 * 客户端播放视频后事件
 *
 * @author jiangwei
 * @date 2022/3/30 14:45
 * @since 1.0.6
 */
public class PlayEvent extends ApplicationEvent {

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public PlayEvent(Play source) {
        super(source);
    }
}
