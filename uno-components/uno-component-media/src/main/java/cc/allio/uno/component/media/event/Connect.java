package cc.allio.uno.component.media.event;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义客户端连接统一格式
 *
 * @author jiangwei
 * @date 2022/3/30 14:29
 * @since 1.0.6
 */
@Data
public class Connect {

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
     * 客户端拉流地址
     */
    private String pullUrl;

    /**
     * 抽离于基本数据结构，为不同的多媒体系统提供额外的属性添加
     */
    private Map<String, Object> attributes = new HashMap<>();
}
