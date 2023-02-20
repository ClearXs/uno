package cc.allio.uno.core.metadata.endpoint.source;

import cc.allio.uno.core.metadata.Metadata;

/**
 * 数据源收集器
 *
 * @author jiangwei
 * @date 2022/9/27 15:18
 * @since 1.1.0
 */
public interface SourceCollector<T extends Metadata> {

    /**
     * 搜集时序数据
     *
     * @param element 时序数据实例
     */
    void collect(T element);
}
