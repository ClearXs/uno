package cc.allio.uno.rule.api.vistor;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.datastructure.tree.Element;
import cc.allio.uno.core.datastructure.tree.TraversalElement;
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
 * @author j.x
 * @since 1.1.4
 */
public class LogicGroup extends TraversalElement<LogicGroup> implements GroupElement<LogicGroup> {

    @Getter
    @Setter
    private Serializable parentId;

    @Setter
    @Getter
    private Serializable id;

    @Getter
    private final LogicPredicate logic;

    @Getter
    private LogicGroup parent;
    private final List<LogicGroup> children;

    @Setter
    @Getter
    private int depth;

    public LogicGroup(LogicGroup parent, LogicPredicate logic) {
        this.parent = parent;
        this.logic = logic;
        this.children = Lists.newArrayList();
        if (parent != null) {
            this.depth = parent.getDepth() + 1;
        }
        this.id = IdGenerator.defaultGenerator().toHex();
    }

    @Override
    public void setParent(LogicGroup parent) {
        this.parent = parent;
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public LogicGroup findChildren(Serializable id) {
        for (LogicGroup child : children) {
            if (id.equals(child.getId())) {
                return child;
            }
            LogicGroup findChild = child.findChildren(id);
            if (findChild != null) {
                return findChild;
            }
        }
        return null;
    }

    @Override
    public boolean removeChildren(Serializable id) {
        boolean removed = children.removeIf(child -> id.equals(child.getId()));
        if (removed) {
            return true;
        }
        // 没有移除尝试在从字节中再次移除
        for (LogicGroup child : children) {
            removed = child.removeChildren(id);
            if (removed) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<LogicGroup> getChildren() {
        return null;
    }

    @Override
    public void addChildren(LogicGroup element) {
        addElement(element);
    }


    @Override
    public void setChildren(List<LogicGroup> children) {
        clearChildren();
        this.children.addAll(children);
    }

    @Override
    public void clearChildren() {
        children.clear();
    }

    @Override
    public boolean addElement(LogicGroup element) {
        return children.add(element);
    }

    @Override
    public boolean addElements(List<LogicGroup> elements) {
        return children.addAll(elements);
    }

    @Override
    public boolean removeElement(LogicGroup element) {
        return children.remove(element);
    }

    @Override
    public List<LogicGroup> getGroupElement() {
        return children.stream()
                .filter(e -> !e.isLeaf())
                .toList();
    }

    @Override
    public List<LogicGroup> getAttrElement() {
        return children.stream()
                .filter(LogicGroup::isLeaf)
                .toList();
    }

    @Override
    public void clearAttrElement() {
        children.removeIf(LogicGroup::isLeaf);
    }

    @Override
    public String getLiteral() {
        if (isRoot() && CollectionUtils.isNotEmpty(children)) {
            // root 节点第一个元素一定时OR
            LogicGroup element = children.get(0);
            return element.getLiteral();
        }
        StringBuilder literalExpr = new StringBuilder();
        for (int i = 0; i < children.size(); i++) {
            LogicGroup children = this.children.get(i);
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
