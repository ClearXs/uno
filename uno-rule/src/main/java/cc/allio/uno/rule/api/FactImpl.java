package cc.allio.uno.rule.api;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.type.TypeOperatorFactory;
import cc.allio.uno.core.util.JsonUtils;
import com.google.common.collect.Maps;
import lombok.ToString;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 事实对象默认实现
 *
 * @author j.x
 * @since 1.1.4
 */
@ToString(of = "variables")
public class FactImpl implements Fact {

    private final Rule rule;
    private final Map<String, Object> variables;

    FactImpl(Rule rule) {
        this(rule, Maps.newHashMap());
    }

    FactImpl(Rule rule, Map<String, Object> variables) {
        this.rule = rule;
        this.variables = variables;
    }

    @Override
    public Rule getRule() {
        return rule;
    }

    @Override
    public Object getValue(String key) {
        return variables.get(key);
    }

    @Override
    public Map<String, Object> ruleValues() {
        Map<String, Object> runtimeValues = Maps.newHashMap();
        Map<String, RuleAttr> keyRuleAttr = rule.getRuleAttr().stream().collect(Collectors.toMap(RuleAttr::getKey, v -> v));
        // 判断fact数据key与rule attr是否一致
        // 1.比较大小
        // 2.比较key是否一致
        Supplier<String> errorMsg = () -> String.format("current rule %s fact variables %s size not equla to rule attr %s", rule.getName(), JsonUtils.toJson(variables), JsonUtils.toJson(rule.getRuleAttr()));
        if (variables.size() != keyRuleAttr.size()) {
            throw new IllegalArgumentException(errorMsg.get());
        }
        for (Entry<String, Object> entry : variables.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (!keyRuleAttr.containsKey(key)) {
                throw new IllegalArgumentException(errorMsg.get());
            }
            Object runValue = StringPool.EMPTY;
            RuleAttr ruleAttr = keyRuleAttr.get(key);
            if (value != null) {
                runValue = TypeOperatorFactory.translator(ruleAttr.getValueType()).convert(value, ruleAttr.getValueType());
            }
            runtimeValues.put(key, runValue);
        }
        return runtimeValues;
    }

    @Override
    public int size() {
        return variables.size();
    }

    @Override
    public boolean isEmpty() {
        return variables.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return variables.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return variables.containsKey(value);
    }

    @Override
    public Object get(Object key) {
        return variables.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return variables.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return variables.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        variables.putAll(m);
    }

    @Override
    public void clear() {
        variables.clear();
    }

    @Override
    public Set<String> keySet() {
        return variables.keySet();
    }

    @Override
    public Collection<Object> values() {
        return variables.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return variables.entrySet();
    }
}
