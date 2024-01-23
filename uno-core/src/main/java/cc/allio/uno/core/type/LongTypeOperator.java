package cc.allio.uno.core.type;

/**
 * Long类型转换器，可能抛出NumberFormatException异常
 *
 * @author jiangwei
 * @date 2021/12/24 15:42
 * @since 1.0
 */
public class LongTypeOperator implements TypeOperator<Long> {

    @Override
    public Long convert(Object target, Class<?> maybeType) {
        return Types.parseLong(target);
    }

    @Override
    public int signum(Object target) {
        long aLong = convert(target);
        return Long.signum(aLong);
    }

    @Override
    public String fromString(Object target) {
        return convert(target).toString();
    }

    @Override
    public Long add(Long origin, Long passive) {
        return origin + passive;
    }

    @Override
    public Long subtract(Long origin, Long passive) {
        return origin - passive;
    }

    @Override
    public Long multiply(Long origin, Long passive) {
        return origin * passive;
    }

    @Override
    public Long divide(Long origin, Long passive) {
        return origin / passive;
    }

    @Override
    public Long defaultValue() {
        return 0L;
    }

    @Override
    public Class<?> getType() {
        return Types.LONG;
    }
}
