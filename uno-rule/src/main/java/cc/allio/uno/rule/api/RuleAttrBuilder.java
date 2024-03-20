package cc.allio.uno.rule.api;

import cc.allio.uno.rule.exception.RuleBuilderException;

/**
 * rule index builder
 *
 * @author j.x
 * @date 2023/4/23 17:28
 * @see RuleAttr
 * @since 1.1.4
 */
public interface RuleAttrBuilder {

    /**
     * 构建key
     *
     * @return RuleIndexBuilder
     */
    RuleAttrBuilder buildKey(String key);

    /**
     * 构建name
     *
     * @return RuleIndexBuilder
     */
    RuleAttrBuilder buildName(String name);

    /**
     * 构建unit
     *
     * @return RuleIndexBuilder
     */
    RuleAttrBuilder buildUnit(String unit);

    /**
     * 构建值比较符
     *
     * @param op op
     * @return RuleIndexBuilder
     */
    RuleAttrBuilder buildOp(OP op);

    /**
     * 构建逻辑符
     *
     * @param logic logic
     * @return RuleIndexBuilder
     */
    RuleAttrBuilder buildLogic(LogicPredicate logic);

    /**
     * trigger value
     *
     * @param value value
     * @return RuleIndexBuilder
     */
    RuleAttrBuilder buildTriggerValue(Object value);

    /**
     * 构建Rule index实例
     *
     * @return RuleIndex
     * @throws RuleBuilderException 构建过程失败则抛出
     */
    RuleAttr build();

    /**
     * 获取RuleIndexBuilder实例
     *
     * @return RuleIndexBuilder instance
     */
    static RuleAttrBuilder get() {
        return new RuleAttrBuilderImpl();
    }
}
