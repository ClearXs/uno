package cc.allio.uno.core.bus;

import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

import cc.allio.uno.core.path.Forest;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 消息主题
 *
 * @author jw
 * @date 2021/12/16 10:50
 */
@Slf4j
public final class Topics<C> extends ConcurrentSkipListMap<String, Topic<C>> {

    private final Forest<Topic<C>> forest = Forest.createRoot();

    /**
     * 创建一个InnerTopic
     *
     * @param subscription 订阅信息
     * @return Topic实例
     * @throws NullPointerException path为空时抛出
     */
    public synchronized Mono<Topic<C>> link(Subscription subscription) {
        Topic<C> maybeTopic = computeIfAbsent(
                subscription.getPath(),
                key -> {
                    log.info("Thread: {} link topic path: {}", Thread.currentThread().getName(), subscription.getPath());
                    return new NoticeTopic<>(subscription);
                });
        return Mono.justOrEmpty(Optional.of(maybeTopic))
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess(topic -> {
                            forest.append(topic.getPath()).subscribe(topic);
                            Flux.create(topic::generate)
                                    .doOnNext(topic::exchange)
                                    .subscribe();
                        }
                );
    }

    /**
     * 消息总线解除这个主题
     *
     * @param topic 主题路径
     * @return 是否解除成功
     * @throws NullPointerException topic为空时抛出
     */
    public Mono<Boolean> unlink(String topic) {
        return lookup(topic)
                .doOnNext(topicData -> {
                    log.info("Thread {} unlink topic path: {}", Thread.currentThread().getName(), topic);
                    topicData.discardAll();
                })
                .then(Mono.defer(() -> {
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
        return forest.findPath(Topic.topicPathway(topic))
                .flatMap(f ->
                        // 获取当前结点子树并把其平展为保存的数据
                        f.getAllSubscriber()
                                .flatMap(af -> Flux.fromIterable(af.getSubscribers()))
                                // 当前已经处于叶子结点，无字节路径，直接返回当前订阅信息
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

}
