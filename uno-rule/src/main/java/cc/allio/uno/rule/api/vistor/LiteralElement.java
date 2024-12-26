package cc.allio.uno.rule.api.vistor;

import cc.allio.uno.core.datastructure.tree.Element;

/**
 * 包含规则表达式的结点
 *
 * @author j.x
 * @since 1.1.5
 */
public interface LiteralElement<T extends LiteralElement<T>> extends Element<T> {

    /**
     * 平展开当前规则group 节点规则表达式。
     *
     * @return a == '5' && b == '5'...
     */
    String getLiteral();
}
