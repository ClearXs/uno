package cc.allio.uno.core.util.comparator;

import cc.allio.uno.core.function.lambda.MethodBiFunction;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;

/**
 * {@link java.util.Comparator}常用类型比较器
 *
 * @author j.x
 * @since 1.1.7
 */
public final class Comparators extends org.springframework.util.comparator.Comparators {

    private static final Comparator<String> STRING_COMPARATOR = new StringComparator();
    private static final Comparator<Integer> INTEGER_COMPARATOR = new IntegerComparator();
    private static final Comparator<Long> LONG_COMPARATOR = new LongComparator();
    private static final Comparator<Float> FLOAT_COMPARATOR = new FloatComparator();
    private static final Comparator<Double> DOUBLE_COMPARATOR = new DoubleComparator();
    private static final Comparator<Boolean> BOOLEAN_COMPARATOR = new BooleanComparator();
    private static final Comparator<Short> SHORT_COMPARATOR = new ShortComparator();
    private static final Comparator<BigDecimal> BIG_DECIMAL_COMPARATOR = new BigDecimalComparator();
    private static final Comparator<Object> OBJECT_COMPARATOR = new ObjectComparator();

    private Comparators() {
    }

    /**
     * as string comparator
     */
    public static Comparator<String> strings() {
        return STRING_COMPARATOR;
    }

    /**
     * as integer comparator
     */
    public static Comparator<Integer> integers() {
        return INTEGER_COMPARATOR;
    }

    /**
     * as long comparator
     */
    public static Comparator<Long> longs() {
        return LONG_COMPARATOR;
    }

    public static Comparator<Double> doubles() {
        return DOUBLE_COMPARATOR;
    }

    /**
     * as float comparator
     */
    public static Comparator<Float> floats() {
        return FLOAT_COMPARATOR;
    }

    /**
     * as boolean comparator
     */
    public static Comparator<Boolean> booleans() {
        return BOOLEAN_COMPARATOR;
    }

    /**
     * as short comparator
     */
    public static Comparator<Short> shorts() {
        return SHORT_COMPARATOR;
    }

    /**
     * as big decimal comparator
     */
    public static Comparator<BigDecimal> bigDecimals() {
        return BIG_DECIMAL_COMPARATOR;
    }

    /**
     * as object comparator
     */
    public static Comparator<Object> objects() {
        return OBJECT_COMPARATOR;
    }

    /**
     * as collection comparator
     */
    public static <T> Comparator<Collection<T>> collections() {
        return new CollectionComparator<>();
    }

    /**
     * as collection comparator
     *
     * @see CollectionComparator#CollectionComparator(MethodBiFunction)
     */
    public static <T> Comparator<Collection<T>> collections(MethodBiFunction<T, Collection<T>, T> finder) {
        return new CollectionComparator<>(finder);
    }
}
