package cc.allio.uno.core.bus;

import cc.allio.uno.core.bus.event.Node;
import com.google.common.collect.Maps;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.function.Supplier;

/**
 * 基于事件通知机制实现的Topic
 *
 * @author j.x
 * @since 1.0
 */
public class NoticeTopic<C> extends AbstractTopic<C> implements Comparable<Topic<C>> {

    /**
     * 维护Topic-Node之间关系
     */
    private final Map<Subscription, Notice<C>> notice;

    public NoticeTopic(String path) {
        super(path);
        this.notice = Maps.newConcurrentMap();
    }

    @Override
    public synchronized Mono<Node<C>> addSubscriber(Subscription subscription) {
        return notice.computeIfAbsent(subscription, s -> new Notice<>(subscription)).getAsyncNode();
    }

    @Override
    public Flux<C> exchange(Supplier<C> supplier) {
        return Flux.fromIterable(notice.values())
                .flatMap(n -> n.notify(supplier));
    }

    @Override
    public Flux<Node<C>> findNode() {
        return Flux.fromIterable(notice.values())
                .flatMap(Notice::getAsyncNode);
    }

    @Override
    public synchronized void discard(Long listener) {
        for (Notice<C> n : notice.values()) {
            n.releaseListener(listener);
        }
    }

    @Override
    public synchronized void discardAll() {
        notice.forEach((k, v) -> v.disappear().subscribe());
        // help gc
        notice.clear();
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
