package cc.allio.uno.core.bus.event;

import com.google.common.collect.Queues;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Queue;

/**
 * 基于{@link EventExecutor}的事件回调处理
 *
 * @author jiangwei
 * @date 2023/5/19 13:23
 * @since 1.1.4
 */
@Slf4j
public class ExecutorEventNode<C> extends EventNode<C> {

    /**
     * 有序任务序列，在多线程环境下，任务执行顺序可能是乱序状态，以此来提供有序任务
     */
    private final Queue<Listener<C>> tasks = Queues.newConcurrentLinkedQueue();

    public ExecutorEventNode(Long subscribeId, String topic) {
        super(subscribeId, topic);
    }

    @Override
    public Mono<C> update(Listener<C>[] listeners, Context<C> eventContext) {
        for (Listener<C> listener : listeners) {
            tasks.offer(listener);
        }
        Listener<C> listener;
        while ((listener = tasks.poll()) != null) {
            new AsyncSubscriber<>(this, listener).publishEvent(eventContext);
        }
        return Mono.just(eventContext.getSource());
    }

    /**
     * <b>异步订阅者，提供异步回调监听</b>
     */
    static class AsyncSubscriber<C> {

        /**
         * 默认事件执行器
         */
        private static final EventExecutorGroup DISPATCHER =
                // 默认为拒绝策略
                new DefaultEventExecutorGroup(
                        Runtime.getRuntime().availableProcessors() * 2,
                        new DefaultThreadFactory("eventNode"));

        private final Listener<C> listener;
        private final AbstractEventNode<C> node;

        public AsyncSubscriber(AbstractEventNode<C> node, Listener<C> listener) {
            this.listener = listener;
            this.node = node;
        }

        void publishEvent(Context<C> eventContext) {
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
