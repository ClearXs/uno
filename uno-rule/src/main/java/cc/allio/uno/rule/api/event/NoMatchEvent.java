package cc.allio.uno.rule.api.event;

import cc.allio.uno.core.bus.EventContext;
import cc.allio.uno.core.bus.TopicKey;
import cc.allio.uno.rule.api.RuleResult;

/**
 * 没有匹配事件
 *
 * @author j.x
 * @since 1.1.4
 */
public class NoMatchEvent extends BaseEvent {

    public static final String RULE_NO_MATCH_EVENT_TOPIC_PREFIX = "/rule/event/no-match/";

    public NoMatchEvent(RuleResult ruleResult, EventContext eventContext) {
        super(ruleResult, eventContext);
    }

    @Override
    public TopicKey getTopicKey() {
        return TopicKey.of(RULE_NO_MATCH_EVENT_TOPIC_PREFIX, new String[]{String.valueOf(ruleResult.getId())});
    }
}
