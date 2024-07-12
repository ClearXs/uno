package cc.allio.uno.rule.api.vistor;

import cc.allio.uno.core.datastructure.tree.Element;
import cc.allio.uno.core.datastructure.tree.Traversal;
import cc.allio.uno.rule.api.LogicPredicate;
import cc.allio.uno.rule.api.RuleAttr;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 默认实现
 *
 * @author j.x
 * @date 2023/4/26 12:13
 * @since 1.1.4
 */
public class DefaultCompilationRule implements CompilationRule {

    @Override
    public GroupElement<?> treeifyBin(List<RuleAttr> ruleItems) {
        // 根节点为 AND group
        LogicGroup root = new LogicGroup(null, LogicPredicate.AND);
        root.setDepth(Element.ROOT_NODE);
        // 获取第一个节点创建遍历表达式
        LogicGroup p = null;
        for (RuleAttr item : ruleItems) {
            if (p == null) {
                p = new LogicGroup(root, item.getLogic());
                root.addElement(p);
            }
            if (p.getLogic() == LogicPredicate.AND) {
                if (item.getLogic() == LogicPredicate.OR) {
                    LogicGroup newP = new LogicGroup(p.getParent(), LogicPredicate.OR);
                    if (p.getParent().equals(root)) {
                        root.removeElement(p);
                        root.addElement(newP);
                    }
                    p.setParent(newP);
                    newP.addElement(p);
                    p = newP;
                }
                p.addElement(new AttrElement(p, item));
            } else if (p.getLogic() == LogicPredicate.OR) {
                if (item.getLogic() == LogicPredicate.AND) {
                    // 放入 and group 中
                    List<LogicGroup> attrElement = p.getAttrElement();
                    p.clearAttrElement();
                    // 规则项重组为and节点
                    LogicGroup n = new LogicGroup(p, LogicPredicate.AND);
                    n.addElements(attrElement);
                    n.addElement(new AttrElement(n, item));
                    p.addElement(n);
                } else if (item.getLogic() == LogicPredicate.OR) {
                    LogicGroup newP = new LogicGroup(p, LogicPredicate.OR);
                    p.addElement(newP);
                    p = newP;
                    p.addElement(new AttrElement(p, item));
                }
            }
        }
        // 构建树节点的层级
        buildTreeLevel(root);
        return root;
    }

    private void buildTreeLevel(GroupElement<?> root) {
        root.accept(
                e -> {
                    int level = Element.ROOT_NODE;
                    Element parent = e.getParent();
                    if (parent != null) {
                        level = parent.getDepth() + 1;
                    }
                    if (e instanceof AttrElement) {
                        ((AttrElement) e).setDepth(level);
                    } else if (e instanceof LogicGroup) {
                        ((LogicGroup) e).setDepth(level);
                    }
                });
    }

    @Override
    public List<RuleAttr> expand(GroupElement<?> tree) {
        List<RuleAttr> ruleAttrs = Lists.newArrayList();
        tree.accept(
                e -> {
                    if (e instanceof AttrElement) {
                        RuleAttr ruleAttr = ((AttrElement) e).getRuleAttr();
                        ruleAttrs.add(ruleAttr);
                    }
                },
                Traversal.DEEP);
        return ruleAttrs;
    }
}
