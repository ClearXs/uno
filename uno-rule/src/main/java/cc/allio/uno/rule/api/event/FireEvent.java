package cc.allio.uno.rule.api.event;

import cc.allio.uno.core.bus.EventContext;
import cc.allio.uno.core.bus.TopicKey;
import cc.allio.uno.rule.api.RuleResult;

/**
 * 触发报警规则事件
 *
 * @author j.x
 * @since 1.1.4
 */
public class FireEvent extends BaseEvent {

    public static final String RULE_FIRE_EVENT_TOPIC_PREFIX = "/rule/event/fire/";

    public FireEvent(RuleResult ruleResult, EventContext eventContext) {
        super(ruleResult, eventContext);
    }

    @Override
    public TopicKey getTopicKey() {
        return TopicKey.of(RULE_FIRE_EVENT_TOPIC_PREFIX, new String[]{String.valueOf(ruleResult.getId())});
    }
}
