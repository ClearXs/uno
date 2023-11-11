package cc.allio.uno.core.datastructure.tree;

import cc.allio.uno.core.util.CollectionUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * 深度优先
 *
 * @author jiangwei
 * @date 2023/4/27 09:16
 * @since 1.1.4
 */
public class DeepTraversalMode implements TraversalMode {

    @Override
    public void doTraversal(TraversalElement e, Visitor visitor) {
        // 深度优先
        Deque<TraversalElement> dfsStack = new ArrayDeque<>();
        dfsStack.push(e);
        List<TraversalElement> childrens = e.getChildren();
        deepAccept(childrens, dfsStack);
        while (!dfsStack.isEmpty()) {
            TraversalElement element = dfsStack.pollLast();
            element.doAccept(visitor);
        }
    }

    private void deepAccept(List<? extends TraversalElement> childrens, Deque<TraversalElement> dfsStack) {
        if (CollectionUtils.isNotEmpty(childrens)) {
            childrens.forEach(dfsStack::offerLast);
            childrens.forEach(e -> deepAccept(e.getChildren(), dfsStack));
        }
    }

    @Override
    public Traversal getMode() {
        return Traversal.DEEP;
    }
}
