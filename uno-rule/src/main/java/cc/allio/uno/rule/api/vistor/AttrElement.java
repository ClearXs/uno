package cc.allio.uno.rule.api.vistor;

import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.rule.api.RuleAttr;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * rule expression。该节点一定是叶子节点
 *
 * @author j.x
 * @date 2023/4/26 11:53
 * @since 1.1.4
 */
public class AttrElement extends LogicGroup {

    private final Serializable id;

    @Getter
    private LogicGroup parent;
    @Getter
    private final RuleAttr ruleAttr;

    /**
     * 节点高度
     */
    @Setter
    @Getter
    private int depth;

    public AttrElement(LogicGroup parent, RuleAttr ruleAttr) {
        super(null, null);
        this.parent = parent;
        this.ruleAttr = ruleAttr;
        if (parent != null) {
            this.depth = parent.getDepth() + 1;
        }
        this.id = IdGenerator.defaultGenerator().toHex();
    }

    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public List<LogicGroup> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public boolean addElement(LogicGroup element) {
        throw Exceptions.eee("AttrElement is leaf element, cannot addElement", UnsupportedOperationException.class);
    }

    @Override
    public boolean addElements(List<LogicGroup> elements) {
        throw Exceptions.eee("AttrElement is leaf element, cannot addElements", UnsupportedOperationException.class);
    }

    @Override
    public boolean removeElement(LogicGroup element) {
        throw Exceptions.eee("AttrElement is leaf element, cannot LogicGroup", UnsupportedOperationException.class);
    }

    @Override
    public List<LogicGroup> getGroupElement() {
        throw Exceptions.eee("AttrElement is leaf element, cannot getGroupElement", UnsupportedOperationException.class);
    }

    @Override
    public List<LogicGroup> getAttrElement() {
        throw Exceptions.eee("AttrElement is leaf element, cannot getAttrElement", UnsupportedOperationException.class);
    }

    @Override
    public void clearAttrElement() {
        throw Exceptions.eee("AttrElement is leaf element, cannot clearAttrElement", UnsupportedOperationException.class);
    }

    @Override
    public void addChildren(LogicGroup element) {
        Exceptions.eee("AttrElement is leaf element, cannot addChildren", UnsupportedOperationException.class);
    }

    @Override
    public void setChildren(List<LogicGroup> children) {
        Exceptions.eee("AttrElement is leaf element, cannot setChildren", UnsupportedOperationException.class);
    }

    @Override
    public void clearChildren() {
        Exceptions.eee("AttrElement is leaf element, cannot clearChildren", UnsupportedOperationException.class);
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
