package cc.allio.uno.rule.api;

import cc.allio.uno.core.bus.EventBusFactory;
import cc.allio.uno.core.bus.EventContext;
import cc.allio.uno.core.bus.event.Node;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.reactive.Reactives;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.rule.api.event.*;
import cc.allio.uno.rule.exception.RuleResultRuntimeException;
import cc.allio.uno.rule.exception.RuleResultTimeoutException;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * 默认规则结果集，基于event-bus与定时器机制进行实现。
 *
 * @author j.x
 * @since 1.1.4
 */
@Slf4j
public class DefaultRuleResult implements RuleResult {

    @Getter
    private final RuleEngine ruleEngine;
    @Getter
    private final Rule rule;
    @Getter
    private final Long id;
    @Getter
    private final Fact fact;
    private Disposable disposable;
    private final RuleContext fireContext;

    // listener registry
    private final List<Listener> listenerRegistry = Lists.newCopyOnWriteArrayList();

    // resultLock
    private final Lock resultLock;
    private final Condition resultCondition;
    // 结果集异常
    private Throwable resultErr;
    private Listener resultListener;

    // 结果集，规则匹配的索引
    private Set<MatchIndex> matchs = Collections.emptySet();
    private final AtomicBoolean isMatched = new AtomicBoolean(false);
    // 同步模式下是否已经进行唤醒
    private final AtomicBoolean isSingle = new AtomicBoolean(false);

    // 结果集存活时间
    private final Duration alive;
    private final AtomicBoolean isShutdown = new AtomicBoolean(false);
    private static Scheduler timer = Schedulers.newSingle("rule-result-scheduler");
    private Disposable timeDispose;

    // 生成结果集时间
    private volatile Date produceTime;
    private static final AtomicReferenceFieldUpdater<DefaultRuleResult, Date> PRODUCE_TIME =
            AtomicReferenceFieldUpdater.newUpdater(DefaultRuleResult.class, Date.class, "produceTime");

    public DefaultRuleResult(RuleEngine ruleEngine,
                             Long id,
                             Rule rule,
                             Fact fact,
                             RuleContext context,
                             Duration alive) {
        this.ruleEngine = ruleEngine;
        this.rule = rule;
        this.id = id;
        this.fact = fact;
        this.fireContext = context;
        this.resultLock = new ReentrantLock();
        this.resultCondition = resultLock.newCondition();
        this.alive = alive;
    }

    @Override
    public Set<MatchIndex> get(long timeout, TimeUnit timeUnit) throws RuleResultTimeoutException {
        if (!resultLock.tryLock()) {
            throw new RuleResultTimeoutException("result lock failed");
        }
        try {
            resultLock.lock();
            // add default for notify getValue() result
            SignalListener signalListener = new SignalListener(this);
            addLister(signalListener);
            while (!isSingle.get()) {
                // 当 match、no-match、err事件发生时该线程将会被唤醒
                boolean await = resultCondition.await(timeout, timeUnit);
                if (await) {
                    break;
                } else {
                    throw new InterruptedException(String.format("more than timeout %s", timeout));
                }
            }
            if (resultErr != null) {
                throw Exceptions.eee(resultErr, RuleResultRuntimeException.class);
            }
            return Collections.unmodifiableSet(matchs);
        } catch (InterruptedException ex) {
            throw Exceptions.eee(ex, RuleResultTimeoutException.class);
        } finally {
            resultLock.unlock();
            listenerRegistry.remove(resultListener);
            resultListener = null;
        }

    }

    @Override
    public Flux<MatchIndex> getOnReactive() {
        if (disposable == null) {
            Flux<EventContext> errStream = subscribeErr(null);
            Flux<EventContext> noMatchStream = subscribeNoMatch(null);
            Flux<EventContext> matchStream = subscribeMatch(null);
            disposable = Reactives.onErrorContinue(Flux.merge(errStream, noMatchStream, matchStream)).subscribe();
        }
        publishFire();
        // wait rule fire
        resultLock.lock();
        try {
            while (!isSingle.get()) {
                // 当 match、no-match、err事件发生时该线程将会被唤醒
                boolean await;
                try {
                    await = resultCondition.await(DEFAULT_TIME_OUT, DEFAULT_TIME_OUT_UNIT);
                } catch (InterruptedException err) {
                    return Flux.error(err);
                }
                if (await) {
                    break;
                } else {
                    return Flux.error(new InterruptedException(String.format("more than timeout %s", DEFAULT_TIME_OUT)));
                }
            }
        } finally {
            resultLock.unlock();
        }
        return Flux.defer(() -> Flux.fromIterable(matchs));
    }

    @Override
    public boolean isMatched() {
        return isMatched.get();
    }

    @Override
    public boolean isMatchedOnSync() {
        get();
        return isMatched();
    }

    @Override
    public Mono<Boolean> isMatchedOnReactive() {
        return getOnReactive().then(Mono.defer(() -> Mono.just(isMatched.get())));
    }

