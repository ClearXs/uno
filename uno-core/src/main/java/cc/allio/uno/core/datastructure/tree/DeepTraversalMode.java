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
    public <T extends TraversalElement<T>> void doTraversal(T e, Visitor<T> visitor) {
        // 深度优先
        Deque<T> dfsStack = new ArrayDeque<>();
        dfsStack.push(e);
        List<T> childrens = e.getChildren();
        deepAccept(childrens, dfsStack);
        while (!dfsStack.isEmpty()) {
            T element = dfsStack.pollLast();
            element.doAccept(visitor);
        }
    }

    private <T extends TraversalElement<T>> void deepAccept(List<T> childrens, Deque<T> dfsStack) {
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
