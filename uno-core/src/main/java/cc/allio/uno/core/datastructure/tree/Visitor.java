package cc.allio.uno.core.datastructure.tree;

/**
 * 树节点访问器
 *
 * @author j.x
 * @since 1.1.4
 */
@FunctionalInterface
public interface Visitor<T extends Element<T>> {

    /**
     * visit the given element
     *
     * @param e element
     */
    void visit(T e);
}
