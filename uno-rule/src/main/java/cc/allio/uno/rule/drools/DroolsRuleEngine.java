package cc.allio.uno.rule.drools;

import cc.allio.uno.core.bus.EventBusFactory;
import cc.allio.uno.rule.api.EventBusRuleEngine;
import cc.allio.uno.rule.api.Fact;
import cc.allio.uno.rule.api.Rule;
import cc.allio.uno.rule.api.event.NoMatchEvent;
import cc.allio.uno.rule.api.event.RuleContext;
import cc.allio.uno.rule.exception.RuleResultRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;

/**
 * 基于drools的规则引擎
 *
 * @author jiangwei
 * @date 2023/4/23 17:43
 * @since 1.1.4
 */
@Slf4j
public class DroolsRuleEngine extends EventBusRuleEngine {

    private KieBase kieBase;
    private final DroolsRuleManager droolsRuleManager;

    public DroolsRuleEngine() {
        super();
        this.droolsRuleManager = new DroolsRuleManager();
    }

    @Override
    protected void onFire(RuleContext context) {
        KieSession kieSession = null;
        Rule rule = context.getCurrentRule();
        if (rule == null) {
            throw new RuleResultRuntimeException("fire rule is empty");
        }
        try {
            synchronized (this) {
                // 判断是否创建rule实例
                if (!droolsRuleManager.compareRuleAndRemoveOldRule(rule)) {
                    droolsRuleManager.buildRule(rule);
                    // 重新构建kbase
                    kieBase = droolsRuleManager.getKBuilder().newKieBase();
                }
                if (kieBase == null) {
                    kieBase = droolsRuleManager.getKBuilder().newKieBase();
                }
                kieSession = kieBase.newKieSession();
            }
        } catch (Throwable ex) {
            onError(ex, context);
        }

        if (kieSession != null) {
            kieSession.setGlobal(DroolsRuleManager.GLOBAL_ACTION_NAME, action);
            kieSession.setGlobal(DroolsRuleManager.GLOBAL_CONTEXT_NAME, context);
            try {
                if (!context.get(RuleContext.CURRENT_FACT, Fact.class).isPresent()) {
                    onError(new RuleResultRuntimeException(String.format("rule %s fact is empty", rule.getName())), context);
                }
                Fact fact = context.get(RuleContext.CURRENT_FACT, Fact.class).get();
                kieSession.insert(fact);
                if (log.isDebugEnabled()) {
                    log.debug("drools execute expression is {}, the fact object is {}", rule.getLiteralExpr(), fact);
                }
                // 触发drools rule
                int fireCounts = kieSession.fireAllRules(match -> match.getRule().getName().equals(rule.getName()));
                // 没有触发时抛出 no match event
                if (fireCounts <= 0) {
                    NoMatchEvent noMatchEvent = context.getEventRegistry().get(NoMatchEvent.class);
                    EventBusFactory.get().publish(noMatchEvent);
                }
            } catch (Throwable ex) {
                onError(ex, context);
            }
        }
    }
}