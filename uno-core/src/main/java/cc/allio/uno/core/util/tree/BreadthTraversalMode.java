package cc.allio.uno.core.util.tree;

import cc.allio.uno.core.util.CollectionUtils;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 树广度优先
 *
 * @author j.x
 * @since 1.1.4
 */
public class BreadthTraversalMode implements TraversalMethod {

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
    public String getMode() {
        return Traversal.BREADTH.getValue();
    }
}
