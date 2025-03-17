package cc.allio.uno.core.util.tree;

import cc.allio.uno.core.StringPool;

import java.io.Serializable;
import java.util.List;

/**
 * 抽象树结点定义
 *
 * @author j.x
 * @since 1.1.4
 */
public interface Element<T extends Element<T>> extends Serializable {

    Element ROOT_SENTINEL = new DefaultElement<>(-1, -1);

    /**
     * 定义根节点
     */
    int ROOT_NODE = 0;

    /**
     * set id
     */
    void setId(Serializable id);

    /**
     * 获取树的标识
     */
    Serializable getId();

    /**
     * 获取节点所在的深度
     *
     * @return level
     */
    int getDepth();

    /**
     * 设置树的层级
     */
    void setDepth(int depth);

    /**
     * 获取当前树的路径
     *
     * @return a.b.c以'.'来切分路径标识
     */
    default String getPath() {
        return getParent() != null ? getParent().getPath() + StringPool.ORIGIN_DOT + getId() : String.valueOf(getId());
    }

    /**
     * 获取父节点Id
     *
     * @return Element
     * @since 1.1.7
     */
    default Serializable getParentId() {
        return getParent() != null ? getParent().getId() : null;
    }

    /**
     * 设置父节点Id
     */
    void setParentId(Serializable parentId);

    /**
     * 获取父节点
     *
     * @return Element
     */
    T getParent();

    /**
     * 设置父节点
     *
     * @param parent parent node
     */
    void setParent(T parent);

    /**
     * 是否为根节点
     *
     * @return true 是 false 否
     */
    default boolean isRoot() {
        return getDepth() == ROOT_NODE;
    }

    /**
     * 是否为叶子节点
     *
     * @return true 是 false 否
     */
    boolean isLeaf();

    /**
     * 根据指定的id获取某个子结点
     *
     * @param id id
     * @return element or null
     */
    T findChildren(Serializable id);

    /**
     * 根据指定的id移除某个子结点
     *
     * @param id id
     * @return true if success
     */
    boolean removeChildren(Serializable id);

    /**
     * 获取子节点
     *
     * @return element list
     */
    List<T> getChildren();

    /**
     * 添加子结点
     */
    void addChildren(T element);

    /**
     * 覆盖并设置子结点
     *
     * @param children children
     */
    void setChildren(List<T> children);

    /**
     * 清除children数据
     */
    void clearChildren();

    /**
     * 默认实现为深度优先原则
     *
     * @param visitor visitor
     */
    default void accept(Visitor<T> visitor) {
        accept(visitor, Traversal.DEEP);
    }

    /**
     * 树的访问
     *
     * @param visitor   visitor
     * @param traversal 遍历原则
     */
    default void accept(Visitor<T> visitor, Traversal traversal) {
        accept(visitor, TraversalMethod.get(traversal));
    }

    /**
     * 树的访问
     *
     * @param visitor   visitor
     * @param method    traversal method
     */
    void accept(Visitor<T> visitor, TraversalMethod method);

    /**
     * 获取Sentinel结点
     */
    default Element obtainSentinel() {
        return new DefaultElement<>(-1, -1);
    }
}
