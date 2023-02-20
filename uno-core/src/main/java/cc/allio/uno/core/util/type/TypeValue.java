package cc.allio.uno.core.util.type;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * Type-Value
 *
 * @author jiangwei
 * @date 2023/1/3 16:32
 * @since 1.1.4
 */
@Data
@Slf4j
public class TypeValue {

    /**
     * 值可能的类型
     */
    private final Class<?> maybeType;

    /**
     * 当前值
     */
    private final Object value;

    /**
     * 转换器
     */
    private final TypeOperator maybeTypeOperator;

    public TypeValue(Class<?> maybeType, Object value) {
        this(maybeType, value, TypeOperatorFactory.translator(maybeType));
    }

    public TypeValue(Class<?> maybeType, Object value, TypeOperator typeOperator) {
        this.maybeType = maybeType;
        this.value = value;
        this.maybeTypeOperator = typeOperator;
    }

    /**
     * 以0->0对应关系对应
     *
     * @param types  Class类型
     * @param values 具体值
     * @return 多数据源
     */
    public static Flux<TypeValue> of(Class<?>[] types, Object[] values) {
        if (values == null || types.length != values.length) {
            return Flux.empty();
        }
        return Flux.zip(
                Flux.fromArray(types),
                Flux.fromArray(values),
                (t, v) -> new TypeValue(t, v, TypeOperatorFactory.translator(t)));
    }

    /**
     * 尝试按照当前存储值可能的类型转换
     * 1.强制转换
     * 2.找到当前可能类型，使用转换器转换
     *
     * @return 值可能正确的类型
     */
    public Object tryTransfer() {
        Object maybeActual = value;
        try {
            maybeType.cast(maybeActual);
        } catch (ClassCastException e) {
            log.debug("Try cast current value {} for type {} failed", value, maybeType.getName());
            try {
                maybeActual = maybeTypeOperator.convert(maybeActual, maybeType);
            } catch (NullPointerException | NumberFormatException e2) {
                log.debug("user translator {} get actual value {} type failed", maybeTypeOperator.getClass().getName(), value);
                return value;
            }
        }
        return maybeActual;
    }
}
