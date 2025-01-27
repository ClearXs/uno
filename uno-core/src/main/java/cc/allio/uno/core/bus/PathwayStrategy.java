package cc.allio.uno.core.bus;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.exception.NotFoundException;
import com.google.common.collect.Lists;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.ArrayUtils;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <b>使某个字符串按照给定的模式把它进行转换，使得当前主题能够构建成某一个具体的路径</b>
 * <ul>
 *     <li>{@link SpacePathwayStrategy}如果是test -> /test</li>
 *     <li>{@link UnderscorePathwayStrategy}如果是par_chi -> /par/chi</li>
 *     <li>{@link DashPathwayStrategy}如果是par-chi -> /par/chi</li>
 *     <li>{@link DotPathwayStrategy}如果时par.chi -> /par/chi</li>
 * </ul>
 *
 * @author j.x
 * @since 1.2.0
 */
public interface PathwayStrategy {

    EmptyPathWayStrategy EMPTY = new EmptyPathWayStrategy();
    SpacePathwayStrategy SPACE = new SpacePathwayStrategy();
    DashPathwayStrategy DASH = new DashPathwayStrategy();
    UnderscorePathwayStrategy UNDERSCORE = new UnderscorePathwayStrategy();
    SlashPathwayStrategy SLASH = new SlashPathwayStrategy();
    DotPathwayStrategy DOT = new DotPathwayStrategy();

    List<PathwayStrategy> ALL_STRATEGIES = Lists.newArrayList(EMPTY, SPACE, DASH, UNDERSCORE, SLASH, DOT);
    Supplier<List<PathwayStrategy>> DEFER = () -> ALL_STRATEGIES;
    Flux<PathwayStrategy> STRATEGIES = Flux.defer(() -> Flux.fromIterable(ALL_STRATEGIES));

    /**
     * transform path to specific strategy.
     *
     * @param path the path
     * @return transform path
     */
    default String transform(String path) {
        return transform().apply(path);
    }

    /**
     * 路径转化抽象方法
     *
     * @return 接收转换前的主题字符串，返回转换后
     * @throws NotFoundException if not found strategy throwing.
     */
    default Function<String, String> transform() {
        return s -> {
            if (match(s)) {
                return s;
            }
            return PathwayStrategy.require(s).transformTo(this).apply(s);
        };
    }

    /**
     * make as path transform to specific strategy.
     *
     * @param strategy the specific strategy
     * @return function of path
     */
    default String transformTo(String path, PathwayStrategy strategy) {
        return transformTo(strategy).apply(path);
    }

    /**
     * make as path transform to specific strategy.
     *
     * @param strategy the specific strategy
     * @return function of path
     */
    default Function<String, String> transformTo(PathwayStrategy strategy) {
        return s -> {
            String[] split = segment(s);
            return strategy.combine(split);
        };
    }

    /**
     * 告诉主题字符串按照什么的规则来进行切分
     *
     * @return 提供某个切分规则
     */
    String rule();

    /**
     * base on strategy rule split the string to char array.
     *
     * @param s the string
     * @return
     */
    default String[] segment(@Nonnull String s) {
        return s.split(rule());
    }

    /**
     * base on strategy rule combine the string array to string.
     *
     * @param array the string array
     * @return from {@link #rule()} combine array.
     */
    default String combine(@Nonnull String[] array) {
        String[] newArray = clipDuplex(array);
        return String.join(rule(), deduplicate(newArray));
    }

    /**
     * base on {@link #rule()} deduplicate.
     * <p>
     * O(n) time complexity.
     *
     * @param array the array
     * @return deduplication array
     */
    default String[] deduplicate(String[] array) {
        int count = 0;
        List<String> list = new ArrayList<>();
        for (String v : array) {
            if (rule().equals(v)) {
                count++;
            } else {
                // have differed value.
                count = 0;
            }
            if (count <= 1) {
                list.add(v);
            }

        }
        return list.toArray(String[]::new);
    }

    /**
     * specific string is match.
     *
     * @return {@code true} if match
     */
    default boolean match(@Nonnull String s) {
        return s.contains(rule());
    }

