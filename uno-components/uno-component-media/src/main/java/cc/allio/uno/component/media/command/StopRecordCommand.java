package cc.allio.uno.component.media.command;

/**
 * 停止录制指令
 *
 * @author jiangwei
 * @date 2022/3/30 14:22
 * @since 1.0.6
 */
public interface StopRecordCommand extends Command {

    // -------------------- request --------------------

    /**
     * 流媒体服务器地址
     */
    String V_HOST = "VHOST";

    /**
     * 标识Media id
     */
    String MEDIA_ID_TAG = "MEDIA_ID";
}
