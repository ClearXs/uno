package cc.allio.uno.rule.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * 规则匹配的索引
 *
 * @author j.x
 * @date 2023/4/24 18:43
 * @since 1.1.4
 */
@Getter
@AllArgsConstructor
@ToString(of = "ruleAttr")
public class MatchIndex {

    private final RuleAttr ruleAttr;
    private final Object value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchIndex that = (MatchIndex) o;
        return Objects.equals(ruleAttr.getKey(), that.ruleAttr.getKey()) && Objects.equals(ruleAttr.getTriggerValue(), that.getRuleAttr().getTriggerValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleAttr.getKey(), ruleAttr.getTriggerValue());
    }
}
