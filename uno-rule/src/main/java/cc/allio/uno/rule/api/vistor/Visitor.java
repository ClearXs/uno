package cc.allio.uno.rule.api.vistor;

/**
 * 树节点访问器
 *
 * @author jiangwei
 * @date 2023/4/26 11:58
 * @since 1.1.4
 */
@FunctionalInterface
public interface Visitor {

    /**
     * visit the given element
     *
     * @param e element
     */
    void visit(Element e);
}
