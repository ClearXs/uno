package cc.allio.uno.core.util.type;

import cc.allio.uno.core.StringPool;

/**
 * Short类型的转换器。可能抛出NumberFormatException异常
 *
 * @author jiangwei
 * @date 2021/12/23 20:11
 * @since 1.0
 */
public class ShortTypeOperator implements TypeOperator {
    @Override
    public Object convert(Object target, Class<?> maybeType) {
        return Short.parseShort(target.toString());
    }

    @Override
    public int signum(Object target) {
        short aShort = Short.parseShort(target.toString());
        return Short.compare(aShort, Short.parseShort(StringPool.ZERO));
    }

    @Override
    public String fromString(Object target) {
        return convert(target, Short.class).toString();
    }

    @Override
    public Object add(Object origin, Object passive) {
        return Short.parseShort(origin.toString());
    }

    @Override
    public Object subtract(Object origin, Object passive) {
        return null;
    }

    @Override
    public Object multiply(Object origin, Object passive) {
        return null;
    }

    @Override
    public Object divide(Object origin, Object passive) {
        return null;
    }

    @Override
    public Object defaultValue() {
        return 0;
    }
}
