package cc.allio.uno.component.media.event;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义视频录制统一格式
 *
 * @author jiangwei
 * @date 2022/3/30 14:43
 * @since 1.0.6
 */
@Data
public class Dvr {

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 多媒体服务主机地址
     */
    private String host;

    /**
     * 客户端所属应用类型
     */
    private String app;

    /**
     * 流Id
     */
    private String stream;

    /**
     * 定义文件存放的根路径
     */
    private String cwd;

    /**
     * 文件存放路径
     */
    private String file;

    /**
     * 抽离于基本数据结构，为不同的多媒体系统提供额外的属性添加
     */
    private Map<String, Object> attributes = new HashMap<>();
}
