package cc.allio.uno.core.bus;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import cc.allio.uno.core.bus.event.*;
import com.google.common.collect.Maps;
import lombok.Getter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.FluxSink;

/**
 * 消息主题，订阅者与数据源之间的联系，他是一个桥接器
 *
 * @author j.x
 */
public class Topic<C extends EventContext> implements Comparable<Topic<C>> {

    // 主题路径
    @Getter
    private final String path;
    private final Map<Subscription, Node<C>> nodes;
    @Getter
    private final Sentinel<C> sentinel;

    // 数据流信号
    private FluxSink<C> sink;

    public Topic(String path, EventBus<C> eventBus) {
        // 使当前主题能够构建成某一个具体的路径，
        // 如果是test -> /test
        // 如果是par_chi -> /par/chi
        // 如果是par-chi -> /par/chi ...
        this.path = Topic.pathway(path);
        this.nodes = Maps.newConcurrentMap();
        this.sentinel = new Sentinel<>(this.path, eventBus);
    }

    /**
     * 添加订阅者
     *
     * @param subscription the subscription
     */
    public Mono<Node<C>> addSubscriber(Subscription subscription) {
        Node<C> node = nodes.computeIfAbsent(subscription, s -> new ReactiveNode<>(subscription.getId(), this));
        sentinel.attach(node);
        return Mono.just(node);
    }

    /**
     * 当上游向下游发送数据时，触发这个方法<br/>
     * 它通过{@link FluxSink#next(Object)}对下游进行传递。触发{@link reactor.core.publisher.Flux#doOnNext(Consumer)}
     *
     * @param context 事件总线上下文对象
     */
    public Flux<C> exchange(C context) {
        return exchange(() -> context);
    }

    /**
     * 当上游向下游发送数据时，触发这个方法<br/>
     *
     * @param supplier 消息主题数据
     * @return
     */
    public Flux<C> exchange(Supplier<C> supplier) {
        C context = supplier.get();
        return Flux.fromIterable(nodes.values())
                // trigger sentinel worker working (sequential is important)
                .doOnNext(n -> sentinel.trigger(context))
                .flatMap(n -> n.update(new Context<>(context, EmitEvent.class, n)));

    }

    /**
     * 向事件总线中生成该主题，接受数据发射源，由其内部转发这个消息数据至在这个主题下的所有节点信息
     *
     * @param sink 数据流信号
     */
    public void generate(FluxSink<C> sink) {
        this.sink = sink;
    }

    /**
     * 由调用方生成数据，从sink中向数据下游发送。
     *
     * @param context 事件总线上下文对象
     */
    public void emmit(C context) {
        if (sink != null) {
            sink.next(context);
        }
    }

    /**
     * 当事件总线丢弃当前主题时触发这个事件
     *
     * @param subscribeId 监听id
     */
    public Mono<Void> discard(Long subscribeId) {
        return findNode(subscribeId)
                // subtraction sentinel subscriber
                .doOnNext(node -> sentinel.detach(subscribeId))
                .flatMap(node -> node.update(new Context<>(null, LiftEvent.class, node)))
                // remove node from nodes cache
                .thenEmpty(Mono.fromRunnable(() -> nodes.remove(Subscription.of(subscribeId))));
    }

    /**
     * 丢弃所有Node监听消息
     */
    public Mono<Void> discardAll() {
        return Flux.fromIterable(nodes.values())
                .doOnNext(node -> sentinel.detach(node.getSubscribeId()))
                .flatMap(node -> node.update(new Context<>(null, LiftEvent.class, node)))
                // remove all nodes in cache
                .thenEmpty(Mono.fromRunnable(nodes::clear));
    }

    /**
     * get subscriber {@link Node} by subscribe id
     *
     * @param subscribeId the subscribe id
     * @return
     */
    public Mono<Node<C>> findNode(Long subscribeId) {
        Node<C> node = nodes.get(Subscription.of(subscribeId));
        return Mono.justOrEmpty(node);
    }

    /**
     * get subscribe current topic all node
     *
     * @return Node节点数据
     */
    public Flux<Node<C>> getNode() {
        return Flux.fromIterable(nodes.values());
    }

    /**
     * get subscriber size
     *
     * @return
     */
    public int size() {
        return nodes.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Topic<C> that = (Topic<C>) o;
        return Objects.equals(getPath(), that.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPath());
    }

    /**
     * 主题路径化
     *
     * @return 路径策略实例
     */
    public static String pathway(String topic) {
        return PathwayStrategy.DEFER
                .get()
                .stream()
                .filter(pathwayStrategy -> topic.contains(pathwayStrategy.segment().get()))
                .findFirst()
                .orElse(PathwayStrategy.BLANK_PATHWAY_STRATEGY)
                .transform()
                .apply(topic);
    }

    @Override
    public int compareTo(Topic<C> that) {
        return Integer.compare(getPath().hashCode(), that.hashCode());
    }

}
