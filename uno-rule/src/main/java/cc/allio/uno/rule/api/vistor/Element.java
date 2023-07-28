package cc.allio.uno.rule.api.vistor;

import java.io.Serializable;

/**
 * <b>定义规则树的节点。</b>
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

    /**
     * 定义根节点
     */
    int ROOT_NODE = 0;

    /**
     * 获取节点所在的高度
     *
     * @return level
     */
    int getLevel();

    /**
     * 获取父节点
     *
     * @return Element
     */
    Element getParent();

    /**
     * 是否为根节点
     *
     * @return true 是 false 否
     */
    default boolean isRoot() {
        return getLevel() == ROOT_NODE;
    }

    /**
     * 是否为叶子节点
     *
     * @return true 是 false 否
     */
    boolean isLeaf();

    /**
     * 平展开当前规则group 节点规则表达式。
     *
     * @return a == '5' && b == '5'...
     */
    String getLiteral();

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
}
