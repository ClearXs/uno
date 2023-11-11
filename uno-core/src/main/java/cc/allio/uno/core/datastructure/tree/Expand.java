package cc.allio.uno.core.datastructure.tree;

import java.io.Serializable;

/**
 * 树平展结点
 *
 * @author j.x
 * @date 2023/11/9 11:32
 * @since 1.1.5
 */
public interface Expand {

    /**
     * 获取当前结点的标识
     */
    Serializable getId();

    /**
     * 获取父节点标识
     */
    Serializable getParentId();
}
