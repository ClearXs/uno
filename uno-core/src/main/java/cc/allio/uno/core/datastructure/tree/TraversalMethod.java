package cc.allio.uno.core.datastructure.tree;

import cc.allio.uno.core.exception.Exceptions;

/**
 * 定义树遍历模式
 *
 * @author j.x
 * @date 2023/4/27 09:12
 * @since 1.1.4
 */
public interface TraversalMethod {
    TraversalMethod NONE = new NoneTraversalMode();
    TraversalMethod DEEP = new DeepTraversalMode();
    TraversalMethod BREADTH = new BreadthTraversalMode();

    /**
     * 根据遍历的方式获取对应的遍历实例
     *
     * @param traversal traversal
     * @return TraversalMode实例
     */
    static TraversalMethod get(Traversal traversal) {
        if (NONE.getMode().equals(traversal.getValue())) {
            return NONE;
        } else if (DEEP.getMode().equals(traversal.getValue())) {
            return DEEP;
        } else if (BREADTH.getMode().equals(traversal.getValue())) {
            return BREADTH;
        }
        throw Exceptions.eee("unknown traversal", NullPointerException.class);
    }

    /**
     * 根据该模式来遍历元素
     *
     * @param e       e
     * @param visitor visitor
     */
    <T extends TraversalElement<T>> void doTraversal(T e, Visitor<T> visitor);

    /**
     * 获取遍历的模式
     *
     * @return Traversal
     */
    default String getMode() {
        return "custom";
    }
}
