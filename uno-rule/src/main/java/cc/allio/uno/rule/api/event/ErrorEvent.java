package cc.allio.uno.rule.api.event;

import cc.allio.uno.core.bus.EventContext;
import cc.allio.uno.core.bus.TopicKey;
import cc.allio.uno.rule.api.RuleResult;

/**
 * 当发生错误时抛出
 *
 * @author j.x
 * @since 1.1.4
 */
public class ErrorEvent extends BaseEvent {

    public static final String RULE_ERROR_EVENT_TOPIC_PREFIX = "/rule/event/error/";

    public ErrorEvent(RuleResult ruleResult, EventContext eventContext) {
        super(ruleResult, eventContext);
    }

    public void setError(Throwable err) {
        getEventContext().put(RuleContext.ERROR, err);
    }

    @Override
    public TopicKey getTopicKey() {
        return TopicKey.of(RULE_ERROR_EVENT_TOPIC_PREFIX, new String[]{String.valueOf(ruleResult.getId())});
    }
}
