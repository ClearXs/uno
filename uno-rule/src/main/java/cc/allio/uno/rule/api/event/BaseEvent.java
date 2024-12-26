package cc.allio.uno.rule.api.event;

import cc.allio.uno.core.bus.ContextTopicEvent;
import cc.allio.uno.core.bus.EventContext;
import cc.allio.uno.rule.api.RuleResult;
import lombok.Getter;

/**
 * rule event on base
 *
 * @author j.x
 * @since 1.1.4
 */
public abstract class BaseEvent extends ContextTopicEvent {

    @Getter
    protected final RuleResult ruleResult;

    protected BaseEvent(RuleResult ruleResult, EventContext eventContext) {
        super(eventContext);
        this.ruleResult = ruleResult;
    }

}
