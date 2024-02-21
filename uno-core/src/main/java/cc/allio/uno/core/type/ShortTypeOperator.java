package cc.allio.uno.core.type;

import cc.allio.uno.core.StringPool;

/**
 * Short类型的转换器。可能抛出NumberFormatException异常
 *
 * @author jiangwei
 * @date 2021/12/23 20:11
 * @since 1.0
 */
public class ShortTypeOperator implements TypeOperator<Short> {
    @Override
    public Short convert(Object target, Class<?> maybeType) {
        return Types.parseShort(target);
    }

    @Override
    public int signum(Object target) {
        short aShort = convert(target);
        return Short.compare(aShort, defaultValue());
    }

    @Override
    public String fromString(Object target) {
        return convert(target).toString();
    }

    @Override
    public Short add(Short origin, Short passive) {
        return (short) (origin + passive);
    }

    @Override
    public Short subtract(Short origin, Short passive) {
        return (short) (origin - passive);
    }

    @Override
    public Short multiply(Short origin, Short passive) {
        return (short) (origin * passive);
    }

    @Override
    public Short divide(Short origin, Short passive) {
        return (short) (origin / passive);
    }

    @Override
    public Short defaultValue() {
        return 0;
    }

    @Override
    public Class<?> getType() {
        return Types.SHORT;
    }
}