    /**
     * if the array[0] match {@link #rule()}. clip it.
     *
     * @param array the array
     * @return after clip array
     */
    default String[] clipFirst(String[] array) {
        if (ArrayUtils.isEmpty(array)) {
            return array;
        }
        // if strategy == SLASH, the first element must be '/' as start.
        // like cc/allio ==> /cc/allio
        if (this == SLASH) {
            if (!rule().equals(array[0])) {
                String[] newArray = new String[array.length + 1];
                newArray[0] = StringPool.EMPTY;
                System.arraycopy(array, 0, newArray, 1, array.length);
                return newArray;
            }
        } else {
            // otherwise others strategy if start is rule, clip it.
            // like as .cc.allio ==> cc.allio
            if (rule().equals(array[0])) {
                String[] newArray = new String[array.length - 1];
                System.arraycopy(array, 1, newArray, 0, newArray.length);
                return newArray;
            }
        }
        return array;
    }

    /**
     * if the array[length - 1] match {@link #rule()}. clip it.
     * like as cc/allio/ ==> cc/allio
     *
     * @param array the array
     * @return after clip array
     */
    default String[] clipLast(String[] array) {
        if (ArrayUtils.isEmpty(array)) {
            return array;
        }
        int length = array.length;
        if (rule().equals(array[length - 1])) {
            String[] newArray = new String[array.length - 1];
            System.arraycopy(array, 0, newArray, 0, newArray.length);
            return newArray;
        }
        return array;
    }

    /**
     * default invoke {@link #clipFirst(String[])} and {@link #clipFirst(String[])}
     *
     * @param array the array
     * @return after clip array
     */
    default String[] clipDuplex(String[] array) {
        String[] newArray = array;
        // first clip
        newArray = clipFirst(newArray);
        // then clip last
        newArray = clipLast(newArray);
        return newArray;
    }

    /**
     * specific string require {@link PathwayStrategy}
     *
     * @param s the string
     * @return the {@link PathwayStrategy} instance if empty return {@link #SPACE}
     */
    static PathwayStrategy require(String s) {
        return ALL_STRATEGIES.stream()
                .filter(strategy -> strategy.match(s))
                .findFirst()
                .orElse(EMPTY);
    }

    /**
     * be directed against single words. like as 'cc', 'ccallio' etc.
     */
    class EmptyPathWayStrategy implements PathwayStrategy {

        @Override
        public String rule() {
            return StringPool.EMPTY;
        }

        @Override
        public String[] segment(@Nonnull String s) {
            return new String[]{s};
        }

        @Override
        public String combine(@Nonnull String[] array) {
            return array[0];
        }

        @Override
        public boolean match(@Nonnull String s) {
            return false;
        }

        @Override
        public Function<String, String> transform() {
            return s -> s;
        }
    }

    class SpacePathwayStrategy implements PathwayStrategy {

        SpacePathwayStrategy() {
        }

        @Override
        public String rule() {
            return StringPool.SPACE;
        }
    }

    /**
     * 以'-'为切分规则的路径策略
     */
    class DashPathwayStrategy implements PathwayStrategy {
        DashPathwayStrategy() {
        }

        @Override
        public String rule() {
            return StringPool.DASH;
        }
    }

    /**
     * 以'_'为切分规则的路径策略
     */
    class UnderscorePathwayStrategy implements PathwayStrategy {

        UnderscorePathwayStrategy() {
        }

        @Override
        public String rule() {
            return StringPool.UNDERSCORE;
        }
    }

    /**
     * 以'/'为切分规则的路径策略，传递什么就返回什么
     */
    class SlashPathwayStrategy implements PathwayStrategy {

        SlashPathwayStrategy() {
        }

        @Override
        public String rule() {
            return StringPool.SLASH;
        }
    }

    /**
     * 以'.'为切分路径
     */
    class DotPathwayStrategy implements PathwayStrategy {

        private DotPathwayStrategy() {
        }

        @Override
        public boolean match(@Nonnull String s) {
            return s.contains(StringPool.DOT) || s.contains(StringPool.ORIGIN_DOT);
        }

        @Override
        public String[] segment(@Nonnull String s) {
            String[] split = s.split(StringPool.ORIGIN_DOT);
            if (ArrayUtils.isEmpty(split)) {
                split = s.split(StringPool.DOT);
            }
            return split;
        }

        @Override
        public String rule() {
            return StringPool.ORIGIN_DOT;
        }
    }
}
