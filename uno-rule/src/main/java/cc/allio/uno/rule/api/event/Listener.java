package cc.allio.uno.rule.api.event;

import cc.allio.uno.rule.api.MatchIndex;
import cc.allio.uno.rule.api.Rule;

import java.util.Set;

/**
 * 规则触发的监听器
 *
 * @author j.x
 * @since 1.1.4
 */
public interface Listener {

    /**
     * 当规则匹配执行后触发
     *
     * @param ruleResult ruleResult
     */
    void onTrigger(Rule rule, Set<MatchIndex> matchIndices);

    /**
     * 当规则没有触发后执行
     */
    void onNoMatch(Rule rule);

    /**
     * 当执行规则错误时抛出
     *
     * @param ex ex
     */
    void onError(Throwable ex);
}
