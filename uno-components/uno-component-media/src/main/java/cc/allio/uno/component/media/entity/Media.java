package cc.allio.uno.component.media.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 尽量制造通用的多媒体对象
 *
 * @author jiangwei
 * @date 2022/3/30 16:58
 * @since 1.0.6
 */
@Data
@Accessors(chain = true)
public class Media {

    /**
     * RTMP协议前缀
     */
    private static final String RTMP_PREFIX = "rtmp://";

    /**
     * FLV（HTTP）协议前缀
     */
    private static final String FLV_PREFIX = "http://";

    /**
     * EZ协议前缀
     */
    private static final String EZ_PREFIX = "ezopen://";

    /**
     * 客户端序列化号或者设备序列号
     */
    private String serial;

    /**
     * 客户端或者设备id
     */
    private String id;

    /**
     * 流服务器主机地址
     */
    private String host;

    /**
     * 流名称
     */
    private String name;

    /**
     * 流id
     */
    private String stream;

    /**
     * flv流地址，形如：http://192.168.124.10:8080/live/livestream.flv
     */
    private String flvUrl;

    /**
     * rtmp流地址：形如：rtmp://58.200.131.2:1935/livetv/hunantv
     */
    private String rtmpUrl;

    /**
     * hls协议地址
     */
    private String hlsUrl;

    /**
     * ez协议地址
     */
    private String ezUrl;

    /**
     * 客户端类型或者设备型号
     */
    private String type;

    /**
     * 是否是推流端
     */
    private boolean publish;

    /**
     * 业务关联键
     */
    private String bizKey;

    /**
     * 设备位置信息
     */
    private Location location = new Location();

    /**
     * 设备状态(0代表在线，1代表离线)
     */
    private Integer iden;

    /**
     * 位置信息
     *
     * @author jiangwei
     * @date 2022/6/16 20:17
     * @since 1.0
     */
    @Data
    public static class Location {

        /**
         * 经度
         */
        private BigDecimal longitude;

        /**
         * 纬度
         */
        private BigDecimal latitude;

        /**
         * 地址详细信息
         */
        private String address;
    }

}
