package cc.allio.uno.core.util.type;

import com.google.common.collect.Sets;

import java.math.BigDecimal;
import java.util.*;

/**
 * 类型公共方法集
 *
 * @author jiangwei
 * @date 2022/12/3 17:25
 * @since 1.1.2
 */
public class Types {

    protected Types() {
    }

    public static final Class<Long> LONG = Long.TYPE;
    public static final Class<Boolean> BOOLEAN = Boolean.TYPE;
    public static final Class<Byte> BYTE = Byte.TYPE;
    public static final Class<Date> DATE = Date.class;
    public static final Class<Double> DOUBLE = Double.TYPE;
    public static final Class<Enum> ENUM = Enum.class;
    public static final Class<Float> FLOAT = Float.TYPE;
    public static final Class<Integer> INTEGER = Integer.TYPE;
    public static final Class<Short> SHORT = Short.TYPE;
    public static final Class<String> STRING = String.class;
    public static final Class<BigDecimal> BIG_DECIMAL = BigDecimal.class;

    private static final Set<Class<?>> SIMPLE_TYPES = Sets.newHashSet(LONG, BOOLEAN, BYTE, DATE, DOUBLE, ENUM, FLOAT, INTEGER, SHORT, STRING);

    /**
     * 判断放给定的class是否是{@link Long}
     *
     * @param clazz class对象实例
     * @return true Long false not Long
     */
    public static boolean isLong(Class<?> clazz) {
        return Long.class.isAssignableFrom(clazz);
    }

    /**
     * 判断放给定的class是否是{@link Boolean}
     *
     * @param clazz class对象实例
     * @return true Boolean false not Boolean
     */
    public static boolean isBoolean(Class<?> clazz) {
        return Boolean.class.isAssignableFrom(clazz);
    }

    /**
     * 判断放给定的class是否是{@link Byte}
     *
     * @param clazz class对象实例
     * @return true Byte false not Byte
     */
    public static boolean isByte(Class<?> clazz) {
        return Byte.class.isAssignableFrom(clazz);
    }

    /**
     * 判断放给定的class是否是{@link Date}
     *
     * @param clazz class对象实例
     * @return true Date false not Date
     */
    public static boolean isDate(Class<?> clazz) {
        return Date.class.isAssignableFrom(clazz);
    }

    /**
     * 判断放给定的class是否是{@link Double}
     *
     * @param clazz class对象实例
     * @return true Double false not Double
     */
    public static boolean isDouble(Class<?> clazz) {
        return Double.class.isAssignableFrom(clazz);
    }

    /**
     * 判断放给定的class是否是{@link Enum}
     *
     * @param clazz class对象实例
     * @return true Enum false not Enum
     */
    public static boolean isEnum(Class<?> clazz) {
        return Enum.class.isAssignableFrom(clazz);
    }

    /**
     * 判断放给定的class是否是{@link Float}
     *
     * @param clazz class对象实例
     * @return true Float false not Float
     */
    public static boolean isFloat(Class<?> clazz) {
        return Float.class.isAssignableFrom(clazz);
    }

    /**
     * 判断放给定的class是否是{@link Integer}
     *
     * @param clazz class对象实例
     * @return true Integer false not Integer
     */
    public static boolean isInteger(Class<?> clazz) {
        return Integer.class.isAssignableFrom(clazz);
    }

    /**
     * 判断放给定的class是否是{@link Short}
     *
     * @param clazz class对象实例
     * @return true Short false not Short
     */
    public static boolean isShort(Class<?> clazz) {
        return Short.class.isAssignableFrom(clazz);
    }

    /**
     * 判断放给定的class是否是{@link String}
     *
     * @param clazz class对象实例
     * @return true String false not String
     */
    public static boolean isString(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);
    }



    /**
     * 判断给定的class是否是{@link java.util.Map}
     *
     * @param clazz class对象实例
     * @return true Map false not map
     */
    public static boolean isMap(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    /**
     * 判断给定的class是否是{@link java.util.List}
     *
     * @param clazz class对象实例
     * @return true List false not list
     */
    public static boolean isList(Class<?> clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    /**
     * 判断给定的class是否是Bean
     *
     * @param clazz class对象实例
     * @return true false
     */
    public static boolean isBean(Class<?> clazz) {
        return !SIMPLE_TYPES.contains(clazz) && !isList(clazz) && !isMap(clazz);
    }

    /**
     * 判断对象是否为简单类型
     *
     * @param clazz class对象实例
     * @return true false
     */
    public static boolean isSimpleType(Class<?> clazz) {
        return SIMPLE_TYPES.contains(clazz);
    }

    /**
     * 判断数字是否为负数
     *
     * @param value 目标对象
     * @return -1：负数，0：零，1：正数
     * @throws NullPointerException 无法找到指定的数据类型时抛出异常
     */
    public static int signum(Object value) {
        TypeOperator typeOperator = TypeOperatorFactory.translator(value.getClass());
        return typeOperator.signum(value);
    }

    /**
     * 返回给定对象字符串数据
     *
     * @param value 给定对象
     * @return String
     */
    public static String toString(Object value) {
        return Optional.ofNullable(TypeOperatorFactory.translator(value.getClass()))
                .map(operator -> operator.fromString(value))
                .orElse(value.toString());
    }
}
