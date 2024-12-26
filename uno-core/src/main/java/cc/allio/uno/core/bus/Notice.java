package cc.allio.uno.core.bus;

import cc.allio.uno.core.bus.event.*;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/**
 * Topic-Node之间的通知者，一对一的关系
 *
 * @param <C> 通知者通知泛型
 * @author j.x
 * @since 1.0
 */
public class Notice<C> {

    /**
     * 关联的单个节点信息
     */
    private final Node<C> node;

    public Notice(Subscription subscription) {
        node = new ReactiveEventNode<>(subscription.getSubscribeId(), subscription.getPath());
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
     * 当发布者向事件总线发布数据，某个Topic订阅该数据时，触发这个事件
     *
     * @param supplier 事件总线传递的数据
     */
    public Mono<C> notify(Supplier<C> supplier) {
        return load(EmitEvent.class, supplier.get());
    }

    /**
     * 当Topic被事件总线丢弃时触发
     *
     * @see Topic#discard(Long)
     */
    public Mono<C> disappear() {
        return load(LiftEvent.class, null);
    }

    /**
     * 加载自定义目标事件
     *
     * @param eventType 事件类型
     * @param source    事件源数据
     */
    public Mono<C> load(Class<? extends BusEvent> eventType, C source) {
        return node.update(new Context<>(source, eventType, this, node));
    }

    /**
     * 获取当前Topic关联的异步节点
     *
     * @return 单数据源Node数据
     */
    public Mono<Node<C>> getAsyncNode() {
        return Mono.just(node);
    }

    /**
     * 获取当前Topic关联的节点
     *
     * @return Node数据
     * @throws NullPointerException 找不到时抛出
     */
    public Node<C> getNode() {
        return node;
    }
}
