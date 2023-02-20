package cc.allio.uno.core.bus;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import cc.allio.uno.core.bus.event.EventNode;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.StringUtils;
import com.google.common.collect.Lists;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

/**
 * 消息主题，订阅者与数据源之间的联系，他是一个桥接器
 *
 * @author jw
 * @date 2021/12/15 15:44
 */
public interface Topic<C> {

    /**
     * 获取当前Topic的路径，它会被{@link PathwayStrategy}进行路径化操作
     *
     * @return 路径的字符串
     */
    String getPath();

    /**
     * 当上游向下游发送数据时，触发这个方法<br/>
     *
     * @param supplier 消息主题数据
     */
    void exchange(Supplier<C> supplier);

    /**
     * 当上游向下游发送数据时，触发这个方法<br/>
     * 它通过{@link FluxSink#next(Object)}对下游进行传递。触发{@link reactor.core.publisher.Flux#doOnNext(Consumer)}
     *
     * @param context 消息总线上下文对象
     */
    default void exchange(C context) {
        exchange(() -> context);
    }

    /**
     * 向消息总线中生成该主题，接受数据发射源，由其内部转发这个消息数据至在这个主题下的所有节点信息
     *
     * @param sink 数据流信号
     */
    void generate(FluxSink<C> sink);

    /**
     * 由调用方生成数据，从sink中向数据下游发送。
     *
     * @param context 消息总线上下文对象
     */
    void emmit(C context);

    /**
     * 当消息总线丢弃当前主题时触发这个事件
     *
     * @param listenerId 监听id
     */
    void discard(Long listenerId);

    /**
     * 丢弃所有Node监听消息
     */
    void discardAll();

    /**
     * 从当前主题中找到某个的节点
     *
     * @return Node节点数据
     */
    Mono<EventNode<C>> findNode();

    /**
     * 主题路径化
     *
     * @return 路径策略实例
     */
    static String topicPathway(String topic) {
        return PathwayStrategy.STRATEGIES.stream()
                .filter(pathwayStrategy -> topic.contains(pathwayStrategy.segment().get()))
                .findFirst()
                .orElse(new BlankPathwayStrategy())
                .transform()
                .apply(topic);
    }

    /**
     * <b>使某个字符串按照给定的模式把它进行转换，使得当前主题能够构建成某一个具体的路径</b>
     * <ul>
     *     <li>{@link BlankPathwayStrategy}如果是test -> /test</li>
     *     <li>{@link UnderscorePathwayStrategy}如果是par_chi -> /par/chi</li>
     *     <li>{@link DashPathwayStrategy}如果是par-chi -> /par/chi</li>
     * </ul>
     */
    interface PathwayStrategy {

        /**
         * 路径策略器
         */
        List<PathwayStrategy> STRATEGIES = Lists.newArrayList(
                new DashPathwayStrategy(),
                new UnderscorePathwayStrategy(),
                new SlashPathwayStrategy());

        /**
         * 路径转化抽象方法
         *
         * @return 接收转换前的主题字符串，返回转换后
         */
        Function<String, String> transform();

        /**
         * 告诉主题字符串按照什么的规则来进行切分
         *
         * @return 提供某个切分规则
         */
        Supplier<String> segment();
    }

    /**
     * 不带任何切分的路径转换策略
     */
    class BlankPathwayStrategy implements PathwayStrategy {

        static final String BLANK = "blank";

        /**
         * 空字符串计数器，记录总共有多少""路径主题
         */
        final AtomicInteger blankCounter = new AtomicInteger();

        @Override
        public Function<String, String> transform() {
            return s -> {
                if (StringUtils.isEmpty(s)) {
                    return BLANK + StringPool.DOLLAR + blankCounter.getAndIncrement();
                } else {
                    return StringPool.SLASH + s;
                }
            };
        }

        @Override
        public Supplier<String> segment() {
            return () -> "";
        }
    }

    /**
     * 以'-'为切分规则的路径策略
     */
    class DashPathwayStrategy implements PathwayStrategy {

        @Override
        public Function<String, String> transform() {
            return s -> StringPool.SLASH + String.join(StringPool.SLASH, s.split(StringPool.DASH));
        }

        @Override
        public Supplier<String> segment() {
            return () -> StringPool.DASH;
        }
    }

    /**
     * 以'_'为切分规则的路径策略
     */
    class UnderscorePathwayStrategy implements PathwayStrategy {

        @Override
        public Function<String, String> transform() {
            return s -> StringPool.SLASH + String.join(StringPool.UNDERSCORE, s.split(StringPool.UNDERSCORE));
        }

        @Override
        public Supplier<String> segment() {
            return () -> StringPool.UNDERSCORE;
        }
    }

    /**
     * 以'/'为切分规则的路径策略，传递什么就返回什么
     */
    class SlashPathwayStrategy implements PathwayStrategy {

        @Override
        public Function<String, String> transform() {
            return s -> s;
        }

        @Override
        public Supplier<String> segment() {
            return () -> StringPool.SLASH;
        }
    }
}
