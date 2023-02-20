package cc.allio.uno.core.bus;

import cc.allio.uno.core.bus.event.EventNode;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/**
 * 基于事件通知机制实现的Topic
 *
 * @author jiangwei
 * @date 2022/1/26 11:37
 * @since 1.0
 */
public class NoticeTopic<C> extends AbstractTopic<C> implements Comparable<Topic<C>> {

    /**
     * 维护Topic-Node之间关系
     */
    private Notice<C> notice;

    public NoticeTopic(Subscription subscription) {
        super(subscription.getPath());
        this.notice = new Notice<>(subscription);
    }

    @Override
    public void exchange(Supplier<C> supplier) {
        notice.notify(supplier);
    }

    @Override
    public Mono<EventNode<C>> findNode() {
        return notice.getAsyncNode();
    }

    @Override
    public void discard(Long listener) {
        if (notice != null) {
            notice.releaseListener(listener);
        }
    }

    @Override
    public void discardAll() {
        notice.disappear();
        // help gc
        notice = null;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int compareTo(Topic that) {
        return Integer.compare(getPath().hashCode(), that.hashCode());
    }
}
