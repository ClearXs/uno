package cc.allio.uno.core.bus;

import cc.allio.uno.core.StringPool;
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
 *     <li>{@link SpacePathway}如果是test -> /test</li>
 *     <li>{@link UnderscorePathway}如果是par_chi -> /par/chi</li>
 *     <li>{@link DashPathway}如果是par-chi -> /par/chi</li>
 *     <li>{@link DotPathway}如果时par.chi -> /par/chi</li>
 * </ul>
 * <p>
 * note:
 * forbid use {@code StringPool#EMPTY} as pattern.
 *
 * @author j.x
 * @since 1.2.0
 */
public interface Pathway {

    EmptyPathWay EMPTY = new EmptyPathWay();
    SpacePathway SPACE = new SpacePathway();
    DashPathway DASH = new DashPathway();
    UnderscorePathway UNDERSCORE = new UnderscorePathway();
    SlashPathway SLASH = new SlashPathway();
    DotPathway DOT = new DotPathway();

    List<Pathway> ALL = Lists.newArrayList(EMPTY, SPACE, DASH, UNDERSCORE, SLASH, DOT);
    Supplier<List<Pathway>> DEFER = () -> ALL;
    Flux<Pathway> STRATEGIES = Flux.defer(() -> Flux.fromIterable(ALL));

    /**
     * @see #transformTo(boolean, boolean, String, Pathway)
     */
    default String transform(String path) {
        return transform(false, path);
    }

    /**
     * @see #transformTo(boolean, boolean, String, Pathway)
     */
    default String transform(boolean headless, String path) {
        return transform(headless, true, path);
    }

    /**
     * @see #transformTo(boolean, boolean, String, Pathway)
     */
    default String transform(boolean headless, boolean tailless, String path) {
        return transform(headless, tailless).apply(path);
    }

    /**
     * @see #transformTo(boolean, boolean, String, Pathway)
     */
    default Function<String, String> transform() {
        return transform(false);
    }

    /**
     * @see #transformTo(boolean, boolean, String, Pathway)
     */
    default Function<String, String> transform(boolean headless) {
        return transform(headless, true);
    }

    /**
     * @see #transformTo(boolean, boolean, String, Pathway)
     */
    default Function<String, String> transform(boolean headless, boolean tailless) {
        return s -> Pathway.require(s).transformTo(headless, tailless, this).apply(s);
    }

    /**
     * @see #transformTo(boolean, boolean, String, Pathway)
     */
    default String transformTo(String path, Pathway strategy) {
        return transformTo(false, path, strategy);
    }

    /**
     * @see #transformTo(boolean, boolean, String, Pathway)
     */
    default String transformTo(boolean headless, String path, Pathway strategy) {
        return transformTo(headless, true, path, strategy);
    }

    /**
     * @see #transformTo(boolean, boolean, String, Pathway)
     */
    default String transformTo(boolean headless, boolean tailless, String path, Pathway strategy) {
        return transformTo(headless, tailless, strategy).apply(path);
    }

    /**
     * @see #transformTo(boolean, boolean, String, Pathway)
     */
    default Function<String, String> transformTo(Pathway strategy) {
        return transformTo(false, true, strategy);
    }

    /**
     * make as path transform to specific strategy.
     *
     * @param headless if headless is true, the first element must be rule.
     * @param tailless if tailless is true, the last element must be rule.
     * @param strategy the specific strategy
     * @return function of path
     */
    default Function<String, String> transformTo(boolean headless, boolean tailless, Pathway strategy) {
        return s -> {
            String[] deduplicate = deduplicate(rule(), s.split(StringPool.EMPTY));
            return strategy.combine(headless, tailless, rule(), deduplicate);
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
     * @param pattern the match pattern
     * @param array   the string array
     * @return from {@link #rule()} combine array.
     */
    default String combine(boolean headless, boolean tailless, @Nonnull String pattern, @Nonnull String[] array) {
        String[] newArray = clipDuplex(headless, tailless, pattern, array);
        String[] result = new String[newArray.length];
        for (int i = 0; i < newArray.length; i++) {
            String v = newArray[i];
            if (pattern.equals(v)) {
                result[i] = rule();
            } else {
                result[i] = v;
            }
        }
        return String.join(StringPool.EMPTY, result);
    }

    /**
     * base on {@link #rule()} deduplicate.
     * <p>
     * O(n) time complexity.
     *
     * @param pattern the match pattern
     * @param array   the array
     * @return deduplication array
     */
    default String[] deduplicate(String pattern, String[] array) {
        int count = 0;
        List<String> list = new ArrayList<>();
        for (String v : array) {
            if (pattern.equals(v)) {
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
     * @param headless if headless is true, the first element must be rule.
     * @param pattern  the match pattern
     * @param array    the array
     * @return after clip array
     */
    default String[] clipFirst(boolean headless, String pattern, String[] array) {
        if (ArrayUtils.isEmpty(array)) {
            return array;
        }
        // if strategy == SLASH, the first element must be '/' as start.
        // like cc/allio ==> /cc/allio
        if (this == SLASH) {
            if (!pattern.equals(array[0]) && !headless) {
                String[] newArray = new String[array.length + 1];
                newArray[0] = pattern;
                System.arraycopy(array, 0, newArray, 1, array.length);
                return newArray;
            }
        } else {
            // otherwise others strategy if start is rule, clip it.
            // like as .cc.allio ==> cc.allio
            if (pattern.equals(array[0])) {
                String[] newArray = new String[array.length - 1];
                System.arraycopy(array, 1, newArray, 0, newArray.length);
                return newArray;
            }
        }
        return array;
    }

    /**
     * if the array[length - 1] match {@link #rule()}. clip it.
     * like as cc/allio/ ==> cc/allio or cc/allio ==> cc/allio/ determine tailless parameter.
     *
     * @param tailless if tailless is true, the last element must be rule.
     * @param pattern  the match pattern
     * @param array    the array
     * @return after clip array
     */
    default String[] clipLast(boolean tailless, String pattern, String[] array) {
        if (ArrayUtils.isEmpty(array)) {
            return array;
        }
        int length = array.length;
        String lastElement = array[length - 1];
        // clip last element
        if (tailless && pattern.equals(lastElement)) {
            String[] newArray = new String[array.length - 1];
            System.arraycopy(array, 0, newArray, 0, newArray.length);
            return newArray;
            // if tailless is false, the last element must be rule.
        } else if (!tailless && !pattern.equals(lastElement)) {
            String[] newArray = new String[array.length + 1];
            System.arraycopy(array, 0, newArray, 0, array.length);
            newArray[array.length] = pattern;
            return newArray;
        }
        return array;
    }

    /**
     * default invoke {@link #clipFirst(boolean, String, String[])} and {@link #clipFirst(boolean, String, String[])}
     *
     * @param pattern the match pattern
     * @param array   the array
     * @return after clip array
     */
    default String[] clipDuplex(boolean headless, boolean tailless, String pattern, String[] array) {
        String[] newArray = array;
        // first clip
        newArray = clipFirst(headless, pattern, newArray);
        // then clip last
        newArray = clipLast(tailless, pattern, newArray);
        return newArray;
    }

    /**
     * specific string require {@link Pathway}
     *
     * @param s the string
     * @return the {@link Pathway} instance if empty return {@link #SPACE}
     */
    static Pathway require(String s) {
        return ALL.stream()
                .filter(strategy -> strategy.match(s))
                .findFirst()
                .orElse(EMPTY);
    }

    /**
     * be directed against single words. like as 'cc', 'ccallio' etc.
     */
    class EmptyPathWay implements Pathway {

        @Override
        public String rule() {
            return StringPool.EMPTY;
        }

        @Override
        public String[] segment(@Nonnull String s) {
            return new String[]{s};
        }

        @Override
        public String combine(boolean headless, boolean tailless, @Nonnull String pattern, @Nonnull String[] array) {
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

    class SpacePathway implements Pathway {

        SpacePathway() {
        }

        @Override
        public String rule() {
            return StringPool.SPACE;
        }
    }

    /**
     * 以'-'为切分规则的路径策略
     */
    class DashPathway implements Pathway {
        DashPathway() {
        }

        @Override
        public String rule() {
            return StringPool.DASH;
        }
    }

    /**
     * 以'_'为切分规则的路径策略
     */
    class UnderscorePathway implements Pathway {

        UnderscorePathway() {
        }

        @Override
        public String rule() {
            return StringPool.UNDERSCORE;
        }
    }

    /**
     * 以'/'为切分规则的路径策略，传递什么就返回什么
     */
    class SlashPathway implements Pathway {

        SlashPathway() {
        }

        @Override
        public String rule() {
            return StringPool.SLASH;
        }
    }

    /**
     * 以'.'为切分路径
     */
    class DotPathway implements Pathway {

        private DotPathway() {
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
