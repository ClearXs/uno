package cc.allio.uno.core.datastructure.tree;

/**
 * 优先访问枝干
 *
 * @author jiangwei
 * @date 2023/4/27 09:13
 * @since 1.1.4
 */
public class NoneTraversalMode implements TraversalMode {

    NoneTraversalMode() {}

    @Override
    public <T extends TraversalElement<T>> void doTraversal(T e, Visitor<T> visitor) {
        e.doAccept(visitor);
        e.getChildren().forEach(c -> c.accept(visitor, getMode()));
    }

    @Override
    public Traversal getMode() {
        return Traversal.NONE;
    }
}
