package cc.allio.uno.core.type;

import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author jiangwei
 * @date 2021/12/23 20:35
 * @modifyDate 2024/01/13 18:32
 * @since 1.1.6
 */
public class TypeOperatorFactory {

    private TypeOperatorFactory() {
    }

    private static final Map<Class<?>, Function<Class<?>, TypeOperator<?>>> TRANSLATOR_FUNC_MAP = Maps.newConcurrentMap();
    private static final TypeOperator<?> DEFAULT_TYPE_OPERATOR = new DefaultTypeOperator();

    private static final TypeOperator<BigDecimal> BIG_DECIMAL_TYPE_OPERATOR = new BigDecimalTypeOperator();
    private static final TypeOperator<Integer> INTEGER_TYPE_OPERATOR = new IntegerTypeOperator();
    private static final TypeOperator<Boolean> BOOLEAN_TYPE_OPERATOR = new BooleanCalculateOperator();
    private static final TypeOperator<Byte> BYTE_TYPE_OPERATOR = new ByteTypeOperator();
    private static final TypeOperator<Double> DOUBLE_TYPE_OPERATOR = new DoubleTypeOperator();
    private static final TypeOperator<Short> SHORT_TYPE_OPERATOR = new ShortTypeOperator();
    private static final TypeOperator<String> STRING_TYPE_OPERATOR = new StringTypeOperator();
    private static final TypeOperator<Date> DATE_TYPE_OPERATOR = new DateCalculateOperator();
    private static final TypeOperator<Long> LONG_TYPE_OPERATOR = new LongTypeOperator();
    private static final TypeOperator<Float> FLOAT_TYPE_OPERATOR = new FloatTypeOperator();


    static {
        addTypeOperator(Types.BIG_DECIMAL, type -> BIG_DECIMAL_TYPE_OPERATOR);
        addTypeOperator(Types.INTEGER, type -> INTEGER_TYPE_OPERATOR);
        addTypeOperator(Types.BOOLEAN, type -> BOOLEAN_TYPE_OPERATOR);
        addTypeOperator(Types.BYTE, type -> BYTE_TYPE_OPERATOR);
        addTypeOperator(Types.DOUBLE, type -> DOUBLE_TYPE_OPERATOR);
        addTypeOperator(Types.SHORT, type -> SHORT_TYPE_OPERATOR);
        addTypeOperator(Types.STRING, type -> STRING_TYPE_OPERATOR);
        addTypeOperator(Types.DATE, type -> DATE_TYPE_OPERATOR);
        addTypeOperator(Types.LONG, type -> LONG_TYPE_OPERATOR);
        addTypeOperator(Types.FLOAT, type -> FLOAT_TYPE_OPERATOR);
    }

    /**
     * 根据指定额离线获取转换器
     *
     * @param type 某个指定类型
     * @return 转换器实例
     */
    public static synchronized <T> TypeOperator<T> translator(Class<T> type) {
        if (type.isEnum()) {
            return (TypeOperator<T>) new EnumTypeOperator(type);
        }
        Function<Class<?>, TypeOperator<?>> func = TRANSLATOR_FUNC_MAP.get(type);
        if (func == null) {
            return (TypeOperator<T>) DEFAULT_TYPE_OPERATOR;
        } else {
            TypeOperator<?> typeOperator = func.apply(type);
            return Optional.ofNullable((TypeOperator<T>) typeOperator)
                    .orElseGet(() -> (TypeOperator<T>) DEFAULT_TYPE_OPERATOR);
        }
    }

    /**
     * 添加{@link TypeOperator}
     *
     * @param type 类型
     * @param func func
     */
    public static synchronized void addTypeOperator(Class<?> type, Function<Class<?>, TypeOperator<?>> func) {
        TRANSLATOR_FUNC_MAP.put(type, func);
    }
}
