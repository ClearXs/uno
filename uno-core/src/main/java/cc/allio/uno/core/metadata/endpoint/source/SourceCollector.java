package cc.allio.uno.core.metadata.endpoint.source;

import cc.allio.uno.core.metadata.Metadata;

/**
 * 数据源收集器
 *
 * @author j.x
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
