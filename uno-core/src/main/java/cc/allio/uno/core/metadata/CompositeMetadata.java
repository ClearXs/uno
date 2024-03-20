package cc.allio.uno.core.metadata;

import java.util.List;

/**
 * 复合型元数据接口，实现类可以包含于多个{@link Metadata}实例
 *
 * @author j.x
 * @date 2022/11/21 22:38
 * @since 1.1.1
 */
public interface CompositeMetadata<T extends Metadata> extends Metadata {

    /**
     * 获取复合元数据列表
     *
     * @return 列表实例数据
     */
    List<T> getCompositeMetadata();
}
