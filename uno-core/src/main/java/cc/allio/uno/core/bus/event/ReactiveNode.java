package cc.allio.uno.core.bus.event;

import cc.allio.uno.core.bus.EventContext;
import cc.allio.uno.core.bus.Topic;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.ObjectUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 * 基于 reactive 事件处理
 *
 * @author j.x
 * @since 1.1.4
 */
@Slf4j
@EqualsAndHashCode(of = "subscriberId")
public class ReactiveNode<C extends EventContext> implements Node<C> {

    final ReadWriteLock lock;

    /**
     * 维护监听器缓存
     * <p>在原先采用的是{@link org.springframework.util.LinkedMultiValueMap}作为存储，但是在超万条数据量下，存在null的数据</p>
     */
    final Map<Class<? extends Event>, List<Listener.IdListener<C>>> lisCache;

    /**
     * 当前Node唯一订阅id
     */
    final Long subscriberId;

    /**
     * 当前Node订阅的Topic
     */
    final Topic<C> topic;

    public ReactiveNode(Long subscribeId, Topic<C> topic) {
        this.lock = new ReentrantReadWriteLock();
        this.lisCache = Maps.newConcurrentMap();
        this.subscriberId = subscribeId;
        this.topic = topic;
    }

    @Override
    public Long getSubscribeId() {
        return this.subscriberId;
    }

    @Override
    public Topic<C> getTopic() {
        return this.topic;
    }

    @Override
    public Long doEmmit(Consumer<C> onNext) {
        Listener<C> emitLis = new Listener<C>() {
            @Override
            public void listen(Node<C> event, C obj) {
                if (obj != null) {
                    onNext.accept(obj);
                }
            }

            @Override
            public Class<? extends Event> getEventType() {
                return EmitEvent.class;
            }
        };
        return registerListen(emitLis);
    }

    @Override
    public Long doLift(LongConsumer consumer) {
        Listener<C> listLis = new Listener<C>() {
            @Override
            public void listen(Node<C> event, C obj) {
                consumer.accept(getSubscribeId());
            }

            @Override
            public Class<? extends Event> getEventType() {
                return LiftEvent.class;
            }
        };
        return registerListen(listLis);
    }

    @Override
    public Long reply(@NonNull Class<? extends Event> eventType, @NonNull Consumer<C> consumer) {
        Listener<C> triggerListener = new Listener<>() {
            @Override
            public void listen(Node<C> event, C obj) {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("subscribe id {}, subscribe topic {}, then emit obj is {} ", subscriberId, topic, obj);
                    }
                    consumer.accept(obj);
                } catch (Throwable err) {
                    log.warn("Trigger event callback error", err);
                }
            }

            @Override
            public Class<? extends Event> getEventType() {
                return eventType;
            }
        };
        return registerListen(triggerListener);
    }

    @Override
    public Mono<C> update(Listener<C>[] listeners, Context<C> eventContext) {
        return Flux.fromArray(listeners)
                // callback consumer
                .doOnNext(l -> l.listen(this, eventContext.getSource()))
                .onErrorContinue((err, l) -> log.error("Listener Callback error, Belong Topic: {}", getTopic(), err))
                .then(Mono.just(eventContext.getSource()));
    }

    /**
     * 注册信息的监听器
     *
     * @param listener 监听器实例对象
     */
    Long registerListen(Listener<C> listener) {
        if (ObjectUtils.isEmpty(listener)) {
            throw new NullPointerException("register listener is empty");
        }
        Lock writeLock = lock.readLock();
        try {
            writeLock.lock();
            Listener.IdListener<C> idListener = new Listener.IdListener<>(listener);
            Class<? extends Event> eventType = idListener.getEventType();
            List<Listener.IdListener<C>> listeners = lisCache.get(eventType);
            if (CollectionUtils.isEmpty(listeners)) {
                listeners = Lists.newCopyOnWriteArrayList();
                lisCache.put(eventType, listeners);
            }
            listeners.add(idListener);
            return idListener.getListenerId();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Mono<C> update(Context<C> eventContext) {
        return update(retrieve(eventContext.getTopicEvent()), eventContext);
    }

    @Override
    public void release(Long listenerId) {
        Lock writeLock = lock.writeLock();
        writeLock.lock();
        try {
            lisCache.values()
                    .forEach(lis ->
                            lis.stream()
                                    .filter(l -> l.getListenerId() == listenerId)
                                    .findFirst()
                                    .ifPresent(lis::remove));
        } finally {
            writeLock.unlock();
        }

    }

    @Override
    public Listener<C>[] retrieve(Class<? extends Event> eventType) {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            return lisCache.getOrDefault(eventType, Collections.emptyList()).toArray(new Listener[]{});
        } finally {
            readLock.unlock();
        }
    }
}
