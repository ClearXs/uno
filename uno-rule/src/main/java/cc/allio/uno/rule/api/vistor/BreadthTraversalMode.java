package cc.allio.uno.rule.api.vistor;

import cc.allio.uno.core.util.CollectionUtils;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 广度优先
 *
 * @author jiangwei
 * @date 2023/4/27 09:17
 * @since 1.1.4
 */
public class BreadthTraversalMode implements TraversalMode {

    BreadthTraversalMode() {
    }

    @Override
    public void doTraversal(TraversalElement e, Visitor visitor) {
        Queue<TraversalElement> bfsQueue = new LinkedList<>();
        bfsQueue.add(e);
        while (!bfsQueue.isEmpty()) {
            TraversalElement element = bfsQueue.poll();
            element.doAccept(visitor);
            if (CollectionUtils.isNotEmpty(element.getChildrens())) {
                element.getChildrens().forEach(bfsQueue::offer);
            }
        }
    }

    @Override
    public Traversal getMode() {
        return Traversal.BREADTH;
    }
}
