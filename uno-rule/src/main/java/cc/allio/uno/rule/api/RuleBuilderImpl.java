package cc.allio.uno.rule.api;

import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.rule.exception.RuleBuilderException;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * RuleBuilderImpl 默认实现
 *
 * @author j.x
 * @date 2023/4/23 17:05
 * @since 1.1.4
 */
public class RuleBuilderImpl implements RuleBuilder {

    protected long id;
    protected String name;
    protected List<RuleAttr> attrs;

    @Override
    public RuleBuilder buildRuleId(long id) {
        this.id = id;
        return this;
    }

    @Override
    public RuleBuilder buildRuleName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 检查
     */
    protected void check() {
        if (StringUtils.isEmpty(name)) {
            throw Exceptions.eee("rule name is empty", RuleBuilderException.class);
        }
        if (CollectionUtils.isEmpty(attrs)) {
            throw Exceptions.eee("rule attrs is empty", RuleBuilderException.class);
        }
    }

    @Override
    public RuleBuilder addRuleAttr(RuleAttr ruleAttr) {
        if (attrs == null) {
            attrs = Lists.newArrayList();
        }
        attrs.add(ruleAttr);
        return this;
    }

    @Override
    public Rule build() {
        check();
        // 设置rule
        RuleImpl rule = new RuleImpl(id, name, attrs);
        attrs.forEach(r -> ((RuleAttrImpl) r).setRule(rule));
        return rule;
    }
}
