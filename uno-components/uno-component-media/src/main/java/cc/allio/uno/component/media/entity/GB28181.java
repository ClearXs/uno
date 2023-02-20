package cc.allio.uno.component.media.entity;

import lombok.Data;

/**
 * 国标28181 实体
 *
 * @author jiangwei
 * @date 2022/7/18 10:04
 * @since 1.0
 */
@Data
public class GB28181 {

    /**
     * 业务关联主键
     */
    private String bizKey;

    /**
     * 视频通道号 '14058100001320000013'@34020000001320000001
     */
    private String id;

    /**
     * 视频编码 14058100001320000013@'34020000001320000001'
     */
    private String chid;

}
