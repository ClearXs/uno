package cc.allio.uno.core.util.type;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author jiangwei
 * @date 2021/12/23 20:35
 * @since 1.0
 */
public class TypeOperatorFactory {

    private TypeOperatorFactory() {
    }

    private static final Map<Class<?>, TypeOperator> TRANSLATOR_MAP = new HashMap<>();

    private static final TypeOperator DEFAULT_TYPE_OPERATOR = new DefaultTypeOperator();

    static {
        TRANSLATOR_MAP.put(BigDecimal.class, new BigDecimalTypeOperator());
        TRANSLATOR_MAP.put(Integer.class, new IntegerTypeOperator());
        TRANSLATOR_MAP.put(Boolean.class, new BooleanCalculateOperator());
        TRANSLATOR_MAP.put(Byte.class, new ByteTypeOperator());
        TRANSLATOR_MAP.put(Double.class, new DoubleTypeOperator());
        TRANSLATOR_MAP.put(Short.class, new ShortTypeOperator());
        TRANSLATOR_MAP.put(String.class, new StringTypeOperator());
        TRANSLATOR_MAP.put(Date.class, new DateCalculateOperator());
        TRANSLATOR_MAP.put(Enum.class, new EnumTypeOperator());
        TRANSLATOR_MAP.put(Long.class, new LongTypeOperator());
    }

    /**
     * 根据指定额离线获取转换器
     *
     * @param type 某个指定类型
     * @return 转换器实例
     */
    public static <T> TypeOperator translator(Class<T> type) {
        return Optional
                .ofNullable(TRANSLATOR_MAP.get(type))
                .orElse(TRANSLATOR_MAP.getOrDefault(type.getSuperclass(), DEFAULT_TYPE_OPERATOR));
    }
}
