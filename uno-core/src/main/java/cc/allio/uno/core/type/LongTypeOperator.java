package cc.allio.uno.core.type;

/**
 * Long类型转换器，可能抛出NumberFormatException异常
 *
 * @author jiangwei
 * @date 2021/12/24 15:42
 * @since 1.0
 */
public class LongTypeOperator implements TypeOperator {

    @Override
    public Object convert(Object target, Class<?> maybeType) {
        return Long.parseLong(target.toString());
    }

    @Override
    public int signum(Object target) {
        long aLong = Long.parseLong(target.toString());
        return Long.signum(aLong);
    }

    @Override
    public String fromString(Object target) {
        return convert(target, Long.class).toString();
    }

    @Override
    public Object add(Object origin, Object passive) {
        return Long.parseLong(origin.toString()) + Long.parseLong(passive.toString());
    }

    @Override
    public Object subtract(Object origin, Object passive) {
        return Long.parseLong(origin.toString()) - Long.parseLong(passive.toString());
    }

    @Override
    public Object multiply(Object origin, Object passive) {
        return Long.parseLong(passive.toString()) * Long.parseLong(passive.toString());
    }

    @Override
    public Object divide(Object origin, Object passive) {
        return Long.parseLong(origin.toString()) / Long.parseLong(passive.toString());
    }

    @Override
    public Object defaultValue() {
        return 0L;
    }

    @Override
    public Class<?> getType() {
        return Types.LONG;
    }
}
