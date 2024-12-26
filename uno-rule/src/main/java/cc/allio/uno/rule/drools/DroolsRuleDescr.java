package cc.allio.uno.rule.drools;

import cc.allio.uno.rule.api.Rule;
import lombok.Getter;
import org.drools.drl.ast.descr.RuleDescr;

/**
 * 添加{@link Rule}实例
 *
 * @author j.x
 * @since 1.1.4
 */
public class DroolsRuleDescr extends RuleDescr {

    @Getter
    private Rule rule;

    public DroolsRuleDescr(String name, Rule rule) {
        super(name);
        this.rule = rule;
    }
}
