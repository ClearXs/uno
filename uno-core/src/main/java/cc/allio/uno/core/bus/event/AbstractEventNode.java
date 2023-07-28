package cc.allio.uno.core.bus.event;

import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.ObjectUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 抽象的基于事件的主题Node
 *
 * @author jiangwei
 * @date 2021/12/19 12:21 PM
 * @since 1.0
 */
@Slf4j
public abstract class AbstractEventNode<C> implements Node<C> {

    protected final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 维护监听器缓存
     * <p>在原先采用的是{@link org.springframework.util.LinkedMultiValueMap}作为存储，但是在超万条数据量下，存在null的数据</p>
     */
    protected final Map<Class<? extends BusEvent>, List<Listener.IdListener<C>>> lisCache = Maps.newConcurrentMap();

    /**
     * 注册信息的监听器
     *
     * @param listener 监听器实例对象
     */
    protected Long registerListen(Listener<C> listener) {
        if (ObjectUtils.isEmpty(listener)) {
            throw new NullPointerException("register listener is empty");
        }
        Lock writeLock = lock.readLock();
        try {
            writeLock.lock();
            Listener.IdListener<C> idListener = new Listener.IdListener<>(listener);
            Class<? extends BusEvent> eventType = idListener.getEventType();
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
        return update(retrieval(eventContext.getTopicEvent()), eventContext);
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
    public Listener<C>[] retrieval(Class<? extends BusEvent> eventType) {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            return lisCache.getOrDefault(eventType, Collections.emptyList()).toArray(new Listener[]{});
        } finally {
            readLock.unlock();
        }
    }

}
