package cc.allio.uno.component.media.event;

import org.springframework.context.ApplicationEvent;

/**
 * 结束视频录制生产flv文件后回调
 *
 * @author jiangwei
 * @date 2022/3/30 14:45
 * @since 1.0.6
 */
public class DvrEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public DvrEvent(Dvr source) {
        super(source);
    }
}
