package cc.allio.uno.core.util.tree;

/**
 * 遍历元素原则实现.{@link Traversal}
 *
 * @author j.x
 * @since 1.1.4
 */
public abstract class TraversalElement<T extends TraversalElement<T>> implements Element<T> {

    @Override
    public void accept(Visitor<T> visitor, TraversalMethod method) {
        method.doTraversal((T) this, visitor);
    }

    /**
     * 子类可以继承实现，默认调用访问访问当前元数据
     *
     * @param visitor visitor访问器
     */
    protected void doAccept(Visitor<T> visitor) {
        visitor.visit((T) this);
    }
}
