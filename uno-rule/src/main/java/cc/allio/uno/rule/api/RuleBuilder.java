package cc.allio.uno.rule.api;

import cc.allio.uno.rule.exception.RuleBuilderException;

import java.util.List;

/**
 * drools rule builder
 *
 * @author j.x
 * @since 1.1.4
 */
public interface RuleBuilder {

    /**
     * 构建规则id
     *
     * @param id id
     * @return RuleBuilder
     */
    RuleBuilder buildRuleId(long id);

    /**
     * 构建规则名称
     *
     * @param name 规则名称
     * @return RuleBuilder
     */
    RuleBuilder buildRuleName(String name);

    /**
     * 添加rule index到默认的rulegroup中
     *
     * @param ruleAttrs ruleAttrs
     * @return RuleBuilder
     */
    default RuleBuilder addRuleAttrs(List<RuleAttr> ruleAttrs) {
        for (RuleAttr ruleAttr : ruleAttrs) {
            addRuleAttr(ruleAttr);
        }
        return this;
    }

    /**
     * 添加rule index到默认的rulegroup中
     *
     * @param ruleAttr ruleAttr
     * @return RuleBuilder
     */
    RuleBuilder addRuleAttr(RuleAttr ruleAttr);

    /**
     * 构建rule实例
     *
     * @return Rule
     * @throws RuleBuilderException 构建过程抛出异常
     */
    Rule build();

    /**
     * 获取RuleBuilder实例
     *
     * @return RuleBuilder instance
     */
    static RuleBuilder get() {
        return new RuleBuilderImpl();
    }
}
