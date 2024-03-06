package cc.allio.uno.rule.drools;

import cc.allio.uno.core.datastructure.tree.Element;
import cc.allio.uno.rule.api.vistor.GroupElement;
import cc.allio.uno.core.datastructure.tree.Traversal;
import cc.allio.uno.core.datastructure.tree.Visitor;
import cc.allio.uno.rule.api.*;
import cc.allio.uno.rule.api.event.RuleContext;
import cc.allio.uno.rule.api.vistor.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.drools.base.definitions.InternalKnowledgePackage;
import org.drools.base.definitions.rule.impl.RuleImpl;
import org.drools.compiler.builder.impl.KnowledgeBuilderImpl;
import org.drools.compiler.compiler.Dialect;
import org.drools.drl.ast.descr.*;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.List;
import java.util.Map;

/**
 * 用于管理drools的类
 *
 * @author jiangwei
 * @date 2023/4/23 19:49
 * @see Fact#ruleValues() 构建运行时数据
 * @see /src/test/resources/cc/allio/uno/rule/drools/ruleindex.drl 构建的drl数据
 * @since 1.1.4
 */
@NotThreadSafe
public class DroolsRuleManager {

    private static final String DEFAULT_PACK_NAME = DroolsRuleManager.class.getPackage().getName();
    private static final KnowledgeBuilderImpl kBuilder;
    private static final PackageDescr pkgDescr;
    private static final InternalKnowledgePackage defaultPkg;
    public static final String GLOBAL_ACTION_NAME = "action";
    public static final String GLOBAL_CONTEXT_NAME = "context";

    /**
     * drools 结果集。触发给定的action实例
     */
    public static final String DROOLS_CONSEQUENCE =
            "        context.putAttribute(\"drools\", drools);\n" +
                    "        action.onTrigger(context);";

    static {
        kBuilder = new KnowledgeBuilderImpl();
        // 创建默认包
        pkgDescr = new PackageDescr(DEFAULT_PACK_NAME);
        ImportDescr importMapDescr = new ImportDescr(Map.class.getName());
        ImportDescr importRuleValueDescr = new ImportDescr(Fact.class.getName());
        ImportDescr importActionValueDescr = new ImportDescr(Action.class.getName());
        ImportDescr importDroolsValueDescr = new ImportDescr(RuleContext.class.getName());
        pkgDescr.addAllImports(Lists.newArrayList(importMapDescr, importRuleValueDescr, importActionValueDescr, importDroolsValueDescr));
        GlobalDescr globalActionDescr = new GlobalDescr(GLOBAL_ACTION_NAME, Action.class.getName());
        GlobalDescr globalContextDescr = new GlobalDescr(GLOBAL_CONTEXT_NAME, RuleContext.class.getName());
        pkgDescr.addGlobal(globalActionDescr);
        pkgDescr.addGlobal(globalContextDescr);
        kBuilder.addPackage(pkgDescr);
        defaultPkg = kBuilder.getPackage(DEFAULT_PACK_NAME);
        // 覆盖PatternDescr的Builder实现
        Dialect dialect = kBuilder.getPackageRegistry().get(DEFAULT_PACK_NAME).getDialectCompiletimeRegistry().getDialect("java");
        dialect.getBuilders().put(DroolsPatternDescr.class, new DroolsPatternBuilder());
    }

    private final Map<String, Rule> ruleRegistry = Maps.newConcurrentMap();
    private static final CompilationRule COMPILATION_RULE = CompilationRule.DEFAULT;

    /**
     * 获取KnowledgeBuilderImpl实现
     *
     * @return KnowledgeBuilderImpl instance
     */
    public KnowledgeBuilderImpl getKBuilder() {
        return kBuilder;
    }

    /**
     * 获取Pkg的实现
     *
     * @return InternalKnowledgePackage instance
     */
    public InternalKnowledgePackage getPkg() {
        return defaultPkg;
    }

