package cc.allio.uno.core.bus;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

import cc.allio.uno.core.path.Forest;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 消息主题
 *
 * @author j.x
 */
@Slf4j
public final class Topics<C extends EventContext> extends ConcurrentSkipListMap<String, Topic<C>> {

    private final Forest<Topic<C>> forest = Forest.createRoot();

    /**
     * 创建一个InnerTopic
     *
     * @param subscription 订阅信息
     * @return Topic实例
     * @throws NullPointerException path为空时抛出
     */
    public Mono<Topic<C>> link(Subscription subscription, EventBus<C> eventBus) {
        synchronized (this) {
            Topic<C> topic = computeIfAbsent(
                    subscription.getPath(),
                    key -> {
                        log.debug("Thread: {} link topic path: {}", Thread.currentThread().getName(), subscription.getPath());
                        return new Topic<>(subscription.getPath(), eventBus);
                    });
            return Mono.defer(
                    () -> {
                        forest.append(topic.getPath()).subscribe(topic);
                        Flux.push(topic::generate).flatMap(topic::exchange).subscribe();
                        return Mono.just(topic);
                    });
        }
    }

    /**
     * 事件总线解除这个主题
     *
     * @param topic 主题路径
     * @return 是否解除成功
     * @throws NullPointerException topic为空时抛出
     */
    public Mono<Boolean> unlink(String topic) {
        return lookup(topic)
                .flatMap(Topic::discardAll)
                .then(
                        Mono.defer(() -> {
                            forest.clean();
                            return Mono.just(Boolean.TRUE);
                        }));
    }

    /**
     * 根据路径查找Topic对象
     *
     * @param topic 主题路径
     * @return 主题实例
     * @throws NullPointerException topic为空时抛出
     */
    public Flux<Topic<C>> lookup(String topic) {
        return forest.findPath(Topic.pathway(topic))
                .flatMap(f ->
                        // 获取当前结点子树并把其平展为保存的数据
                        f.getAllSubscriber()
                                .flatMap(af -> Flux.fromIterable(af.getSubscribers()))
                                // 当前已经处于叶子结点，无子节点路径，直接返回当前订阅信息
                                .switchIfEmpty(Flux.fromIterable(f.getSubscribers()))
                )
                // Topic对象去重
                .collect(Collectors.toSet())
                .flatMapMany(Flux::fromIterable);
    }

    /**
     * 删除当前主题缓存中的所有对象
     */
    public void deleteAll() {
        forest.unsubscribeAll();
    }

    @Override
    public String toString() {
        return forest.toString();
    }
}
