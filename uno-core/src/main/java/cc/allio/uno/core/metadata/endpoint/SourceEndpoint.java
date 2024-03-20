package cc.allio.uno.core.metadata.endpoint;

import cc.allio.uno.core.metadata.Metadata;

/**
 * 数据源端点定义
 *
 * @author j.x
 * @date 2023/4/11 12:35
 * @since 1.1.4
 */
public interface SourceEndpoint<T extends Metadata> {

    /**
     * 数据源注册
     */
    void register();

    /**
     * 订阅某个数据源
     */
    void subscribe();
}
