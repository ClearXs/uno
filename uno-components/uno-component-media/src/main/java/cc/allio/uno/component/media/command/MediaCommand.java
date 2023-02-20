package cc.allio.uno.component.media.command;

/**
 * 获取单个多媒体（客户端/设备）指令标识接口</br>
 *
 * @author jiangwei
 * @date 2022/3/30 22:06
 * @see BaseMediaCommand
 * @since 1.0.6
 */
public interface MediaCommand extends Command {

    /**
     * 标识Media实体
     */
    String MEDIA_TAG = "MEDIA";
}
