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
    public void doTraversal(TraversalElement e, Visitor visitor) {
        Queue<TraversalElement> bfsQueue = new LinkedList<>();
        bfsQueue.add(e);
        while (!bfsQueue.isEmpty()) {
            TraversalElement element = bfsQueue.poll();
            element.doAccept(visitor);
            if (CollectionUtils.isNotEmpty(element.getChildren())) {
                element.getChildren().forEach(chd -> bfsQueue.offer((TraversalElement) chd));
            }
        }
    }

    @Override
    public Traversal getMode() {
        return Traversal.BREADTH;
    }
}
