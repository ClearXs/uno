package cc.allio.uno.rule.api;

import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.util.ObjectUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.rule.exception.RuleBuilderException;

/**
 * 默认实现
 *
 * @author jiangwei
 * @date 2023/4/23 17:33
 * @since 1.1.4
 */
public class RuleAttrBuilderImpl implements RuleAttrBuilder {

    protected String key;
    protected String unit;
    protected String name;
    protected OP op;
    protected LogicPredicate logic = LogicPredicate.AND;
    protected Object triggerValue;

    @Override
    public RuleAttrBuilder buildKey(String key) {
        this.key = key;
        return this;
    }

    @Override
    public RuleAttrBuilder buildName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public RuleAttrBuilder buildUnit(String unit) {
        this.unit = unit;
        return this;
    }

    @Override
    public RuleAttrBuilder buildOp(OP op) {
        this.op = op;
        return this;
    }

    @Override
    public RuleAttrBuilder buildLogic(LogicPredicate logic) {
        this.logic = logic;
        return this;
    }

    @Override
    public RuleAttrBuilder buildTriggerValue(Object value) {
        this.triggerValue = value;
        return this;
    }

    /**
     * <ul>
     *     <li>key</li>
     *     <li>op</li>
     *     <li>triggerValue</li>
     * </ul>
     * 不能为空
     */
    protected void check() {
        if (StringUtils.isEmpty(key)) {
            throw Exceptions.eee("rule attr key is empty", RuleBuilderException.class);
        }
        if (op == null) {
            throw Exceptions.eee("rule op error", RuleBuilderException.class);
        }
        if (logic == null) {
            throw Exceptions.eee("rule logic error", RuleBuilderException.class);
        }
        if (ObjectUtils.isEmpty(triggerValue)) {
            throw Exceptions.eee("rule triggerValue error", RuleBuilderException.class);
        }
    }

    @Override
    public RuleAttr build() {
        check();
        return new RuleAttrImpl(key, name, unit, op, logic, triggerValue);
    }
}
