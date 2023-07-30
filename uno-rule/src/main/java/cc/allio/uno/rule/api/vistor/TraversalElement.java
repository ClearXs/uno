package cc.allio.uno.rule.api.vistor;

import java.util.*;

/**
 * 遍历元素原则实现.{@link Traversal}
 *
 * @author jiangwei
 * @date 2023/4/26 15:18
 * @since 1.1.4
 */
public abstract class TraversalElement implements Element {

    @Override
    public void accept(Visitor visitor, Traversal traversal) {
        TraversalMode.get(traversal).doTraversal(this, visitor);
    }

    /**
     * 获取子节点
     *
     * @return element list
     */
    protected abstract List<TraversalElement> getChildrens();

    /**
     * 子类可以继承实现，默认调用访问访问当前元数据
     *
     * @param visitor visitor访问器
     */
    protected void doAccept(Visitor visitor) {
        visitor.visit(this);
    }
}