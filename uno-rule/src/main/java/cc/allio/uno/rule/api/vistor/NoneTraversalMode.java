package cc.allio.uno.rule.api.vistor;

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
    public void doTraversal(TraversalElement e, Visitor visitor) {
        e.doAccept(visitor);
        e.getChildrens().forEach(c -> c.accept(visitor, getMode()));
    }

    @Override
    public Traversal getMode() {
        return Traversal.NONE;
    }
}
