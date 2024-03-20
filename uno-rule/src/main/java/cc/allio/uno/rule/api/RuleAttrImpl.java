package cc.allio.uno.rule.api;

import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.template.LangValue;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.Objects;

/**
 * default rule index
 *
 * @author j.x
 * @date 2023/4/23 19:42
 * @see RuleAttrBuilder#get()
 * @since 1.1.4
 */
@Data
@ToString
public class RuleAttrImpl implements RuleAttr {

    private final String key;
    private final String name;
    private final String unit;
    private final OP op;
    private final LogicPredicate logic;
    private final Object originValue;
    private final Object triggerValue;
    private final Class<?> valueType;

    @Setter
    @Getter
    private Rule rule;

    RuleAttrImpl(String key,
                 String unit,
                 String name,
                 OP op,
                 LogicPredicate logic,
                 Object value) {
        this.key = key;
        this.name = name;
        this.unit = unit;
        this.op = op;
        this.logic = logic;
        this.originValue = value;
        this.triggerValue = Types.tryToNumeric(value);
        this.valueType = triggerValue.getClass();
    }

    @Override
    public String getExpr() {
        Map<String, Object> templateVariables = Maps.newHashMap();
        templateVariables.put("key", new LangValue().setValue(getKey()).setLangsym(true));
        templateVariables.put("op", getOp().getSm());
        templateVariables.put("value", new LangValue().setValue(getTriggerValue()).setLangsym(true));
        return TEMPLATE.parseTemplate(EXPRESS_TEMPLATE, templateVariables);
    }

    @Override
    public String getIndexExpr() {
        Map<String, Object> templateVariables = Maps.newHashMap();
        templateVariables.put("key", new LangValue().setValue(getKey()).setLangsym(true));
        templateVariables.put("op", getOp().getSm());
        templateVariables.put("value", new LangValue().setValue(getTriggerValue()).setLangsym(true));
        return TEMPLATE.parseTemplate(SIMPLE_EXPRESS_TEMPLATE, templateVariables);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleAttrImpl ruleAttr = (RuleAttrImpl) o;
        return Objects.equals(key, ruleAttr.key) && op == ruleAttr.op && logic == ruleAttr.logic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, op, logic);
    }
}
