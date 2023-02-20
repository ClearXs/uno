package cc.allio.uno.core.bus.event;

import cc.allio.uno.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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
public abstract class AbstractEventNode<C> implements EventNode<C> {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 维护监听器缓存
     * <p>在原先采用的是{@link org.springframework.util.LinkedMultiValueMap}作为存储，但是在超万条数据量下，存在null的数据</p>
     */
    private final Map<Class<? extends TopicEvent>, List<Listener.IdListener<C>>> lisCache = Maps.newConcurrentMap();

    /**
     * 有序任务序列，在多线程环境下，任务执行顺序可能是乱序状态，以此来提供有序任务
     */
    private final Queue<Listener<C>> tasks = Queues.newConcurrentLinkedQueue();

    /**
     * 注册信息的监听器
     *
     * @param listener 监听器实例对象
     */
    protected Long registerListen(Listener<C> listener) {
        if (ObjectUtil.isEmpty(listener)) {
            throw new NullPointerException("register listener is empty");
        }
        Lock writeLock = lock.readLock();
        writeLock.lock();
        try {
            Listener.IdListener<C> idListener = new Listener.IdListener<>(listener);
            Class<? extends TopicEvent> eventType = idListener.getEventType();
            List<Listener.IdListener<C>> listeners = lisCache.get(eventType);
            if (cc.allio.uno.core.util.Collections.isEmpty(listeners)) {
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
    public void update(EventContext<C> eventContext) {
        update(retrieval(eventContext.getTopicEvent()), eventContext);
    }

    @Override
    public void update(Listener<C>[] listeners, EventContext<C> eventContext) {
        for (Listener<C> listener : listeners) {
            tasks.offer(listener);
        }
        Listener<C> listener;
        while ((listener = tasks.poll()) != null) {
            new AsyncSubscriber<>(this, listener).publishEvent(eventContext);
        }
    }

    @Override
    public Listener<C>[] retrieval(Class<? extends TopicEvent> eventType) {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            return lisCache.getOrDefault(eventType, Collections.emptyList()).toArray(new Listener[]{});
        } finally {
            readLock.unlock();
        }
    }


    /**
     * <b>异步订阅者，提供异步回调监听</b>
     */
    static class AsyncSubscriber<C> {

        /**
         * 默认事件执行器
         */
        private static final EventExecutor DISPATCHER = new DefaultEventExecutor(new DefaultThreadFactory(AsyncSubscriber.class));

        private final Listener<C> listener;
        private final AbstractEventNode<C> node;

        public AsyncSubscriber(AbstractEventNode<C> node, Listener<C> listener) {
            this.listener = listener;
            this.node = node;
        }

        void publishEvent(EventContext<C> eventContext) {
            DISPATCHER.execute(() -> {
                try {
                    listener.listen(node, eventContext.getSource());
                } catch (Throwable ex) {
                    // Ignore Just Record Exception
                    log.error("Listener Callback error, Belong Topic: {}", node.getTopic(), ex);
                }
            });
        }
    }
}
