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
     * 搜集元数据
     *
     * @param element 元数据
     */
    void collect(T element);
}
