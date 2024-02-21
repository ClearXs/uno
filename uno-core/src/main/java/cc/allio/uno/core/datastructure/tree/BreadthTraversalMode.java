package cc.allio.uno.core.datastructure.tree;

import cc.allio.uno.core.util.CollectionUtils;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 树广度优先
 *
 * @author jiangwei
 * @date 2023/4/27 09:17
 * @since 1.1.4
 */
public class BreadthTraversalMode implements TraversalMode {

    @Override
    public <T extends TraversalElement<T>> void doTraversal(T e, Visitor<T> visitor) {
        Queue<TraversalElement<T>> bfsQueue = new LinkedList<>();
        bfsQueue.add(e);
        while (!bfsQueue.isEmpty()) {
            TraversalElement<T> element = bfsQueue.poll();
            element.doAccept(visitor);
            if (CollectionUtils.isNotEmpty(element.getChildren())) {
                element.getChildren().forEach(bfsQueue::offer);
            }
        }
    }

    @Override
    public Traversal getMode() {
        return Traversal.BREADTH;
    }
}