    /**
     * 根据rule实例构建drools rule desr并放入pkg中
     *
     * @param rule rule
     */
    public void buildRule(Rule rule) {
        RuleDescr ruleDescr = new DroolsRuleDescr(rule.getName(), rule);
        ruleDescr.setConsequence(DROOLS_CONSEQUENCE);
        // build root lhs
        AndDescr lhs = new AndDescr();
        ruleDescr.setLhs(lhs);
        PatternDescr pattern = new PatternDescr(Fact.class.getName());
        pattern.setIdentifier("$values");
        pattern.setObjectType("Fact");
        lhs.addDescr(pattern);
        // build children lhs
        List<RuleAttr> ruleAttrs = rule.getRuleAttr();
        GroupElement<?> groupElement = COMPILATION_RULE.treeifyBin(ruleAttrs);
        DroolsBFSRuleVisitorElement visitor = new DroolsBFSRuleVisitorElement(lhs);
        groupElement.accept(visitor, Traversal.BREADTH);
        pkgDescr.addRule(ruleDescr);
        kBuilder.addPackage(pkgDescr);
        ruleRegistry.put(rule.getName(), rule);
    }

    /**
     * 判断是否包含rule
     *
     * @param ruleName ruleName
     * @return true 包含 false 不包含
     */
    public boolean containsRule(String ruleName) {
        RuleImpl rule = defaultPkg.getRule(ruleName);
        return rule != null;
    }

    /**
     * 比较rule，包含指标的比较。
     * <p>当判断rule存在后，比较旧的与新的指标值是否一致，如果不一致则先移除旧的在返回结果</p>
     *
     * @param rule the rule
     * @return true 包含 false 不包含
     */
    public boolean compareRuleAndRemoveOldRule(Rule rule) {
        RuleImpl droolsRule = defaultPkg.getRule(rule.getName());
        // 存在，判断指标是否一致
        if (droolsRule != null && ruleRegistry.containsKey(rule.getName())) {
            boolean compareResult = true;
            Rule keep = ruleRegistry.get(rule.getName());
            List<RuleAttr> keepAttr = keep.getRuleAttr();
            List<RuleAttr> ruleAttr = rule.getRuleAttr();
            if (keepAttr.size() != ruleAttr.size()) {
                compareResult = false;
            }
            if (!compareResult) {
                // 移除
                removeRule(rule);
                return false;
            }
            // 两者指标数量一致，判断指标key是否一致
            for (int i = 0; i < keepAttr.size(); i++) {
                RuleAttr keepItem = keepAttr.get(i);
                RuleAttr item = ruleAttr.get(i);
                if (!keepItem.getKey().equals(item.getKey())) {
                    compareResult = false;
                    break;
                }
            }
            if (!compareResult) {
                removeRule(rule);
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 获取{@link Rule}实例
     *
     * @param ruleName ruleName
     * @return rule instance
     */
    public Rule getRule(String ruleName) {
        return ruleRegistry.get(ruleName);
    }

    /**
     * 移除rule及其定义
     *
     * @param rule
     */
    public void removeRule(Rule rule) {
        ruleRegistry.remove(rule.getName());
        RuleImpl ruleImpl = defaultPkg.getRule(rule.getName());
        if (ruleImpl != null) {
            defaultPkg.removeRule(ruleImpl);
        }
    }

    public static class DroolsBFSRuleVisitorElement implements Visitor {

        private final AndDescr root;
        private final Map<Element, ConditionalElementDescr> parentrMap = Maps.newHashMap();

        public DroolsBFSRuleVisitorElement(AndDescr root) {
            this.root = root;
        }

        @Override
        public void visit(Element e) {
            if (e.isRoot()) {
                // 放入根节点
                parentrMap.put(e, root);
                return;
            }
            // 构建drools conditions需要明确当前parent，做映射.
            // 已知group一定是parent节点
            if (e instanceof LogicGroup) {
                ConditionalElementDescr p = parentrMap.get(e.getParent());
                LogicPredicate logic = ((LogicGroup) e).getLogic();
                BaseDescr logicDescr = createLogicDescr(logic);
                p.addDescr(logicDescr);
                parentrMap.put(e, (ConditionalElementDescr) logicDescr);
            } else if (e instanceof AttrElement) {
                ConditionalElementDescr p = parentrMap.get(e.getParent());
                DroolsPatternDescr node = new DroolsPatternDescr(Map.class.getName());
                FromDescr fromDescr = new FromDescr();
                fromDescr.setDataSource(new MVELExprDescr("$values.ruleValues"));
                node.setSource(fromDescr);
                node.addConstraint(new ExprConstraintDescr(((AttrElement) e).getRuleAttr().getExpr()));
                p.addDescr(node);
            }
        }

        private BaseDescr createLogicDescr(LogicPredicate logicPredicate) {
            if (LogicPredicate.AND == logicPredicate) {
                return new AndDescr();
            }
            if (LogicPredicate.OR == logicPredicate) {
                return new OrDescr();
            }
            return new AndDescr();
        }
    }

}
