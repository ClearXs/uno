package cc.allio.uno.rule.api.vistor;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.datastructure.tree.Element;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.rule.api.LogicPredicate;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * logic
 *
 * @author jiangwei
 * @date 2023/4/26 11:53
 * @since 1.1.4
 */
public class LogicGroup extends LiteralTraversalElement implements GroupElement<LiteralTraversalElement> {

    @Getter
    private final Serializable id;

    @Getter
    private final LogicPredicate logic;

    @Getter
    private GroupElement<LiteralTraversalElement> parent;
    private List<Element> children;

    @Setter
    @Getter
    private int depth;

    public LogicGroup(GroupElement<LiteralTraversalElement> parent, LogicPredicate logic) {
        this.parent = parent;
        this.logic = logic;
        this.children = Lists.newArrayList();
        if (parent != null) {
            this.depth = parent.getDepth() + 1;
        }
        this.id = IdGenerator.defaultGenerator().toHex();
    }

    @Override
    public <T extends Element> void setParent(T parent) {
        this.parent = (GroupElement<LiteralTraversalElement>) parent;
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public <T extends Element> List<T> getChildren() {
        return (List<T>) children;
    }

    @Override
    public <T extends Element> void addChildren(T element) {
        addElement((LiteralTraversalElement) element);
    }

    @Override
    public <T extends Element> void setChildren(List<T> children) {
        clearChildren();
        this.children.addAll(children);
    }

    @Override
    public void clearChildren() {
        children.clear();
    }

    @Override
    public boolean addElement(LiteralTraversalElement element) {
        return children.add(element);
    }

    @Override
    public boolean addElements(List<LiteralTraversalElement> elements) {
        return children.addAll(elements);
    }

    @Override
    public boolean removeElement(LiteralTraversalElement element) {
        return children.remove(element);
    }

    @Override
    public List<LiteralTraversalElement> getGroupElement() {
        return children.stream()
                .filter(e -> GroupElement.class.isAssignableFrom(e.getClass()))
                .map(LiteralTraversalElement.class::cast)
                .toList();
    }

    @Override
    public List<LiteralTraversalElement> getAttrElement() {
        return children.stream()
                .filter(e -> AttrElement.class.isAssignableFrom(e.getClass()))
                .map(LiteralTraversalElement.class::cast)
                .toList();
    }

    @Override
    public void clearAttrElement() {
        children.removeIf(AttrElement.class::isInstance);
    }

    @Override
    public String getLiteral() {
        if (isRoot() && CollectionUtils.isNotEmpty(children)) {
            // root 节点第一个元素一定时OR
            LogicGroup element = (LogicGroup) children.get(0);
            return element.getLiteral();
        }
        StringBuilder literalExpr = new StringBuilder();
        for (int i = 0; i < children.size(); i++) {
            LiteralTraversalElement children = (LiteralTraversalElement) this.children.get(i);
            String literal = children.getLiteral();
            // 构建 a = xxx && xxx 或者 a = xx && (b = xx)
            literalExpr.append(literal).append(StringPool.SPACE);
            if (i != this.children.size() - 1) {
                literalExpr.append(logic.getSm()).append(StringPool.SPACE);
            }
        }
        // 如果只有一个children 则不进行，否则添加 '('  ')'
        if (children.size() == 1) {
            return literalExpr.toString();
        }
        literalExpr.insert(0, StringPool.LEFT_BRACKET);
        literalExpr.insert(literalExpr.length(), StringPool.RIGHT_BRACKET);
        return literalExpr.toString();
    }
}
