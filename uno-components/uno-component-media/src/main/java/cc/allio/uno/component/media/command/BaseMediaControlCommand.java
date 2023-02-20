package cc.allio.uno.component.media.command;

import cc.allio.uno.component.media.entity.MediaControl;
import lombok.Getter;

/**
 * 定义多媒体控制构造参数
 *
 * @author jiangwei
 * @date 2022/11/25 15:31
 * @since 1.1.2
 */
@Getter
public abstract class BaseMediaControlCommand implements MediaControlCommand {

    private final MediaControl mediaControl;

    protected BaseMediaControlCommand(MediaControl mediaControl) {
        this.mediaControl = mediaControl;
    }
}
