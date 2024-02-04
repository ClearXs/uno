package cc.allio.uno.core.type;

import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.function.lambda.MethodReferenceColumn;

import java.util.Arrays;
import java.util.Set;

/**
 * Enum类型转化，包含以下几种转换方法：
 * <ul>
 *     <li>以枚举类型的名称作为比较条件，获取对应的枚举类型。比如：EXAMPLE("name")，EXAMPLE就是枚举类型名称</li>
 *     <li>如果第一种不匹配，则按照枚举类里面可能为值的字段名称作比较，如：value、label...</li>
 *     <li>{@link #convert(Object, Class, String)}采用该方法，给定值</li>
 * </ul>
 *
 * @author jiangwei
 * @date 2021/12/24 00:59
 * @since 1.0
 */
public class EnumTypeOperator<T extends Enum<?>> extends UnsupportedCalculateOperator<T> {

    private static final Set<String> maybeValueTexts = Set.of("value", "label");

    private final Class<?> enumType;

    public EnumTypeOperator(Class<?> enumType) {
        this.enumType = enumType;
    }

    @Override
    public T convert(Object target, Class<?> maybeType) {
        Object[] enumConstants = maybeType.getEnumConstants();
        if (enumConstants == null) {
            return null;
        }
        return (T) Arrays.stream(enumConstants)
                .filter(constant -> {
                    String name = ((Enum<?>) constant).name();
                    if (target.equals(name)) {
                        return true;
                    }
                    ObjectWrapper constantWrapper = new ObjectWrapper(constant);
                    for (String maybeValueText : maybeValueTexts) {
                        Object force = constantWrapper.getForce(maybeValueText);
                        if (target.equals(force)) {
                            return true;
                        }
                    }
                    return false;
                })
                .findFirst()
                .orElseGet(() -> target);
    }

    /**
     * 通过给定取值的字段名称，进行枚举类型的转化
     *
     * @param target    target
     * @param valueText 给定取值的字段
     * @return enum
     */
    public T convert(Object target, String valueText) {
        return convert(target, getType(), valueText);
    }

    /**
     * 通过给定取值的字段名称，进行枚举类型的转化
     *
     * @param target    target
     * @param valueText 给定取值的字段
     * @return enum
     */
    public T convert(Object target, MethodReferenceColumn<T> valueText) {
        return convert(target, getType(), valueText.getColumn());
    }

    /**
     * 通过给定取值的字段名称，进行枚举类型的转化
     *
     * @param target    target
     * @param maybeType maybeType
     * @param valueText 给定取值的字段
     * @return enum
     */
    public T convert(Object target, Class<?> maybeType, MethodReferenceColumn<T> valueText) {
        return convert(target, maybeType, valueText.getColumn());
    }

    /**
     * 通过给定取值的字段名称，进行枚举类型的转化
     *
     * @param target    target
     * @param maybeType maybeType
     * @param valueText 给定取值的字段
     * @return enum
     */
    public T convert(Object target, Class<?> maybeType, String valueText) {
        Object[] enumConstants = maybeType.getEnumConstants();
        return (T) Arrays.stream(enumConstants)
                .filter(constant -> {
                    Object force = new ObjectWrapper(constant).getForce(valueText);
                    return target.equals(force);
                })
                .findFirst()
                .orElse(target);
    }

    @Override
    public int signum(Object target) {
        return 0;
    }

    @Override
    public String fromString(Object target) {
        return convert(target, getType()).toString();
    }

    @Override
    public Class<?> getType() {
        return enumType;
    }
}
