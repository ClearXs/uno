package cc.allio.uno.core.type;

import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Date;
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

    private static final Map<Class<?>, TypeOperator> TRANSLATOR_MAP = Maps.newConcurrentMap();
    private static final TypeOperator DEFAULT_TYPE_OPERATOR = new DefaultTypeOperator();

    static {
        addTypeOperator(BigDecimal.class, new BigDecimalTypeOperator());
        addTypeOperator(Integer.class, new IntegerTypeOperator());
        addTypeOperator(Boolean.class, new BooleanCalculateOperator());
        addTypeOperator(Byte.class, new ByteTypeOperator());
        addTypeOperator(Double.class, new DoubleTypeOperator());
        addTypeOperator(Short.class, new ShortTypeOperator());
        addTypeOperator(String.class, new StringTypeOperator());
        addTypeOperator(Date.class, new DateCalculateOperator());
        addTypeOperator(Enum.class, new EnumTypeOperator());
        addTypeOperator(Long.class, new LongTypeOperator());
    }

    /**
     * 根据指定额离线获取转换器
     *
     * @param type 某个指定类型
     * @return 转换器实例
     */
    public static synchronized <T> TypeOperator translator(Class<T> type) {
        return Optional
                .ofNullable(TRANSLATOR_MAP.get(type))
                .orElseGet(() ->
                        Optional.ofNullable(type.getSuperclass())
                                .map(TRANSLATOR_MAP::get)
                                .orElse(DEFAULT_TYPE_OPERATOR));
    }

    /**
     * 添加{@link TypeOperator}
     *
     * @param type         类型
     * @param typeOperator 类型操作
     */
    public static synchronized void addTypeOperator(Class<?> type, TypeOperator typeOperator) {
        TRANSLATOR_MAP.put(type, typeOperator);
    }
}
