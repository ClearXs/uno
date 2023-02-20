package cc.allio.uno.component.media.entity;

import cc.allio.uno.component.media.command.MediaControlCommand;
import lombok.Data;

/**
 * 多媒体控制实体
 *
 * @author jiangwei
 * @date 2022/7/16 10:43
 * @since 1.0.6
 */
@Data
public class MediaControl {

    /**
     * 视频控制信息
     */
    private GB28181 gb28181;

    /**
     * 控制指令
     */
    private MediaControlCommand.ControlCommandSet command;

    /**
     * 控制速度
     */
    private Integer speed;
}
