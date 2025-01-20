package cc.allio.uno.core.util.tree;

/**
 * 优先访问枝干
 *
 * @author j.x
 * @since 1.1.4
 */
public class NoneTraversalMode implements TraversalMethod {

    @Override
    public <T extends TraversalElement<T>> void doTraversal(T e, Visitor<T> visitor) {
        e.doAccept(visitor);
        e.getChildren().forEach(c -> c.accept(visitor, Traversal.NONE));
    }

    @Override
    public String getMode() {
        return Traversal.NONE.getValue();
    }
}
