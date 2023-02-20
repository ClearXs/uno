package cc.allio.uno.component.media.command;

import cc.allio.uno.component.media.entity.Media;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 播放多媒体指令标识接口
 *
 * @author jiangwei
 * @date 2022/3/30 14:15
 * @since 1.0.6
 */
public interface PlayCommand extends Command {

    /**
     * 视频数据
     */
    String PLAY_DATA = "PLAY_TAG";

    /**
     * 设备播放协议
     */
    @Getter
    @AllArgsConstructor
    enum DevicePlayProtocol {

        /**
         * <a href="http://open.ys7.com/doc/zh/readme/ezopen.html">EZOPEN协议</a>
         */
        EZOPEN("EZOPEN", "", 1),

        /**
         * HLS协议
         */
        HLS("HLS", "", 2),

        /**
         * RTMP协议
         */
        RTMP("RTMP", "", 3),

        /**
         * FLV协议
         */
        FLV("FLV", "", 4);

        private final String code;
        private final String desc;

        /**
         * 标识萤石云转换协议
         */
        private final Object ezTag;

        /**
         * 获取当前协议
         *
         * @param media media对象实例
         * @return 当前协议的地址
         */
        public String toUrl(Media media) {
            if (this == EZOPEN) {
                return media.getEzUrl();
            }
            if (this == HLS) {
                return media.getHlsUrl();
            }
            if (this == RTMP) {
                return media.getRtmpUrl();
            }
            if (this == FLV) {
                return media.getFlvUrl();
            }
            return "";
        }
    }
}
