package cc.allio.uno.rule.api.event;

import cc.allio.uno.core.bus.EventContext;
import cc.allio.uno.core.bus.TopicKey;
import cc.allio.uno.rule.api.RuleResult;

/**
 * 匹配事件
 *
 * @author j.x
 * @since 1.1.4
 */
public class MatchEvent extends BaseEvent {

    public static final String RULE_MATCH_EVENT_TOPIC_PREFIX = "/rule/event/match/";

    public MatchEvent(RuleResult ruleResult, EventContext eventContext) {
        super(ruleResult, eventContext);
    }

    @Override
    public TopicKey getTopicKey() {
        return TopicKey.create(RULE_MATCH_EVENT_TOPIC_PREFIX, new String[]{String.valueOf(ruleResult.getId())});
    }
}
