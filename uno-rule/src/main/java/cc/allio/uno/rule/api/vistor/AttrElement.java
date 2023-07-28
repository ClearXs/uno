package cc.allio.uno.rule.api.vistor;

import cc.allio.uno.rule.api.RuleAttr;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * rule expression。该节点一定是叶子节点
 *
 * @author jiangwei
 * @date 2023/4/26 11:53
 * @since 1.1.4
 */
public class AttrElement extends TraversalElement implements Element {

    @Getter
    private final GroupElement<?> parent;
    @Getter
    private final RuleAttr ruleAttr;

    /**
     * 节点高度
     */
    @Setter
    @Getter
    private int level;

    public AttrElement(GroupElement<?> parent, RuleAttr ruleAttr) {
        this.parent = parent;
        this.ruleAttr = ruleAttr;
        if (parent != null) {
            this.level = parent.getLevel() + 1;
        }
    }

    @Override
    public List<TraversalElement> getChildrens() {
        return Collections.emptyList();
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public String getLiteral() {
        return ruleAttr.getIndexExpr();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttrElement that = (AttrElement) o;
        return Objects.equals(ruleAttr, that.ruleAttr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleAttr);
    }
}
