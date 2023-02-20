package cc.allio.uno.stater.srs;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Srs流服务器，客户端类型
 *
 * @author jiangwei
 * @date 2022/3/30 16:43
 * @since 1.0.6
 */
@Getter
@AllArgsConstructor
public enum SrsClientType {

    /**
     * flash推流端
     */
    FLASH_PUBLISH("flash-publish", "flash推流端"),

    /**
     * fmle推流端
     */
    FMLE_PUBLISH("fmle-publish", "fmle推流端"),

    /**
     * haivision推流端
     */
    HAIVISION_PUBLISH("haivision-publish", "haivision推流端"),

    /**
     * rtc推流端
     */
    RTC_PUBLISH("rtc-publish", "rtc推流端"),

    /**
     * rtc拉流端
     */
    RTC_PLAY("rtc-play", "rtc拉流端"),

    /**
     * rtmp拉流端
     */
    RTMP_PLAY("rtmp-play", "rtmp拉流端");

    /**
     * 类型
     */
    private String value;

    /**
     * 说明
     */
    private String des;

}
