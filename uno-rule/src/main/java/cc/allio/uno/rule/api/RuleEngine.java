package cc.allio.uno.rule.api;

import java.time.Duration;

/**
 * rule engine 定义
 *
 * @author j.x
 * @date 2023/4/23 17:20
 * @since 1.1.4
 */
public interface RuleEngine {

    /**
     * 执行规则
     *
     * @param rule rule
     * @param fact fact
     * @return RuleResult
     */
    default RuleResult fire(Rule rule, Fact fact) {
        return fire(rule, fact, null);
    }

    /**
     * 执行规则，按照给定的时间保留结果集，超过指定时间后则销毁结果集。
     *
     * @param rule  rule
     * @param fact  fact
     * @param timer timer
     * @return RuleResult
     */
    RuleResult fire(Rule rule, Fact fact, Duration timer);

    /**
     * 结束RuleResult
     *
     * @param result result
     */
    void finish(RuleResult result);
}
