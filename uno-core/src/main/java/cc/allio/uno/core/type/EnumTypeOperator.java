package cc.allio.uno.core.type;

import java.util.Arrays;

/**
 * Enum类型转化，以枚举类型的名称作为比较条件，获取对应的枚举类型。比如：EXAMPLE("name")，EXAMPLE就是枚举类型名称
 *
 * @author jiangwei
 * @date 2021/12/24 00:59
 * @since 1.0
 */
public class EnumTypeOperator extends UnsupportedCalculateOperator {

    @Override
    public Object convert(Object target, Class<?> maybeType) {
        Object[] enumConstants = maybeType.getEnumConstants();
        return Arrays.stream(enumConstants)
                .filter(constant -> ((Enum<?>) constant).name().equals(target.toString()))
                .findFirst()
                .orElse(target);
    }

    @Override
    public int signum(Object target) {
        return 0;
    }

    @Override
    public String fromString(Object target) {
        return convert(target, Enum.class).toString();
    }

    @Override
    public Class<?> getType() {
        return Types.ENUM;
    }
}