    @Override
    public void addLister(Listener listener) {
        if (listener == null) {
            throw Exceptions.eee(String.format("rule result from rule %s listener is empty", rule.getName()), RuleResultRuntimeException.class);
        }
        if (isShutdown.get()) {
            throw Exceptions.eee(String.format("rule result from rule %s timeout ", rule.getName()), RuleResultTimeoutException.class);
        }
        try {
            resultLock.lock();
            listenerRegistry.add(listener);
            if (disposable == null) {
                Flux<EventContext> errStream = subscribeErr(err -> listenerRegistry.forEach(l -> l.onError(err)));
                Flux<EventContext> noMatchStream = subscribeNoMatch(c -> listenerRegistry.forEach(l -> l.onNoMatch(rule)));
                Flux<EventContext> matchStream = subscribeMatch(matchIndices -> listenerRegistry.forEach(l -> l.onTrigger(rule, matchIndices)));
                disposable = Reactives.onErrorContinue(Flux.merge(errStream, noMatchStream, matchStream)).subscribe();
            }
            // 发布数据
            publishFire();
            // 添加存活时间
            if (alive != null) {
                timeDispose =
                        timer.schedule(
                                () -> {
                                    clear();
                                    isShutdown.compareAndSet(false, true);
                                },
                                alive.toMillis(),
                                TimeUnit.MILLISECONDS);
            }
        } finally {
            resultLock.unlock();
        }
    }

    @Override
    public Date getResultTime() {
        return PRODUCE_TIME.get(this);
    }

    @Override
    public Throwable getErr() {
        return resultErr;
    }

    @Override
    public void release() {
        if (disposable != null) {
            disposable.dispose();
        }
        if (CollectionUtils.isNotEmpty(listenerRegistry)) {
            listenerRegistry.clear();
        }
        if (timeDispose != null && !timeDispose.isDisposed()) {
            timeDispose.dispose();
        }
    }

    /**
     * 清除资源
     */
    void clear() {
        ruleEngine.finish(this);
    }

    /**
     * 设置规则处理时间
     */
    void setProduceTime() {
        PRODUCE_TIME.compareAndSet(this, null, DateUtil.now());
    }

    /**
     * publish on rule fire
     */
    void publishFire() {
        EventBusFactory.current().publish(fireContext.getEventRegistry().get(FireEvent.class));
    }

    /**
     * 订阅没有匹配的规则项
     *
     * @param c 没有匹配时的回调
     * @return EventContext for flux
     */
    Flux<EventContext> subscribeNoMatch(Consumer<EventContext> c) {
        return EventBusFactory.current().subscribeOnRepeatable(fireContext.getEventRegistry().get(NoMatchEvent.class))
                .flatMap(Node::onNext)
                .doOnNext(eventContext -> {
                    resultLock.lock();
                    try {
                        if (log.isDebugEnabled()) {
                            log.debug("rule {} fire no matched", rule);
                        }
                        onSignal();
                        setProduceTime();
                        if (c != null) {
                            c.accept(eventContext);
                        }
                    } finally {
                        resultLock.unlock();
                    }
                });
    }

    /**
     * 订阅匹配的规则项
     *
     * @param c 有匹配时的回调
     * @return MatchIndex for mono list
     */
    Flux<EventContext> subscribeMatch(Consumer<Set<MatchIndex>> c) {
        return EventBusFactory.current().subscribeOnRepeatable(fireContext.getEventRegistry().get(MatchEvent.class))
                .flatMap(Node::onNext)
                .doOnNext(eventContext ->
                        eventContext.get(RuleContext.MATCH_INDEX, Set.class)
                                .ifPresent(m -> {
                                    resultLock.lock();
                                    try {
                                        if (log.isDebugEnabled()) {
                                            log.debug("rule {} fire result is matched, the matched list is {}", rule, m);
                                        }
                                        onSignal();
                                        // 触发多次不进行匹配
                                        if (isMatched.compareAndSet(false, true) && !isShutdown.get()) {
                                            isSingle.compareAndSet(false, true);
                                            matchs = m;
                                            setProduceTime();
                                            if (c != null) {
                                                c.accept(m);
                                            }
                                        }
                                    } finally {
                                        resultLock.unlock();
                                    }
                                }));
    }

    /**
     * 订阅匹配执行过程中的错误
     *
     * @param c 发生错误时的回调
     * @return Throwable for flux
     */
    Flux<EventContext> subscribeErr(Consumer<Throwable> c) {
        return EventBusFactory.current().subscribeOnRepeatable(fireContext.getEventRegistry().get(ErrorEvent.class))
                .flatMap(Node::onNext)
                .doOnNext(eventContext ->
                        eventContext.get(RuleContext.ERROR, Throwable.class)
                                .ifPresent(err -> {
                                    resultLock.lock();
                                    try {
                                        if (log.isDebugEnabled()) {
                                            log.debug("rule {} fire result has err", rule, err);
                                        }
                                        onSignal();
                                        resultErr = err;
                                        setProduceTime();
                                        if (c != null) {
                                            c.accept(err);
                                        }
                                    } finally {
                                        resultLock.unlock();
                                    }
                                }));
    }

    @Override
    public String toString() {
        return "DefaultRuleResult{" +
                "rule=" + rule +
                ", id=" + id +
                ", fact=" + fact +
                '}';
    }

    void onSignal() {
        resultLock.lock();
        try {
            if (isSingle.compareAndSet(false, true)) {
                resultCondition.signalAll();
            }
        } finally {
            resultLock.unlock();
        }
    }

    static class SignalListener implements Listener {

        private final DefaultRuleResult result;

        public SignalListener(DefaultRuleResult result) {
            this.result = result;
        }

        @Override
        public void onTrigger(Rule rule, Set<MatchIndex> matchIndices) {
            result.onSignal();
        }

        @Override
        public void onNoMatch(Rule rule) {
            result.onSignal();
        }

        @Override
        public void onError(Throwable ex) {
            result.onSignal();
        }
    }

}
