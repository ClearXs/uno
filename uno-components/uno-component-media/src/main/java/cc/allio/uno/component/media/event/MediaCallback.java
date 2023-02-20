package cc.allio.uno.component.media.event;

import org.springframework.context.ApplicationEvent;

/**
 * 多媒体事件回调
 *
 * @author jiangwei
 * @date 2022/3/30 11:30
 * @since 1.0.6
 */
public interface MediaCallback {

    /**
     * 触发Application事件
     *
     * @param event ApplicationEvent实例
     */
    void onEvent(ApplicationEvent event);
}
