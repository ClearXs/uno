package cc.allio.uno.rule.api;

import cc.allio.uno.core.bus.EventBus;
import cc.allio.uno.core.bus.EventBusFactory;
import cc.allio.uno.core.bus.EventRegistry;
import cc.allio.uno.core.bus.event.EmitEvent;
import cc.allio.uno.rule.api.event.*;
import cc.allio.uno.rule.exception.RuleResultRuntimeException;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 基于{@link EventBus}的规则引擎
 *
 * @author j.x
 * @since 1.1.4
 */
public abstract class EventBusRuleEngine implements RuleEngine {

    protected final Action action;
    private final AtomicLong nextId = new AtomicLong(0L);

    protected EventBusRuleEngine() {
        this.action = new EventAction();
    }

    @Override
    public synchronized RuleResult fire(Rule rule, Fact fact, Duration timer) {
        if (rule == null) {
            throw new RuleResultRuntimeException("rule is empty");
        }
        // 构建规则执行上下文对象
        RuleContext context = new RuleContext();
        DefaultRuleResult ruleResult = new DefaultRuleResult(
                this,
                nextId.getAndIncrement(),
                rule,
                fact,
                context,
                timer);
        // setValue context value
        context.putCurrentRule(rule);
        context.putCurrentFact(fact);
        context.putResultRule(ruleResult);
        EventRegistry eventRegistry = new EventRegistry();
        loadAllEvent(eventRegistry, ruleResult, context);
        context.putEventRegistry(eventRegistry);
        FireEvent event = context.getEventRegistry().get(FireEvent.class);
        if (event == null) {
            throw new RuleResultRuntimeException("fire event must not null");
        }
        // 订阅规则释放事件
        EventBusFactory.current().subscribeOnRepeatable(event).subscribe();
        return ruleResult;
    }

    /**
     * 加载规则所有事件
     *
     * @param eventRegistry 事件中心
     * @param ruleResult    结果集
     * @param context       上下文
     */
    private void loadAllEvent(EventRegistry eventRegistry, RuleResult ruleResult, RuleContext context) {
        eventRegistry.put(FireEvent.class, new FireEvent(ruleResult, context));
        eventRegistry.put(ErrorEvent.class, new ErrorEvent(ruleResult, context));
        eventRegistry.put(MatchEvent.class, new MatchEvent(ruleResult, context));
        eventRegistry.put(NoMatchEvent.class, new NoMatchEvent(ruleResult, context));
    }

    /**
     * 子类实现，当需要进行规则触发时调用
     *
     * @param ruleContext ruleContext
     */
    protected abstract void onFire(RuleContext ruleContext);

    /**
     * 解析错误
     *
     * @param ex      ex
     * @param context context
     */
    protected void onError(Throwable ex, RuleContext context) {
        ErrorEvent event = context.getEventRegistry().get(ErrorEvent.class);
        event.setError(ex);
        EventBusFactory.current().publish(event).subscribe();
    }

    @Override
    public synchronized void finish(RuleResult result) {
        if (result != null) {
            result.release();
        }
    }
}
