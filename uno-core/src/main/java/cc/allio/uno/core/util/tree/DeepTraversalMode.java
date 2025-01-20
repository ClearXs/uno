package cc.allio.uno.core.util.tree;

import cc.allio.uno.core.util.CollectionUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * 深度优先
 *
 * @author j.x
 * @since 1.1.4
 */
public class DeepTraversalMode implements TraversalMethod {

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
    public String getMode() {
        return Traversal.DEEP.getValue();
    }
}
