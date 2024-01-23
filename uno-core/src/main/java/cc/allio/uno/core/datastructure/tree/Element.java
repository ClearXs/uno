package cc.allio.uno.core.datastructure.tree;

import cc.allio.uno.core.StringPool;

import java.io.Serializable;
import java.util.List;

/**
 * 节点类型分为两种：
 * <ul>
 *     <li>group 节点</li>
 *     <li>attr 节点</li>
 * </ul>
 *
 * @author jiangwei
 * @date 2023/4/26 11:31
 * @since 1.1.4
 */
public interface Element extends Serializable {

    Element ROOT_SENTINEL = new DefaultElement(-1, -1);

    /**
     * 定义根节点
     */
    int ROOT_NODE = 0;

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
     * @since 1.1.6
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
    <T extends Element> T getParent();

    /**
     * 设置父节点
     *
     * @param parent parent node
     */
    <T extends Element> void setParent(T parent);

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
     * 获取子节点
     *
     * @return element list
     */
    <T extends Element> List<T> getChildren();

    /**
     * 添加子结点
     */
    <T extends Element> void addChildren(T element);

    /**
     * 覆盖并设置子结点
     *
     * @param children children
     */
    <T extends Element> void setChildren(List<T> children);

    /**
     * 清除children数据
     */
    void clearChildren();

    /**
     * 访问器模式访问每一个节点。默认实现为深度优先原则
     *
     * @param visitor visitor
     */
    default void accept(Visitor visitor) {
        accept(visitor, Traversal.NONE);
    }

    /**
     * 访问器模式访问每一个节点。默认实现为深度优先原则
     *
     * @param visitor   visitor
     * @param traversal 遍历原则
     */
    void accept(Visitor visitor, Traversal traversal);

    /**
     * 获取Sentinel结点
     */
    static Element getRootSentinel() {
        return new DefaultElement(-1, -1);
    }
}
