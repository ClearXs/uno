package cc.allio.uno.rule.api.vistor;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.rule.api.LogicPredicate;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * logic
 *
 * @author jiangwei
 * @date 2023/4/26 11:53
 * @since 1.1.4
 */
public class LogicGroup extends TraversalElement implements GroupElement<TraversalElement> {

    @Getter
    private final LogicPredicate logic;
    @Setter
    @Getter
    private GroupElement<TraversalElement> parent;
    private final List<TraversalElement> childrens;

    @Setter
    @Getter
    private int level;

    public LogicGroup(GroupElement<TraversalElement> parent, LogicPredicate logic) {
        this.parent = parent;
        this.logic = logic;
        this.childrens = Lists.newArrayList();
        if (parent != null) {
            this.level = parent.getLevel() + 1;
        }
    }

    @Override
    public List<TraversalElement> getChildrens() {
        return childrens;
    }

    @Override
    public boolean isLeaf() {
        return childrens.isEmpty();
    }

    @Override
    public boolean addElement(TraversalElement element) {
        return childrens.add(element);
    }

    @Override
    public boolean addElements(List<TraversalElement> elements) {
        return childrens.addAll(elements);
    }

    @Override
    public boolean removeElement(TraversalElement element) {
        return childrens.remove(element);
    }

    @Override
    public List<TraversalElement> getGroupElement() {
        return childrens.stream()
                .filter(e -> GroupElement.class.isAssignableFrom(e.getClass()))
                .map(TraversalElement.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public List<TraversalElement> getAttrElement() {
        return childrens.stream()
                .filter(e -> AttrElement.class.isAssignableFrom(e.getClass()))
                .map(AttrElement.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public void clearAttrElement() {
        childrens.removeIf(AttrElement.class::isInstance);
    }

    @Override
    public String getLiteral() {
        if (isRoot() && CollectionUtils.isNotEmpty(childrens)) {
            // root 节点第一个元素一定时OR
            LogicGroup element = (LogicGroup) childrens.get(0);
            return element.getLiteral();
        }
        StringBuilder literalExpr = new StringBuilder();
        for (int i = 0; i < childrens.size(); i++) {
            TraversalElement children = childrens.get(i);
            String literal = children.getLiteral();
            // 构建 a = xxx && xxx 或者 a = xx && (b = xx)
            literalExpr.append(literal).append(StringPool.SPACE);
            if (i != childrens.size() - 1) {
                literalExpr.append(logic.getSm()).append(StringPool.SPACE);
            }
        }
        // 如果只有一个children 则不进行，否则添加 '('  ')'
        if (childrens.size() == 1) {
            return literalExpr.toString();
        }
        literalExpr.insert(0, StringPool.LEFT_BRACKET);
        literalExpr.insert(literalExpr.length(), StringPool.RIGHT_BRACKET);
        return literalExpr.toString();
    }
}
