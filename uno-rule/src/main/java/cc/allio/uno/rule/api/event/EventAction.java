package cc.allio.uno.rule.api.event;

import cc.allio.uno.core.bus.EventBusFactory;
import cc.allio.uno.core.bus.TopicEvent;
import cc.allio.uno.rule.api.Action;

/**
 * 基于事件的action
 *
 * @author jiangwei
 * @date 2023/4/25 14:03
 * @since 1.1.4
 */
public class EventAction implements Action {

    @Override
    public void onTrigger(RuleContext context) {
        TopicEvent event = context.getEventRegistry().get(MatchEvent.class);
        if (event != null) {
            EventBusFactory.get().publish(event);
        }
    }
}
