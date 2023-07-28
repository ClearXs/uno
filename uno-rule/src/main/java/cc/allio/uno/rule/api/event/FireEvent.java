package cc.allio.uno.rule.api.event;

import cc.allio.uno.core.bus.EventContext;
import cc.allio.uno.core.bus.TopicKey;
import cc.allio.uno.rule.api.RuleResult;

/**
 * 触发报警规则事件
 *
 * @author jiangwei
 * @date 2023/4/25 09:04
 * @since 1.1.4
 */
public class FireEvent extends BaseEvent {

    public static final String RULE_FIRE_EVENT_TOPIC_PREFIX = "/rule/event/fire/";

    public FireEvent(RuleResult ruleResult, EventContext eventContext) {
        super(ruleResult, eventContext);
    }

    @Override
    public TopicKey getTopicKey() {
        return TopicKey.create(RULE_FIRE_EVENT_TOPIC_PREFIX, new String[]{String.valueOf(ruleResult.getId())});
    }
}
