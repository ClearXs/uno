package cc.allio.uno.core.datastructure.tree;

/**
 * 遍历元素原则实现.{@link Traversal}
 *
 * @author jiangwei
 * @date 2023/4/26 15:18
 * @since 1.1.4
 */
public abstract class TraversalElement<T extends TraversalElement<T>> implements Element<T> {

    @Override
    public void accept(Visitor<T> visitor, Traversal traversal) {
        TraversalMode.get(traversal).doTraversal((T) this, visitor);
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
