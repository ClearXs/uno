package cc.allio.uno.core.datastructure.tree;

import java.io.Serializable;

/**
 * 树平展结点
 *
 * @author j.x
 * @since 1.1.5
 */
public interface Expand extends Serializable {

    /**
     * 获取当前结点的标识
     */
    Serializable getId();

    /**
     * 获取父节点标识
     */
    Serializable getParentId();
}
