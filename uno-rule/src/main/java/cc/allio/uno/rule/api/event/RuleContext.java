package cc.allio.uno.rule.api.event;

import cc.allio.uno.core.bus.DefaultEventContext;
import cc.allio.uno.core.bus.EventRegistry;
import cc.allio.uno.rule.api.Fact;
import cc.allio.uno.rule.api.MatchIndex;
import cc.allio.uno.rule.api.Rule;
import cc.allio.uno.rule.api.RuleResult;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Rule运行时上下文对象
 *
 * @author j.x
 * @since 1.1.4
 */
public class RuleContext extends DefaultEventContext {

    // 上下文常量
    public static final String CURRENT_RULE = "current_rule";
    public static final String CURRENT_FACT = "current_fact";
    public static final String MATCH_INDEX = "match_index";
    public static final String RULE_RESULT = "rule_result";
    public static final String ERROR = "error";
    public static final String EVENT_REGISTRY = "event_registry";

    public RuleContext() {
        super();
        putAttribute(MATCH_INDEX, Sets.newHashSet());
    }

    /**
     * 放入当前rule实例
     *
     * @param currentRule currentRule
     */
    public void putCurrentRule(Rule currentRule) {
        putAttribute(CURRENT_RULE, currentRule);
    }

    /**
     * 获取当前rule实例
     *
     * @return rule instance or null
     */
    public Rule getCurrentRule() {
        return getForce(CURRENT_RULE, Rule.class);
    }

    /**
     * 放入当前fact实例
     *
     * @param currentFact currentFact
     */
    public void putCurrentFact(Fact currentFact) {
        putAttribute(CURRENT_FACT, currentFact);
    }

    /**
     * 获取当前fact实例
     *
     * @return fact instance or null
     */
    public Fact getCurrentFact() {
        return getForce(CURRENT_FACT, Fact.class);
    }

    /**
     * 存入匹配的index实例
     *
     * @param matchIndex matchIndex
     */
    public void putMatchIndex(MatchIndex matchIndex) {
        getMatchIndex().add(matchIndex);
    }

    /**
     * 获取匹配的index
     *
     * @return MatchIndex list
     */
    public Set<MatchIndex> getMatchIndex() {
        return getForce(MATCH_INDEX, Set.class);
    }

    /**
     * 放入rule result
     *
     * @param ruleResult ruleResult
     */
    public void putResultRule(RuleResult ruleResult) {
        putAttribute(RULE_RESULT, ruleResult);
    }

    /**
     * 获取rule result实例
     *
     * @return rule result instance or null
     */
    public RuleResult getRuleResult() {
        return getForce(RULE_RESULT, RuleResult.class);
    }

    /**
     * 放入err错误
     *
     * @param err err
     */
    public void putErr(Throwable err) {
        putAttribute(ERROR, err);
    }

    /**
     * 获取err
     *
     * @return err instance or null
     */
    public Throwable getErr() {
        return getForce(ERROR, Throwable.class);
    }

    /**
     * 放入 event registry
     *
     * @param eventRegistry eventRegistry
     */
    public void putEventRegistry(EventRegistry eventRegistry) {
        putAttribute(EVENT_REGISTRY, eventRegistry);
    }

    /**
     * 获取event registry
     *
     * @return EventRegistry
     */
    public EventRegistry getEventRegistry() {
        return getForce(EVENT_REGISTRY, EventRegistry.class);
    }
}
