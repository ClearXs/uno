package cc.allio.uno.core.bus;

import cc.allio.uno.core.bus.event.*;
import cc.allio.uno.core.bus.event.EventNode;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/**
 * Topic-Node之间的通知者，一对一的关系
 *
 * @param <C> 通知者通知泛型
 * @author jiangwei
 * @date 2021/12/19 12:41 PM
 * @since 1.0
 */
public class Notice<C> {


    /**
     * 关联的单个节点信息
     */
    private final EventNode<C> node;

    public Notice(Subscription subscription) {
        node = new MessageNode<>(subscription.getSubscribeId(), subscription.getPath());
    }

    /**
     * 取消当前Node上的监听
     *
     * @param listenerId node上监听的id
     */
    public synchronized void releaseListener(Long listenerId) {
        if (node != null) {
            node.release(listenerId);
        }
    }

    /**
     * 当发布者向消息总线发布数据，某个Topic订阅该数据时，触发这个事件
     *
     * @param supplier 消息总线传递的数据
     * @see Topic#exchange(C)
     */
    public void notify(Supplier<C> supplier) {
        load(EmitEvent.class, supplier.get());
    }

    /**
     * 当Topic被消息总线丢弃时触发
     *
     * @see Topic#discard(Long)
     */
    public void disappear() {
        load(LiftEvent.class, null);
    }

    /**
     * 加载自定义目标事件
     *
     * @param eventType 事件类型
     * @param source    事件源数据
     */
    public void load(Class<? extends TopicEvent> eventType, C source) {
        node.update(new EventContext<>(source, eventType, this, node));
    }

    /**
     * 获取当前Topic关联的异步节点
     *
     * @return 单数据源Node数据
     */
    public Mono<EventNode<C>> getAsyncNode() {
        return Mono.just(node);
    }

    /**
     * 获取当前Topic关联的节点
     *
     * @return Node数据
     * @throws NullPointerException 找不到时抛出
     */
    public EventNode<C> getNode() {
        return node;
    }
}
