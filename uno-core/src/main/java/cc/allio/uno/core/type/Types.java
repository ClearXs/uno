package cc.allio.uno.core.type;

import cc.allio.uno.core.StringPool;
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

    public static final Long LONG_EMPTY = 0L;
    public static final Integer INTEGER_EMPTY = 0;
    public static final Byte BYTE_EMPTY = 0;
    public static final Double DOUBLE_EMPTY = 0d;
    public static final Float FLOAT_EMPTY = 0f;
    public static final Short SHORT_EMPTY = 0;
    public static final String STRING_EMPTY = StringPool.EMPTY;


    public static final Class<Long> LONG = Long.class;
    public static final Class<Boolean> BOOLEAN = Boolean.class;
    public static final Class<Character> CHARACTER = Character.class;
    public static final Class<Byte> BYTE = Byte.class;
    public static final Class<Date> DATE = Date.class;
    public static final Class<Double> DOUBLE = Double.class;
    public static final Class<Enum> ENUM = Enum.class;
    public static final Class<Float> FLOAT = Float.class;
    public static final Class<Integer> INTEGER = Integer.class;
    public static final Class<Short> SHORT = Short.class;
    public static final Class<String> STRING = String.class;
    public static final Class<BigDecimal> BIG_DECIMAL = BigDecimal.class;
    public static final Class<List> LIST = List.class;
    public static final Class<Set> SET = Set.class;
    public static final Class<Map> MAP = Map.class;
    public static final Class<Stack> STACK = Stack.class;
    public static final Class<Queue> QUEUE = Queue.class;

    private static final Set<Class<?>> SIMPLE_TYPES = Sets.newHashSet(
            LONG, BOOLEAN, CHARACTER, BYTE, DATE, DOUBLE, ENUM, FLOAT, INTEGER, SHORT, STRING);
    private static final Set<Class<?>> COLLECTION_TYPES = Sets.newHashSet(
            LIST, SET, MAP, STACK, QUEUE);

    // ==================== is operator ====================

    /**
     * 判断放给定的class是否是{@link Long}
     *
     * @param clazz class对象实例
     * @return true Long false not Long
     */
    public static boolean isLong(Class<?> clazz) {
        return LONG.isAssignableFrom(clazz);
    }

    /**
     * 判断放给定的class是否是{@link Boolean}
     *
     * @param clazz class对象实例
     * @return true Boolean false not Boolean
     */
    public static boolean isBoolean(Class<?> clazz) {
        return BOOLEAN.isAssignableFrom(clazz);
    }

    /**
     * 判断放给定的class是否是{@link Character}
     *
     * @param clazz class对象实例
     * @return true Character false not Character
     */
    public static boolean isChar(Class<?> clazz) {
        return CHARACTER.isAssignableFrom(clazz);
    }

    /**
     * 判断放给定的class是否是{@link Byte}
     *
     * @param clazz class对象实例
     * @return true Byte false not Byte
     */
    public static boolean isByte(Class<?> clazz) {
        return BYTE.isAssignableFrom(clazz);
    }

    /**
     * 判断放给定的class是否是{@link Date}
     *
     * @param clazz class对象实例
     * @return true Date false not Date
     */
    public static boolean isDate(Class<?> clazz) {
        return DATE.isAssignableFrom(clazz);
    }

    /**
     * 判断放给定的class是否是{@link Double}
     *
     * @param clazz class对象实例
     * @return true Double false not Double
     */
    public static boolean isDouble(Class<?> clazz) {
        return DOUBLE.isAssignableFrom(clazz);
    }

    /**
     * 判断放给定的class是否是{@link Enum}
     *
     * @param clazz class对象实例
     * @return true Enum false not Enum
     */
    public static boolean isEnum(Class<?> clazz) {
        return ENUM.isAssignableFrom(clazz);
    }

    /**
     * 判断放给定的class是否是{@link Float}
     *
     * @param clazz class对象实例
     * @return true Float false not Float
     */
    public static boolean isFloat(Class<?> clazz) {
        return FLOAT.isAssignableFrom(clazz);
    }

    /**
     * 判断放给定的class是否是{@link Integer}
     *
     * @param clazz class对象实例
     * @return true Integer false not Integer
     */
    public static boolean isInteger(Class<?> clazz) {
        return INTEGER.isAssignableFrom(clazz);
    }

    /**
     * 判断放给定的class是否是{@link Short}
     *
     * @param clazz class对象实例
     * @return true Short false not Short
     */
    public static boolean isShort(Class<?> clazz) {
        return SHORT.isAssignableFrom(clazz);
    }

    /**
     * 判断放给定的class是否是{@link String}
     *
     * @param clazz class对象实例
     * @return true String false not String
     */
    public static boolean isString(Class<?> clazz) {
        return STRING.isAssignableFrom(clazz);
    }

    /**
     * 判断给定的class是否是{@link java.util.Map}
     *
     * @param clazz class对象实例
     * @return true Map false not map
     */
    public static boolean isMap(Class<?> clazz) {
        return MAP.isAssignableFrom(clazz);
    }

    /**
     * 判断给定的class是否是{@link java.util.List}
     *
     * @param clazz class对象实例
     * @return true List false not list
     */
    public static boolean isList(Class<?> clazz) {
        return LIST.isAssignableFrom(clazz);
    }

    /**
     * 判断给定的class是否是{@link java.util.Set}
     *
     * @param clazz class对象实例
     * @return true Set false not Set
     */
    public static boolean isSet(Class<?> clazz) {
        return SET.isAssignableFrom(clazz);
    }

    /**
     * 判断给定的class是否是{@link java.util.Stack}
     *
     * @param clazz class对象实例
     * @return true Stack false not Stack
     */
    public static boolean isStack(Class<?> clazz) {
        return STACK.isAssignableFrom(clazz);
    }

    /**
     * 判断给定的class是否是{@link java.util.Queue}
     *
     * @param clazz class对象实例
     * @return true Queue false not Queue
     */
    public static boolean isQueue(Class<?> clazz) {
        return QUEUE.isAssignableFrom(clazz);
    }

    /**
     * 判断给定的class是否是{@link java.util.Queue}
     *
     * @param clazz class对象实例
     * @return true array false not array
     */
    public static boolean isArray(Class<?> clazz) {
        return clazz.isArray();
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

    // ==================== to operator ====================

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

    /**
     * 整形数据转换为bool
     *
     * @param i 1 or 0
     * @return i为null 则返回 false 1 = true 、0 = false or false
     */
    public static Boolean toBoolean(Integer i) {
        if (i == null) {
            return Boolean.FALSE;
        }
        if (i >= 1) {
            return Boolean.TRUE;
        } else if (i == 0) {
            return Boolean.FALSE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * 整形数据转换为bool
     *
     * @param o o
     * @return i为null 则返回 false > 1 -> true 、0 = false or false
     */
    public static Boolean parseBoolean(Object o) {
        if (o == null) {
            return Boolean.FALSE;
        }
        if (Types.isBoolean(o.getClass())) {
            return (Boolean) o;
        }
        if (Types.isInteger(o.getClass())) {
            return toBoolean((Integer) o);
        }
        if (Types.isLong(o.getClass())) {
            return toBoolean(((Long) o).intValue());
        }
        if (Types.isString(o.getClass())) {
            try {
                return toBoolean(Integer.valueOf((String) o));
            } catch (ClassCastException | NumberFormatException ex) {
                // ignore
                return Boolean.FALSE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 判断给定的值是否为类型空值
     *
     * @param value 值
     * @return true empty false not empty
     */
    public static boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof Integer && Types.INTEGER_EMPTY.equals(value)) {
            return true;
        } else if (value instanceof Long && Types.LONG_EMPTY.equals(value)) {
            return true;
        } else if (value instanceof Byte && Types.BYTE_EMPTY.equals(value)) {
            return true;
        } else if (value instanceof Double && Types.DOUBLE_EMPTY.equals(value)) {
            return true;
        } else if (value instanceof Float && Types.FLOAT_EMPTY.equals(value)) {
            return true;
        } else if (value instanceof Short && Types.SHORT_EMPTY.equals(value)) {
            return true;
        } else if (value instanceof String && Types.STRING_EMPTY.equals(value)) {
            return true;
        } else if (value.getClass().isArray()) {
            return ((Object[]) value).length > 0;
        }
        return false;
    }

    /**
     * 判断给定的值是否不为类型空值
     *
     * @param value 值
     * @return true not empty false empty
     */
    public static boolean isNotEmpty(Object value) {
        return !isEmpty(value);
    }

    // ==================== get operator ====================

    /**
     * 获取Integer类型的数据
     *
     * @param value 原始类型
     * @return Integer or null
     */
    public static Integer getInteger(Object value) {
        if (isInteger(value.getClass())) {
            return (Integer) value;
        }
        return null;
    }

    /**
     * 获取Double类型的数据
     *
     * @param value 原始类型
     * @return Double or null
     */
    public static Double getDouble(Object value) {
        if (isFloat(value.getClass())) {
            return ((Float) value).doubleValue();
        } else if (isDouble(value.getClass())) {
            return (Double) value;
        }
        return null;
    }

    /**
     * 获取Float类型的数据
     *
     * @param value 原始类型
     * @return Float or null
     */
    public static Float getFloat(Object value) {
        if (isFloat(value.getClass())) {
            return (Float) value;
        }
        return null;
    }

    /**
     * 获取Long类型的数据
     *
     * @param value 原始类型
     * @return Long or null
     */
    public static Long getLong(Object value) {
        if (isShort(value.getClass())) {
            return ((Short) value).longValue();
        } else if (isInteger(value.getClass())) {
            return ((Integer) value).longValue();
        } else if (isLong(value.getClass())) {
            return (Long) value;
        }
        return null;
    }

    /**
     * 获取Boolean类型的数据
     *
     * @param value 原始类型
     * @return Boolean or null
     */
    public static Boolean getBoolean(Object value) {
        if (isBoolean(value.getClass())) {
            return (Boolean) value;
        }
        return null;
    }

    /**
     * 获取Short类型的数据
     *
     * @param value 原始类型
     * @return Short or null
     */
    public static Short getShort(Object value) {
        if (isShort(value.getClass())) {
            return (Short) value;
        }
        return null;
    }

    /**
     * 获取Byte类型的数据
     *
     * @param value 原始类型
     * @return Byte or null
     */
    public static Byte getByte(Object value) {
        if (isByte(value.getClass())) {
            return (Byte) value;
        }
        return null;
    }

    /**
     * 获取String类型的数据
     *
     * @param value 原始类型
     * @return String or null
     */
    public static String getString(Object value) {
        if (isString(value.getClass())) {
            return (String) value;
        }
        return null;
    }

    /**
     * 获取String类型的数据
     *
     * @param value 原始类型
     * @return String or null
     */
    public static Date getDate(Object value) {
        if (isDate(value.getClass())) {
            return (Date) value;
        }
        return null;
    }

    /**
     * 尝试转换为 数字类型
     *
     * @param o 原始数据
     * @return cast for numeric or o
     */
    public static Object tryToNumeric(Object o) {
        Object tryToValue = null;
        // 尝试转换为数字类型
        try {
            tryToValue = new Integer(toString(o));
        } catch (NumberFormatException err) {
            // ignore
        }
        if (tryToValue != null) {
            return tryToValue;
        }
        try {
            tryToValue = new Double(toString(o));
        } catch (NumberFormatException err) {
            // ignore
        }
        if (tryToValue != null) {
            return tryToValue;
        }
        return o;
    }
}
