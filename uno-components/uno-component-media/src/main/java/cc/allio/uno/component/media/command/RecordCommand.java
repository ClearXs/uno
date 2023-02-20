package cc.allio.uno.component.media.command;

/**
 * 多媒体录制指令接口
 *
 * @author jiangwei
 * @date 2022/3/30 14:17
 * @since 1.0.6
 */
public interface RecordCommand extends Command {

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
