package cc.allio.uno.core.bus;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.StringUtils;
import com.google.common.collect.Lists;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <b>使某个字符串按照给定的模式把它进行转换，使得当前主题能够构建成某一个具体的路径</b>
 * <ul>
 *     <li>{@link BlankPathwayStrategy}如果是test -> /test</li>
 *     <li>{@link UnderscorePathwayStrategy}如果是par_chi -> /par/chi</li>
 *     <li>{@link DashPathwayStrategy}如果是par-chi -> /par/chi</li>
 *     <li>{@link DotPathwayStrategy}如果时par.chi -> /par/chi</li>
 * </ul>
 *
 * @author j.x
 * @since 1.2.0
 */
public interface PathwayStrategy {

    BlankPathwayStrategy BLANK_PATHWAY_STRATEGY = new BlankPathwayStrategy();
    DashPathwayStrategy DASH_PATHWAY_STRATEGY = new DashPathwayStrategy();
    UnderscorePathwayStrategy UNDERSCORE_PATHWAY_STRATEGY = new UnderscorePathwayStrategy();
    SlashPathwayStrategy SLASH_PATHWAY_STRATEGY = new SlashPathwayStrategy();
    DotPathwayStrategy DOT_PATHWAY_STRATEGY = new DotPathwayStrategy();

    Supplier<List<PathwayStrategy>> DEFER =
            () -> Lists.newArrayList(
                    BLANK_PATHWAY_STRATEGY,
                    DASH_PATHWAY_STRATEGY,
                    UNDERSCORE_PATHWAY_STRATEGY,
                    SLASH_PATHWAY_STRATEGY,
                    DOT_PATHWAY_STRATEGY
            );

    /**
     * 路径策略器
     */
    Flux<PathwayStrategy> STRATEGIES =
            Flux.defer(
                    () -> Flux.just(
                            BLANK_PATHWAY_STRATEGY,
                            DASH_PATHWAY_STRATEGY,
                            UNDERSCORE_PATHWAY_STRATEGY,
                            SLASH_PATHWAY_STRATEGY,
                            DOT_PATHWAY_STRATEGY
                    )
            );

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

    /**
     * 公用转换实现，目标转换
     *
     * @param origin 原始路径信息
     * @return 转换完成
     */
    default String staticTransform(String origin) {
        String split = segment().get();
        String pathway = origin;
        // 路径头增加'/'
        if (!pathway.startsWith(StringPool.SLASH)) {
            pathway = StringPool.SLASH + pathway;
        }
        return String.join(StringPool.SLASH, pathway.split(split));
    }


    /**
     * 不带任何切分的路径转换策略
     */
    class BlankPathwayStrategy implements PathwayStrategy {

        BlankPathwayStrategy() {
        }

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
        DashPathwayStrategy() {
        }

        @Override
        public Function<String, String> transform() {
            return this::staticTransform;
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

        UnderscorePathwayStrategy() {
        }

        @Override
        public Function<String, String> transform() {
            return this::staticTransform;
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
        SlashPathwayStrategy() {
        }

        @Override
        public Function<String, String> transform() {
            return s -> s;
        }

        @Override
        public Supplier<String> segment() {
            return () -> StringPool.SLASH;
        }
    }

    /**
     * 以'.'为切分路径
     */
    class DotPathwayStrategy implements PathwayStrategy {


        private DotPathwayStrategy() {
        }

        @Override
        public Function<String, String> transform() {
            return this::staticTransform;
        }

        @Override
        public Supplier<String> segment() {
            return () -> StringPool.ORIGIN_DOT;
        }
    }
}
